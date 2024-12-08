package com.link.payments.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "card_transaction")
@Data
public class CardTransaction {

   @Id
   @Column(name = "transaction_id")
   private Long transactionId;

   @Column(name = "card_id")
   private String cardId;

   @Column(name = "merchant_name")
   private String merchantName;

   @Column(name = "merchant_id")
   private String merchantId;

   @Column(name = "mcc_code")
   private Integer mccCode;

}