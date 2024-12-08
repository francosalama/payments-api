package com.link.payments.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "p2p_transfer")
@Data
public class P2PTransfer {

   @Id
   @Column(name = "transaction_id")
   private Long transactionId;

   @Column(name = "sender_id")
   private String senderId;

   @Column(name = "recipient_id")
   private String recipientId;

   @Column(name = "note")
   private String note;

}