package com.link.payments.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bank_transfer")
@Data
public class BankTransfer {

   @Id
   @Column(name = "transaction_id")
   private Long transactionId;

   @Column(name = "bank_code")
   private String bankCode;

   @Column(name = "recipient_account")
   private String recipientAccount;

}