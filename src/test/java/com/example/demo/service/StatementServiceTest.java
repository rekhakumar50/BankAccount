package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.demo.adapter.PrintDataAdapter;
import com.example.demo.dao.Transaction;
import com.example.demo.repository.RuleRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.util.Utility;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class StatementServiceTest {
	
	@InjectMocks
	private StatementService statementService;
	
	@Mock
	private TransactionRepository transactionRepository;
	
	@Mock
	private RuleRepository ruleRepository;
	
	@Mock
	private PrintDataAdapter printData;
	
	
	private MockedStatic<Utility> utilityStatic;

	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		utilityStatic = Mockito.mockStatic(Utility.class);
	}
	
	
	@Test
	public void testGetAccStartDate() {
		when(transactionRepository.findAccStartDate(anyString())).thenReturn("20230505");

		statementService.getAccStartDate("20230505");
		verify(transactionRepository, Mockito.times(1)).findAccStartDate(Mockito.any());
		utilityStatic.close();
	}
	
	
	@Test
	public void testProcessStatementWithTransaction() {
		utilityStatic.when(() -> Utility.getCurrentYear()).thenReturn(2023);
		utilityStatic.when(() -> Utility.getLengthOfYear()).thenReturn(365);
		utilityStatic.when(() -> Utility.getLastDayOfMonth(anyInt())).thenReturn(30);

		when(transactionRepository.findByAccNoAndDateLike(anyString(), anyString())).thenReturn(getTransactions());
		when(transactionRepository.getTotalAmountByDate(anyString(), anyString())).thenReturn(100.00);
		when(ruleRepository.getRateByDate(anyString())).thenReturn(2.20);

		statementService.processStatement("AC001|06");
		verify(printData, Mockito.times(1)).printStatementTable(Mockito.any(), Mockito.any());
		utilityStatic.close();
	}
	
	
	@Test
	public void testProcessStatementWithOutTransaction() {
		Transaction t1 = getTransaction("20230505","20230505-01","D",100.00,100.00);
		
		utilityStatic.when(() -> Utility.getCurrentYear()).thenReturn(2023);
		utilityStatic.when(() -> Utility.getLengthOfYear()).thenReturn(365);
		utilityStatic.when(() -> Utility.getLastDayOfMonth(anyInt())).thenReturn(30);

		when(transactionRepository.findByAccNoAndDateLike(anyString(), anyString())).thenReturn(null);
		when(transactionRepository.findByAccNoAndDate(anyString(), anyString())).thenReturn(t1);
		when(transactionRepository.getTotalAmountByDate(anyString(), anyString())).thenReturn(100.00);
		when(ruleRepository.getRateByDate(anyString())).thenReturn(2.20);

		statementService.processStatement("AC001|06");
		verify(printData, Mockito.times(1)).printStatementTable(Mockito.any(), Mockito.any());
		utilityStatic.close();
	}
	
	
	private List<Transaction> getTransactions() {
		Transaction t1 = getTransaction("20230601","20230601-01","D",150.00,250.00);
		Transaction t2 = getTransaction("20230626","20230626-01","W",20.00,230.00);
		Transaction t3 = getTransaction("20230626","20230626-02","W",100.00,130.00);
		
		return new ArrayList<>(Arrays.asList(t1, t2, t3));
	}
	
	private Transaction getTransaction(String Date, String transactionId, String transactionType, Double amount, Double totalAmount) {
		Transaction transaction = new Transaction();
		transaction.setDate(Date);
		transaction.setTransactionId(transactionId);
		transaction.setTransactionType(transactionType);
		transaction.setAmount(amount);
		transaction.setTotalAmount(totalAmount);
		
		return transaction;
	}
	
}
