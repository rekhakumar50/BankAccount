package com.example.demo.mapper;

import static com.example.demo.constant.Constants.PIPE;

import org.apache.commons.lang3.StringUtils;

import com.example.demo.dao.Transaction;
import com.example.demo.dto.TransactionDto;

public class TransactionMapper {
	
	/**
	 * Convert Input string to TransactionDto Object
	 * @param input
	 * @return
	 */
	public static TransactionDto convertInputToTransactionDto(final String input) {
		String[] inputArr = StringUtils.split(input, PIPE);
		
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setDate(inputArr[0]);
		transactionDto.setAccNo(inputArr[1]);
		transactionDto.setTransactionType(inputArr[2]);
		transactionDto.setAmount(Double.valueOf(inputArr[3]));
		
		return transactionDto;
	}
	
	
	/**
	 * Convert TransactionDto to Transaction Object
	 * @param transactionDto
	 * @param totalAmount
	 * @param transactionId
	 * @return
	 */
	public static Transaction convertToTransaction(TransactionDto transactionDto, Double totalAmount, String transactionId) {
		Transaction transaction = new Transaction();
		transaction.setAccNo(transactionDto.getAccNo());
		transaction.setTransactionType(transactionDto.getTransactionType());
		transaction.setDate(transactionDto.getDate());
		transaction.setAmount(transactionDto.getAmount());
		transaction.setTransactionId(transactionId);
		transaction.setTotalAmount(totalAmount);

		return transaction;
	}
	
}
