package com.link.payments.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class P2PTransactionRequest {

   @NotBlank(message = "The sender ID cannot be empty.")
   private String senderId;

   @NotBlank(message = "The recipient ID cannot be empty.")
   private String recipientId;

   private String note;

}
