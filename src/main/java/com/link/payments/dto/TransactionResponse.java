package com.link.payments.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionResponse {

   private Long id;

   private String userId;

   private BigDecimal amount;

   private BigDecimal usdAmount;

   private String currency;

   private String type;

   private String status;

   private LocalDateTime createdAt;

}
