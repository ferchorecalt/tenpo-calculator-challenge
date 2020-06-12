package com.tenpo.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tenpo.challenge.model.TransactionHistory;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

}
