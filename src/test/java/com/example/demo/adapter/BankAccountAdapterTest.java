package com.example.demo.adapter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.demo.service.RuleService;
import com.example.demo.service.StatementService;
import com.example.demo.service.TransactionService;

public class BankAccountAdapterTest {
	
	@InjectMocks
	private BankAccountAdapter bankAccountAdapter;
	
	@Mock
	private StatementService statementService;
		
	@Mock
	private TransactionService transactionService;
	
	@Mock
	private RuleService ruleService;
	
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	
	@Test
    public void run() throws Exception {
		String[] args = {};
		when(statementService.getAccStartDate(anyString())).thenReturn("20230604");

		bankAccountAdapter.run(args);
    	
		verify(statementService, Mockito.times(1)).processStatement(Mockito.any());
		verify(transactionService, Mockito.times(1)).processTransaction(Mockito.any());
		verify(ruleService, Mockito.times(1)).processRules(Mockito.any());

    }
	

}
