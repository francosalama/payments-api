package com.link.payments.mapper;

import com.link.payments.dto.BankTransferRequest;
import com.link.payments.model.BankTransfer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankTransferMapper {

   BankTransferMapper INSTANCE = Mappers.getMapper(BankTransferMapper.class);

   @Mapping(target = "transactionId", source = "transactionId")
   BankTransfer toEntity(BankTransferRequest request, Long transactionId);

}
