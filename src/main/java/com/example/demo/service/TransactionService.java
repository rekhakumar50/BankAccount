package com.example.demo.service;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.adapter.PrintDataAdapter;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.TransactionDto;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
		
	@Autowired
	private PrintDataAdapter printData;
	
	
	public void processTransaction(final String input) {
		Transaction transaction = null;
		TransactionDto transactionDto = TransactionMapper.convertInputToTransactionDto(input);
		
		boolean isAccNoExist = transactionRepository.existsByAccNo(transactionDto.getAccNo());
		
		
		if(!isAccNoExist && StringUtils.equalsIgnoreCase(transactionDto.getTransactionType(), "W")) {
			System.out.println("Cannot create the account as the tranaction type is Withdraw!!");
			
		} else if(!isAccNoExist && StringUtils.equalsIgnoreCase(transactionDto.getTransactionType(), "D")) {
			transaction = TransactionMapper.convertToTransaction(
					transactionDto, 
					transactionDto.getAmount(), 
					this.computeTransactionId(transactionDto.getDate()));
			
		} else if(isAccNoExist && StringUtils.equalsIgnoreCase(transactionDto.getTransactionType(), "W")) {
			Double totalAmount = transactionRepository.getTotalAmountByDate(transactionDto.getDate());
			Double updateTotalAmount = totalAmount - transactionDto.getAmount();
			if(updateTotalAmount < 0) {
				System.out.println("Cannot withdraw, account balance is low!!");
			} else {
				transaction = TransactionMapper.convertToTransaction(
						transactionDto, 
						updateTotalAmount, 
						this.computeTransactionId(transactionDto.getDate()));
			}
		} else if(isAccNoExist && StringUtils.equalsIgnoreCase(transactionDto.getTransactionType(), "D")) {
			Double totalAmount = transactionRepository.getTotalAmountByDate(transactionDto.getDate());
			Double updateTotalAmount = totalAmount + transactionDto.getAmount();
			transaction = TransactionMapper.convertToTransaction(
					transactionDto, 
					updateTotalAmount, 
					this.computeTransactionId(transactionDto.getDate()));	
		}
		
		if(Objects.nonNull(transaction)) {
			transactionRepository.save(transaction);
			List<Transaction> transactions = transactionRepository.findByAccNo(transactionDto.getAccNo());
			printData.printTransactionTable(transactionDto.getAccNo(), transactions);
		}
	
	}
	
	
	private String computeTransactionId(String date) {
		String transactionId = transactionRepository.getTransactionIdByDate(date);
		if(StringUtils.isNotEmpty(transactionId)) {
			int id = Integer.valueOf(transactionId.split("-")[1])+1;
			return date+"-"+id;
		}
		
		return date+"-"+1;
	}	

}