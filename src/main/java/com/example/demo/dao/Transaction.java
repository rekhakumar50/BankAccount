package com.example.demo.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String accNo;
	
	private String transactionId;
	
	@Column(nullable = false)
	private String transactionType;
	
	@Column(nullable = false)
	private String date;
	
	@Column(nullable = false)
	private Double amount;
	
	private Double totalAmount;
	
}
