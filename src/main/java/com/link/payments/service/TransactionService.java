package com.link.payments.service;

import com.link.payments.client.ExchangeRateApiClient;
import com.link.payments.dto.TransactionRequest;
import com.link.payments.dto.TransactionResponse;
import com.link.payments.dto.external.ExchangeRateApiResponse;
import com.link.payments.enums.ETransactionType;
import com.link.payments.exception.DuplicateIdempotencyKeyException;
import com.link.payments.exception.TransactionNotFoundException;
import com.link.payments.factory.TransactionProcessorFactory;
import com.link.payments.mapper.TransactionMapper;
import com.link.payments.model.Transaction;
import com.link.payments.repository.TransactionRepository;
import com.link.payments.service.processor.TransactionProcessor;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

   private static final String USD = "USD";

   private final TransactionRepository transactionRepository;

   private final TransactionProcessorFactory processorFactory;

   private final ExchangeRateApiClient exchangeRateApiClient;

   @Transactional(rollbackFor = Exception.class)
   public TransactionResponse createTransaction(TransactionRequest request, String idempotencyKey) {
      if (transactionRepository.existsByIdempotencyKey(idempotencyKey)) {
         throw new DuplicateIdempotencyKeyException("The provided idempotency key has already been used: " + idempotencyKey);
      }

      BigDecimal usdAmount = USD.equalsIgnoreCase(request.getCurrency())
            ? request.getAmount()
            : exchangeRateApiClient.getExchangeRate(request.getCurrency(), request.getAmount()).getConversionResult();

      Transaction transaction = TransactionMapper.INSTANCE.toEntity(request, idempotencyKey, usdAmount);
      transaction = transactionRepository.save(transaction);

      TransactionProcessor processor = processorFactory.getProcessor(ETransactionType.valueOf(request.getType()));
      processor.processTransaction(request, transaction);

      return TransactionMapper.INSTANCE.toDto(transaction);
   }

   public TransactionResponse getTransaction(Long id) {
      Transaction transaction = transactionRepository
            .findById(id)
            .orElseThrow(() -> new TransactionNotFoundException("Transaction not found: " + id));
      return TransactionMapper.INSTANCE.toDto(transaction);
   }

   public List<TransactionResponse> listUserTransactions(String userId, String status, String currency, LocalDateTime startDate,
         LocalDateTime endDate) {
      List<Transaction> transactions = transactionRepository.findByUserIdAndFilters(userId, status, currency, startDate, endDate);
      return TransactionMapper.INSTANCE.toDtoList(transactions);
   }

}
