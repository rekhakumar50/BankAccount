package com.example.demo.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dao.Rule;
import com.example.demo.dao.Transaction;

@Component
public class PrintDataAdapter {
	
	
	/**
	 * Print Transactions in table format
	 * 
	 * @param accNo
	 * @param transactions
	 */
	public void printTransactionTable(final String accNo, final List<Transaction> transactions) {
		System.out.println("Account: " + accNo);
		System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("%10s %30s %10s %30s", "Date", "Txn Id", "Type", "Amount");
        System.out.println();
        System.out.println("------------------------------------------------------------------------------------------");
        for(Transaction transaction: transactions){
            System.out.format("%10s %30s %10s %30s",
            		transaction.getDate(), transaction.getTransactionId(), transaction.getTransactionType(), transaction.getAmount());
            System.out.println();
        }
        System.out.println("------------------------------------------------------------------------------------------");
	}
	
	
	/**
	 * Print Rules in table format
	 * 
	 * @param rules
	 */
	public void printRuleTable(final List<Rule> rules) {
		System.out.println("Interest rules:");
		System.out.println("---------------------------------------------------");
        System.out.printf("%10s %20s %10s", "Date", "RuleId", "Rate (%)");
        System.out.println();
        System.out.println("---------------------------------------------------");
        for(Rule rule: rules){
            System.out.format("%10s %20s %10s",	rule.getDate(), rule.getRuleId(), rule.getRate());
            System.out.println();
        }
        System.out.println("---------------------------------------------------");
	}
		
	
	/**
	 * Print Statements in table format
	 * 
	 * @param accNo
	 * @param transactions
	 */
	public void printStatementTable(final String accNo, final List<Transaction> transactions) {
		System.out.println("Account: " + accNo);
		System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.printf("%10s %30s %10s %20s %20s", "Date", "Txn Id", "Type", "Amount", "Balance");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------------");
        for(Transaction transaction: transactions){
            System.out.format("%10s %30s %10s %20s %20s",
            		transaction.getDate(), 
            		transaction.getTransactionId(), 
            		transaction.getTransactionType(), 
            		transaction.getAmount(), 
            		transaction.getTotalAmount());
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------------------------------");
	}

}
