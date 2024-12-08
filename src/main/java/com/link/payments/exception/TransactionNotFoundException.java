package com.link.payments.exception;

public class TransactionNotFoundException extends RuntimeException {

   public TransactionNotFoundException(String message) {
      super(message);
   }

}
