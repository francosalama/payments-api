package com.link.payments.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

   @ExceptionHandler(InvalidTransactionException.class)
   public ResponseEntity<Map<String, Object>> handleInvalidTransactionException(InvalidTransactionException ex) {
      return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(MissingTransactionDetailsException.class)
   public ResponseEntity<Map<String, Object>> handleMissingTransactionDetailsException(MissingTransactionDetailsException ex) {
      return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(TransactionNotFoundException.class)
   public ResponseEntity<Map<String, Object>> handleTransactionNotFoundException(TransactionNotFoundException ex) {
      return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
      log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
      return buildErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
   }

   @ExceptionHandler(DuplicateIdempotencyKeyException.class)
   public ResponseEntity<String> handleDuplicateIdempotencyKeyException(DuplicateIdempotencyKeyException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap<>();

      ex.getBindingResult().getAllErrors().forEach(error -> {
         String fieldName = ((FieldError) error).getField();
         String errorMessage = error.getDefaultMessage();
         errors.put(fieldName, errorMessage);
      });

      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
   }

   private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
      Map<String, Object> errorDetails = new HashMap<>();
      errorDetails.put("timestamp", LocalDateTime.now());
      errorDetails.put("message", message);
      errorDetails.put("status", status.value());
      errorDetails.put("error", status.getReasonPhrase());
      return new ResponseEntity<>(errorDetails, status);
   }

}
