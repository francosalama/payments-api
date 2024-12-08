package com.link.payments.service.processor;

import com.link.payments.dto.TransactionRequest;
import com.link.payments.enums.ETransactionType;
import com.link.payments.model.Transaction;

public interface TransactionProcessor {

   void processTransaction(TransactionRequest request, Transaction transaction);

   ETransactionType getType();

}
