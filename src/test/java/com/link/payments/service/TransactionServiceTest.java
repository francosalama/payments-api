package com.link.payments.service;

import com.link.payments.client.ExchangeRateApiClient;
import com.link.payments.dto.TransactionRequest;
import com.link.payments.dto.TransactionResponse;
import com.link.payments.dto.external.ExchangeRateApiResponse;
import com.link.payments.enums.ETransactionType;
import com.link.payments.exception.DuplicateIdempotencyKeyException;
import com.link.payments.exception.TransactionNotFoundException;
import com.link.payments.factory.TransactionProcessorFactory;
import com.link.payments.model.Transaction;
import com.link.payments.repository.TransactionRepository;
import com.link.payments.service.processor.TransactionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExchangeRateApiClient exchangeRateApiClient;

    @Mock
    private TransactionProcessorFactory processorFactory;

    @Mock
    private TransactionProcessor transactionProcessor;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionRequest request;

    private String idempotencyKey;

    private Long transactionId;

    private String userId;

    private String status;

    private String currency;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        request = new TransactionRequest();
        request.setCurrency("USD");
        request.setAmount(BigDecimal.valueOf(100));
        request.setType("CARD");
        idempotencyKey = "unique-key-123";
        transactionId = 1L;
        userId = "user123";
        status = "COMPLETED";
        currency = "USD";
        startDate = LocalDateTime.now().minusDays(1);
        endDate = LocalDateTime.now();
    }

    @Test
    void testCreateTransactionWhenIdempotencyKeyExists() {
        when(transactionRepository.existsByIdempotencyKey(idempotencyKey)).thenReturn(true);

        DuplicateIdempotencyKeyException exception = assertThrows(DuplicateIdempotencyKeyException.class, () ->
                transactionService.createTransaction(request, idempotencyKey)
        );
        assertEquals("The provided idempotency key has already been used: unique-key-123", exception.getMessage());
    }

    @Test
    void testCreateTransactionWhenCurrencyIsUSD() {
        when(transactionRepository.existsByIdempotencyKey(idempotencyKey)).thenReturn(false);

        Transaction transaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        when(processorFactory.getProcessor(any(ETransactionType.class))).thenReturn(transactionProcessor);

        TransactionResponse response = transactionService.createTransaction(request, idempotencyKey);

        assertNotNull(response);
        verify(transactionRepository).existsByIdempotencyKey(idempotencyKey);
        verify(transactionRepository).save(any(Transaction.class));
        verify(transactionProcessor).processTransaction(any(TransactionRequest.class), any(Transaction.class));
    }

    @Test
    void testCreateTransactionWhenCurrencyIsNotUSD() {
        request.setCurrency("EUR");
        request.setAmount(BigDecimal.valueOf(200));

        ExchangeRateApiResponse exchangeRateApiResponse = new ExchangeRateApiResponse();
        exchangeRateApiResponse.setBaseCode("EUR");
        exchangeRateApiResponse.setTargetCode("USD");
        exchangeRateApiResponse.setConversionRate(BigDecimal.valueOf(1.2));
        exchangeRateApiResponse.setConversionResult(BigDecimal.valueOf(240));

        when(transactionRepository.existsByIdempotencyKey(idempotencyKey)).thenReturn(false);
        when(exchangeRateApiClient.getExchangeRate("EUR", BigDecimal.valueOf(200)))
                .thenReturn(exchangeRateApiResponse);

        Transaction transaction = new Transaction();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        when(processorFactory.getProcessor(any(ETransactionType.class))).thenReturn(transactionProcessor);

        TransactionResponse response = transactionService.createTransaction(request, idempotencyKey);

        assertNotNull(response);
        verify(exchangeRateApiClient).getExchangeRate("EUR", BigDecimal.valueOf(200));
        verify(transactionRepository).save(any(Transaction.class));
        verify(transactionProcessor).processTransaction(any(TransactionRequest.class), any(Transaction.class));
    }

    @Test
    void testGetTransactionWhenTransactionExists() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        TransactionResponse response = transactionService.getTransaction(transactionId);

        assertNotNull(response);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void testGetTransactionWhenTransactionNotFound() {
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () ->
                transactionService.getTransaction(transactionId)
        );
        assertEquals("Transaction not found: 1", exception.getMessage());
    }

    @Test
    void testListUserTransactionsWhenTransactionsExist() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        when(transactionRepository.findByUserIdAndFilters(userId, status, currency, startDate, endDate))
                .thenReturn(transactions);

        List<TransactionResponse> response = transactionService.listUserTransactions(userId, status, currency, startDate, endDate);

        assertNotNull(response);
        assertEquals(2, response.size());
        verify(transactionRepository).findByUserIdAndFilters(userId, status, currency, startDate, endDate);
    }

    @Test
    void testListUserTransactionsWhenNoTransactionsFound() {
        when(transactionRepository.findByUserIdAndFilters(userId, status, currency, startDate, endDate))
                .thenReturn(Collections.emptyList());

        List<TransactionResponse> response = transactionService.listUserTransactions(userId, status, currency, startDate, endDate);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(transactionRepository).findByUserIdAndFilters(userId, status, currency, startDate, endDate);
    }

    @Test
    void testListUserTransactionsWithNullFilters() {
        when(transactionRepository.findByUserIdAndFilters(userId, null, null, null, null))
                .thenReturn(Collections.emptyList());

        List<TransactionResponse> response = transactionService.listUserTransactions(userId, null, null, null, null);

        assertNotNull(response);
        assertTrue(response.isEmpty());
        verify(transactionRepository).findByUserIdAndFilters(userId, null, null, null, null);
    }

}