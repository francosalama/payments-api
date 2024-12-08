package com.link.payments.repository;

import com.link.payments.model.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

   @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " + "AND (:status IS NULL OR t.status = :status) "
         + "AND (:currency IS NULL OR t.currency = :currency) " + "AND (:startDate IS NULL OR t.createdAt >= :startDate) "
         + "AND (:endDate IS NULL OR t.createdAt <= :endDate)")
   List<Transaction> findByUserIdAndFilters(String userId, @Param("status") String status, @Param("currency") String currency,
         @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

   boolean existsByIdempotencyKey(String idempotencyKey);

}
