package com.link.payments.exception;

public class DuplicateIdempotencyKeyException extends RuntimeException {

   public DuplicateIdempotencyKeyException(String message) {
      super(message);
   }

}
