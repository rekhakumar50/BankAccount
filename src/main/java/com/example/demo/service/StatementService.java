package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.adapter.PrintDataAdapter;
import com.example.demo.dao.Transaction;
import com.example.demo.repository.RuleRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.util.Utility;

@Service
public class StatementService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private RuleRepository ruleRepository;
	
	@Autowired
	private PrintDataAdapter printData;
	

	public void processStatement(final String input) {
		String[] inputArr = StringUtils.split(input, "|");
		String accNo = inputArr[0];
		String month = inputArr[1];
		
		int currentYear = Utility.getCurrentYear();
	
		List<Transaction> transactions = transactionRepository.findByAccNoAndDateLike(accNo, currentYear+month+"__");
		if(CollectionUtils.isNotEmpty(transactions)) {
			Double totalAmt = transactions.get(transactions.size() - 1).getTotalAmount();
			Transaction transaction = this.processInterestTransaction(month, totalAmt);
			transactions.add(transaction);
			printData.printStatementTable(accNo, transactions);
		} else {
			System.out.println("Account does not have any transaction details for the month.");
			Transaction transaction = transactionRepository.findByAccNoAndDate(accNo, currentYear+month+"01");
			List<Transaction> transactionList = Arrays.asList(this.processInterestTransaction(month, transaction.getTotalAmount()));
			printData.printStatementTable(accNo, transactionList);
		}
	}
	
	
	public Transaction processInterestTransaction(final String month, final Double totalAmt) {
		Double totalInterest = 0.0;
		int currentYear = Utility.getCurrentYear();
		int lastDateOfMonth = Utility.getLastDayOfMonth(Integer.valueOf(month)-1);
		
		String lastDayOfMonth = currentYear+month+lastDateOfMonth;
		
		for(int i=1;i<=lastDateOfMonth;i++) {
			String date = currentYear + month + String.format("%02d", i);
			Double totalAmount = transactionRepository.getTotalAmountByDate(date);
			Double rate = ruleRepository.getRateByDate(date);
			if(totalAmount != null && rate != null) {
				totalInterest = totalInterest + (totalAmount * (rate/100));
			}
		}
		totalInterest = totalInterest / Utility.getLengthOfYear();
		Double amt = new BigDecimal(totalInterest).setScale(2, RoundingMode.HALF_UP).doubleValue();
		
		Transaction transaction = new Transaction();
		transaction.setDate(lastDayOfMonth);
		transaction.setTransactionType("I");
		transaction.setAmount(amt);
		transaction.setTotalAmount(totalAmt + amt);
		transaction.setTransactionId("");
		
	    return transaction;
	}
	
	
	public String getAccStartDate(final String accNo) {
		return transactionRepository.findAccStartDate(accNo);
	}
	
}
