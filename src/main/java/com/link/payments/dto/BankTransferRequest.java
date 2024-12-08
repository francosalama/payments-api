package com.link.payments.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankTransferRequest {

   @NotBlank(message = "The bank code cannot be empty.")
   private String bankCode;

   @NotBlank(message = "The recipient account cannot be empty.")
   private String recipientAccount;

}
