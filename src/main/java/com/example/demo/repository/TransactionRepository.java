package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dao.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findByAccNo(String accNo);

    @Query(value = "SELECT t.total_amount FROM Transactions t where t.date <= :date ORDER BY t.id DESC LIMIT 1", nativeQuery = true)
	Double getTotalAmountByDate(@Param("date") String date);
    
    @Query(value = "SELECT t.transaction_id FROM Transactions t where t.date = :date ORDER BY t.id DESC LIMIT 1", nativeQuery = true)
	String getTransactionIdByDate(@Param("date") String date);
	
	boolean existsByAccNo(String accNo);
	
	List<Transaction> findByAccNoAndDateLike(String accNo, String date);
	
    @Query(value = "SELECT t.date FROM Transactions t where t.acc_no = :accNo ORDER BY t.id LIMIT 1", nativeQuery = true)
	String findAccStartDate(String accNo);
    
    @Query(value = "SELECT * FROM Transactions t where t.date <= :date and t.acc_no = :accNo ORDER BY t.id DESC LIMIT 1", nativeQuery = true)
	Transaction findByAccNoAndDate(@Param("accNo") String accNo, @Param("date") String date);

}
