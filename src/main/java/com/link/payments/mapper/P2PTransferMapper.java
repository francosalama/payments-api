package com.link.payments.mapper;

import com.link.payments.dto.P2PTransactionRequest;
import com.link.payments.model.P2PTransfer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface P2PTransferMapper {

   P2PTransferMapper INSTANCE = Mappers.getMapper(P2PTransferMapper.class);

   @Mapping(target = "transactionId", source = "transactionId")
   P2PTransfer toEntity(P2PTransactionRequest request, Long transactionId);

}
