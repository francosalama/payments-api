package com.link.payments.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CardTransactionRequest {

   @NotBlank(message = "The card ID cannot be empty.")
   private String cardId;

   @NotBlank(message = "The merchant name cannot be empty.")
   private String merchantName;

   @NotBlank(message = "The merchant ID cannot be empty.")
   private String merchantId;

   @NotNull(message = "The MCC code cannot be null.")
   private Integer mccCode;

}
