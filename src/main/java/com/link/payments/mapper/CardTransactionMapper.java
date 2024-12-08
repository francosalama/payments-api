package com.link.payments.mapper;

import com.link.payments.dto.CardTransactionRequest;
import com.link.payments.model.CardTransaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardTransactionMapper {

   CardTransactionMapper INSTANCE = Mappers.getMapper(CardTransactionMapper.class);

   @Mapping(target = "transactionId", source = "transactionId")
   CardTransaction toEntity(CardTransactionRequest request, Long transactionId);

}
