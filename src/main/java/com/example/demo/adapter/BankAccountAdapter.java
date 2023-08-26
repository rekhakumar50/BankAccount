package com.example.demo.adapter;

import java.text.ParseException;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.service.StatementService;
import com.example.demo.service.RuleService;
import com.example.demo.service.TransactionService;
import com.example.demo.util.Utility;

@Component
public class BankAccountAdapter implements CommandLineRunner {
	
	@Autowired
	private StatementService statementService;
		
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private RuleService ruleService;
	

	@Override
    public void run(String... args) throws Exception {
		System.out.print("Welcome to AwesomeGIC Bank! What would you like to do?\r\n");
        this.processOptions();
     }
	
	
	private void processOptions() throws ParseException {
		String line = this.getOptions();
        switch(line.toLowerCase()) {
	        case "i":
				this.inputTransaction();
	            break;
	        case "d":
	        	this.interestRules();
	        	break;
	        case "p":
	        	this.printStatement();
	        	break;
	        case "q":
	        	System.out.print("Thank you for banking with AwesomeGIC Bank.\r\n"
	        			+ "Have a nice day!");
	        	System.exit(1);
	        	break;
	        default:	
	        	System.out.print("Please enter valid input\r\n"
	        			+ ">");
	        	this.processOptions();
	        	break;
        }
	}
	
	
	private String getOptions() {
		System.out.print("[I]nput transactions \r\n"
        		+ "[D]efine interest rules\r\n"
        		+ "[P]rint statement\r\n"
        		+ "[Q]uit\r\n"
        		+ ">");
        return new Scanner(System.in).nextLine();
	}
	
	
	private void printStatement() throws ParseException  {
		System.out.print("Please enter account and month to generate the statement <Account>|<Month>\r\n"
    			+ "(or enter blank to go back to main menu):\r\n"
    			+ ">");
    	String input = new Scanner(System.in).nextLine();
    	this.validateMonth(input);
    	statementService.processStatement(input);
        System.out.println("Is there anything else you'd like to do?");
    	this.processOptions();
	}
	
	
	private void validateMonth(final String input) throws ParseException {
		String[] inputArr = StringUtils.split(input, "|");
		
		if(!Utility.validateMonth(inputArr[1])) {
			System.out.println("Enter valid month");
			this.printStatement();
		}
	}
	
	
	private void inputTransaction() throws ParseException {
		System.out.print("Please enter transaction details in <Date>|<Account>|<Type>|<Amount> format \r\n"
    			+ "(or enter blank to go back to main menu):\r\n"
    			+ ">");
    	String input = new Scanner(System.in).nextLine();
    	this.validateInputTransaction(input);
    	transactionService.processTransaction(input);
        System.out.println("Is there anything else you'd like to do?");
        this.processOptions();
	}
	
	
	private void validateInputTransaction(final String input) throws ParseException {
		String[] inputArr = StringUtils.split(input, "|");
		
		if(!Utility.validateDate(inputArr[0])) {
			System.out.println("Date should be in YYYYMMdd format");
		} 
		
		if(!Utility.validateAccType(inputArr[2])) {
			System.out.println("Type can be either D or W. D for deposit, W for withdrawal");
		}
		
		if(!Utility.validateAmount(inputArr[3])) {
			System.out.println("Enter valid amount");
		}
		
		if(!(Utility.validateDate(inputArr[0]) && Utility.validateAccType(inputArr[2]) && Utility.validateAmount(inputArr[3]))) {
			this.inputTransaction();
		}
	}

	
	private void interestRules() throws ParseException {
		System.out.print("Please enter interest rules details in <Date>|<RuleId>|<Rate in %> format \r\n"
				+ "(or enter blank to go back to main menu):\r\n"
				+ ">");
    	String input = new Scanner(System.in).nextLine();
    	this.validateInterestRules(input);
    	ruleService.processRules(input);
        System.out.println("Is there anything else you'd like to do?");
        this.processOptions();
	}
	
	
	private void validateInterestRules(final String input) throws ParseException {
		String[] inputArr = StringUtils.split(input, "|");
		
		if(!Utility.validateDate(inputArr[0])) {
			System.out.println("Date should be in YYYYMMdd format");
		} 
		
		if(!Utility.validateInterestRate(inputArr[2])) {
			System.out.println("Interest rate should be greater than 0 and less than 100");
		}
		
		if(!(Utility.validateDate(inputArr[0]) && Utility.validateInterestRate(inputArr[2]))) {
			this.interestRules();
		}
	}
	
}