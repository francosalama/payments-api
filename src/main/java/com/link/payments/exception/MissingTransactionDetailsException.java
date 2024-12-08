package com.link.payments.exception;

public class MissingTransactionDetailsException extends RuntimeException {

   public MissingTransactionDetailsException(String message) {
      super(message);
   }

}
