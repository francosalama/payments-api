package com.link.payments.service.processor;

import com.link.payments.dto.TransactionRequest;
import com.link.payments.enums.ETransactionType;
import com.link.payments.exception.MissingTransactionDetailsException;
import com.link.payments.mapper.P2PTransferMapper;
import com.link.payments.model.P2PTransfer;
import com.link.payments.model.Transaction;
import com.link.payments.repository.P2PTransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor

public class P2PTransactionProcessor implements TransactionProcessor {

   private final P2PTransactionRepository p2pTransactionRepository;

   @Override
   public void processTransaction(TransactionRequest request, Transaction transaction) {
      if (Objects.isNull(request.getP2pDetails())) {
         throw new MissingTransactionDetailsException("Missing P2P transfer details");
      }
      P2PTransfer p2pTransfer = P2PTransferMapper.INSTANCE.toEntity(request.getP2pDetails(), transaction.getId());
      p2pTransactionRepository.save(p2pTransfer);
      transaction.setStatus("COMPLETED");
   }

   @Override
   public ETransactionType getType() {
      return ETransactionType.P2P;
   }

}