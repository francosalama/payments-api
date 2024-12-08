package com.link.payments.repository;

import com.link.payments.model.P2PTransfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface P2PTransactionRepository extends JpaRepository<P2PTransfer, Long> {

}