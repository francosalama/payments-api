package com.link.payments.controller;

import com.link.payments.dto.TransactionRequest;
import com.link.payments.dto.TransactionResponse;
import com.link.payments.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

   private final TransactionService transactionService;

   @PostMapping
   public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request,
         @RequestHeader("Idempotency-Key") String idempotencyKey) {
      return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createTransaction(request, idempotencyKey));
   }

   @GetMapping("/{id}")
   public TransactionResponse getTransaction(@PathVariable Long id) {
      return transactionService.getTransaction(id);
   }

   @GetMapping("/user/{userId}")
   public List<TransactionResponse> listTransactions(@PathVariable String userId, @RequestParam(required = false) String status,
         @RequestParam(required = false) String currency,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
      return transactionService.listUserTransactions(userId, status, currency, startDate, endDate);
   }

}
