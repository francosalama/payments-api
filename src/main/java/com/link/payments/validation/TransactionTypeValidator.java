package com.link.payments.validation;

import com.link.payments.annotation.ValidTransactionType;
import com.link.payments.dto.TransactionRequest;
import com.link.payments.enums.ETransactionType;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TransactionTypeValidator implements ConstraintValidator<ValidTransactionType, String> {

   @Override
   public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null) {
         return false;
      }
      try {
         ETransactionType.valueOf(value);
         return true;
      } catch (IllegalArgumentException e) {
         return false;
      }
   }

}
