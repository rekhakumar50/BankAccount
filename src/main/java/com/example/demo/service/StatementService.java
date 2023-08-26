package com.example.demo.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.adapter.PrintDataAdapter;
import com.example.demo.dao.Transaction;
import com.example.demo.repository.TransactionRepository;

@Service
public class StatementService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private PrintDataAdapter printData;
	

	public void processStatement(final String input) {
		String[] inputArr = StringUtils.split(input, "|");
		String accNo = inputArr[0];
		String month = inputArr[1];
		
		List<Transaction> transactions = transactionRepository.findByAccNoAndDateLike(accNo, "____"+month+"__");
		
		if(CollectionUtils.isNotEmpty(transactions)) {
			printData.printStatementTable(accNo, transactions);
		}
	}
	
}
