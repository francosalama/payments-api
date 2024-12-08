package com.link.payments.service.processor;

import com.link.payments.dto.TransactionRequest;
import com.link.payments.enums.ETransactionType;
import com.link.payments.exception.MissingTransactionDetailsException;
import com.link.payments.mapper.CardTransactionMapper;
import com.link.payments.model.CardTransaction;
import com.link.payments.model.Transaction;
import com.link.payments.repository.CardTransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CardTransactionProcessor implements TransactionProcessor {

   private final CardTransactionRepository cardTransactionRepository;

   @Override
   public void processTransaction(TransactionRequest request, Transaction transaction) {
      if (Objects.isNull(request.getCardDetails())) {
         throw new MissingTransactionDetailsException("Missing credit card details");
      }
      CardTransaction cardTransaction = CardTransactionMapper.INSTANCE.toEntity(request.getCardDetails(), transaction.getId());
      cardTransactionRepository.save(cardTransaction);
      transaction.setStatus("COMPLETED");
   }

   @Override
   public ETransactionType getType() {
      return ETransactionType.CARD;
   }

}
