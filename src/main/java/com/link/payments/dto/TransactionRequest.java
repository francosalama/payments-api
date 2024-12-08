package com.link.payments.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.link.payments.annotation.ValidTransactionType;
import com.link.payments.enums.ETransactionType;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionRequest {

   @NotBlank(message = "The userId cannot be empty.")
   private String userId;

   @NotNull(message = "The amount cannot be null.")
   @DecimalMin(value = "0.01", message = "The amount must be greater than 0.")
   private BigDecimal amount;

   @NotBlank(message = "The currency cannot be empty.")
   @Size(min = 3, max = 3, message = "The currency must be a 3-character ISO code.")
   private String currency;

   @NotBlank(message = "The transaction type cannot be empty.")
   @ValidTransactionType
   private String type;

   private CardTransactionRequest cardDetails;

   private BankTransferRequest bankDetails;

   private P2PTransactionRequest p2pDetails;

}
