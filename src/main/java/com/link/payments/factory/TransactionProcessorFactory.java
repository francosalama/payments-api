package com.link.payments.factory;

import com.link.payments.enums.ETransactionType;
import com.link.payments.service.processor.TransactionProcessor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransactionProcessorFactory {

   private final Map<ETransactionType, TransactionProcessor> processorMap;

   public TransactionProcessorFactory(List<TransactionProcessor> processors) {
      this.processorMap = processors.stream().collect(Collectors.toMap(TransactionProcessor::getType, processor -> processor));
   }

   public TransactionProcessor getProcessor(ETransactionType type) {
      TransactionProcessor processor = processorMap.get(type);
      if (processor == null) {
         throw new IllegalArgumentException("Unsupported transaction type: " + type);
      }
      return processor;
   }

}
