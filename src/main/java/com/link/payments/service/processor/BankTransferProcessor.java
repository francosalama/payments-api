package com.link.payments.service.processor;

import com.link.payments.dto.TransactionRequest;
import com.link.payments.enums.ETransactionType;
import com.link.payments.exception.MissingTransactionDetailsException;
import com.link.payments.mapper.BankTransferMapper;
import com.link.payments.model.BankTransfer;
import com.link.payments.model.Transaction;
import com.link.payments.repository.BankTransferRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BankTransferProcessor implements TransactionProcessor {

   private final BankTransferRepository bankTransferRepository;

   @Override
   public void processTransaction(TransactionRequest request, Transaction transaction) {
      if (Objects.isNull(request.getBankDetails())) {
         throw new MissingTransactionDetailsException("Missing bank transfer details");
      }
      BankTransfer bankTransfer = BankTransferMapper.INSTANCE.toEntity(request.getBankDetails(), transaction.getId());
      bankTransferRepository.save(bankTransfer);
      transaction.setStatus("COMPLETED");
   }

   @Override
   public ETransactionType getType() {
      return ETransactionType.BANK_TRANSFER;
   }

}