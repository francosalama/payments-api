package com.link.payments.model;

import com.link.payments.enums.ETransactionType;

import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", unique = true, insertable = false)
   private Long id;

   @Column(name = "user_id")
   private String userId;

   @Column(name = "amount")
   private BigDecimal amount;

   @Column(name = "usd_amount")
   private BigDecimal usdAmount;

   @Column(name = "currency")
   private String currency;

   @Enumerated(EnumType.STRING)
   @Column(name = "type")
   private ETransactionType type;

   @Column(name = "status")
   private String status;

   @Column(name = "idempotency_key")
   private String idempotencyKey;

   @CreationTimestamp
   @Column(name = "created_at")
   private LocalDateTime createdAt;

}