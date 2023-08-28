package com.example.demo.adapter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.service.StatementService;
import com.example.demo.service.RuleService;
import com.example.demo.service.TransactionService;
import com.example.demo.util.Utility;

import static com.example.demo.constant.Constants.*;

@Component
public class BankAccountAdapter implements CommandLineRunner {
	
	private static Scanner stdin = new Scanner(System.in);
	
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
	
	
	/**
	 * Processing function's options
	 * @throws ParseException
	 */
	private void processOptions() throws ParseException {
		String line = this.getOptions();
        switch(line.toLowerCase()) {
	        case I:
				this.inputTransaction();
	            break;
	        case D:
	        	this.interestRules();
	        	break;
	        case P:
	        	this.printStatement();
	        	break;
	        case Q:
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
	
	
	/**
	 * Processing printStatement by getting input, validating and processing it
	 * @throws ParseException
	 */
	private void printStatement() throws ParseException  {
		System.out.print("Please enter account and month to generate the statement <Account>|<Month>\r\n"
    			+ "(or enter blank to go back to main menu):\r\n"
    			+ ">");
    	String input = stdin.nextLine();
    	if(StringUtils.isBlank(input))  {
        	this.processOptions();
    	}
    	this.validatePrintStatement(input);
    	statementService.processStatement(input);
        System.out.println("Is there anything else you'd like to do?");
    	this.processOptions();
	}
	
	
	/**
	 * Validating printStatement input
	 * 
	 * @param input
	 * @throws ParseException
	 */
	private void validatePrintStatement(final String input) throws ParseException {
		if(StringUtils.containsNone(input, PIPE)) {
			System.out.println("Enter Valid Input");
			this.printStatement();
		}
		
		String[] inputArr = StringUtils.split(input, PIPE);
		if(inputArr.length != 2) {
			System.out.println("Enter Valid Input");
			this.printStatement();
		}
		String accNo = inputArr[0];
		String month = inputArr[1];
		
		if(!Utility.validateMonth(month)) {
			System.out.println("Enter Valid Month");
			this.printStatement();
		} else if(Integer.valueOf(month) > Calendar.getInstance().get(Calendar.MONTH) + 1) {
			System.out.println("Month should not be greater than current month. Enter valid month.");
			this.printStatement();
		} else {
			String accStartDate = statementService.getAccStartDate(accNo);
			if(StringUtils.isNotEmpty(accStartDate)) {
				int accStartMonth = Integer.valueOf(StringUtils.substring(accStartDate, 4, 6));
				if(accStartMonth > Integer.valueOf(month)) {
					System.out.println("Account opened on " + accStartMonth + "th month. Please enter a valid month.");
					this.printStatement();
				}
			} else {
				System.out.println("Enter Valid Account Number");
				this.printStatement();
			}
		}
	}
	
	
	/**
	 * Processing inputTransaction by getting input, validating and processing it
	 * @throws ParseException
	 */
	private void inputTransaction() throws ParseException {
		System.out.print("Please enter transaction details in <Date>|<Account>|<Type>|<Amount> format \r\n"
    			+ "(or enter blank to go back to main menu):\r\n"
    			+ ">");
    	String input = stdin.nextLine();
    	if(StringUtils.isBlank(input))  {
        	this.processOptions();
    	}
    	this.validateInputTransaction(input);
    	transactionService.processTransaction(input);
        System.out.println("Is there anything else you'd like to do?");
        this.processOptions();
	}
	
	
	/**
	 * Validating transaction input
	 * 
	 * @param input
	 * @throws ParseException
	 */
	private void validateInputTransaction(final String input) throws ParseException {
		if(StringUtils.containsNone(input, PIPE)) {
			System.out.println("Enter Valid Input");
			this.inputTransaction();
		}
		
		String[] inputArr = StringUtils.split(input, PIPE);
		if(inputArr.length != 4) {
			System.out.println("Enter Valid Input");
			this.inputTransaction();
		}
		
		if(!Utility.validateDate(inputArr[0])) {
			System.out.println("Date should be in YYYYMMdd format");
		} 
		
		if(!Utility.validateAccType(inputArr[2])) {
			System.out.println("Type can be either D or W. D for deposit, W for withdrawal");
		}
		
		if(!Utility.validateAmount(inputArr[3])) {
			System.out.println("Enter Valid Amount");
		}
		
		if(!(Utility.validateDate(inputArr[0]) && Utility.validateAccType(inputArr[2]) && Utility.validateAmount(inputArr[3]))) {
			this.inputTransaction();
		}
		
		String accNo = inputArr[1];
		boolean isAccNoExist = transactionService.isAccExist(accNo);
		if(isAccNoExist) {
			String accStartDate = transactionService.getAccStartDate(accNo);
			if(!Utility.isDateBefore(accStartDate, inputArr[0])) {
				System.out.println("Account started on " + accStartDate + ". Please enter valid transaction date");
				this.inputTransaction();
			}
		}

	}

	
	/**
	 * Processing interestRules by getting input, validating and processing it
	 * @throws ParseException
	 */
	private void interestRules() throws ParseException {
		System.out.print("Please enter interest rules details in <Date>|<RuleId>|<Rate in %> format \r\n"
				+ "(or enter blank to go back to main menu):\r\n"
				+ ">");
    	String input = stdin.nextLine();
    	if(StringUtils.isBlank(input))  {
        	this.processOptions();
    	}
    	this.validateInterestRules(input);
    	ruleService.processRules(input);
        System.out.println("Is there anything else you'd like to do?");
        this.processOptions();
	}
	
	
	/**
	 * Validating interest Rules input
	 * 
	 * @param input
	 * @throws ParseException
	 */
	private void validateInterestRules(final String input) throws ParseException {
		if(StringUtils.containsNone(input, PIPE)) {
			System.out.println("Enter Valid Input");
			this.inputTransaction();
		}
		
		String[] inputArr = StringUtils.split(input, PIPE);
		if(inputArr.length != 3) {
			System.out.println("Enter Valid Input");
			this.inputTransaction();
		}
		
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
	
	
	/**
	 * Printing function's options
	 * @return
	 */
	private String getOptions() {
		System.out.print("[I]nput transactions \r\n"
        		+ "[D]efine interest rules\r\n"
        		+ "[P]rint statement\r\n"
        		+ "[Q]uit\r\n"
        		+ ">");
        return stdin.nextLine();
	}
	
}