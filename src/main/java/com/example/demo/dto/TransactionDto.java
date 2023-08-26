package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {
	
	private String accNo;
		
	private String transactionType;
	
	private String date;
	
	private Double amount;
		
}
