package com.link.payments.mapper;

import com.link.payments.dto.TransactionRequest;
import com.link.payments.dto.TransactionResponse;
import com.link.payments.model.Transaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TransactionMapper {

   TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

   @Mapping(target = "idempotencyKey", source = "idempotencyKey")
   @Mapping(target = "usdAmount", source = "usdAmount")
   Transaction toEntity(TransactionRequest request, String idempotencyKey, BigDecimal usdAmount);

   TransactionResponse toDto(Transaction transaction);

   List<TransactionResponse> toDtoList(List<Transaction> transaction);

}
