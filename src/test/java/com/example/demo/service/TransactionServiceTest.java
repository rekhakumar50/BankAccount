package com.example.demo.service;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.demo.adapter.PrintDataAdapter;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.TransactionDto;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.TransactionRepository;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TransactionServiceTest {
	
	@InjectMocks
	private TransactionService transactionService;
	
	@Mock
	private TransactionRepository transactionRepository;
		
	@Mock
	private PrintDataAdapter printData;
	
	
	private MockedStatic<TransactionMapper> transactionMapperStatic;

	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		transactionMapperStatic = Mockito.mockStatic(TransactionMapper.class);
	}
	

	@Test
	public void testTransactionAccNoExistAndWithdraw() {
		TransactionDto dto = getTransactionDto("20230626", "AC001", "W", 100.00);
		when(transactionRepository.existsByAccNo(anyString())).thenReturn(false);

		transactionMapperStatic.when(() -> TransactionMapper.convertInputToTransactionDto(anyString())).thenReturn(dto);

		transactionService.processTransaction("20230626|AC001|W|100.00");
		verify(printData, Mockito.times(0)).printTransactionTable(Mockito.any(), Mockito.any());
		verify(printData, Mockito.times(0)).printTransactionTable(Mockito.any(), Mockito.any());
		transactionMapperStatic.close();
	}
	
	
	@Test
	public void testTransactionAccNoExistAndDeposit() {
		TransactionDto dto = getTransactionDto("20230626", "AC001", "D", 100.00);
		Transaction transaction = getTransaction(dto, dto.getAmount(), "20230626-01");
		
		when(transactionRepository.getTransactionIdByDate(anyString())).thenReturn(null);
		when(transactionRepository.existsByAccNo(anyString())).thenReturn(false);
		when(transactionRepository.findByAccNo(anyString())).thenReturn(Arrays.asList(transaction));

		transactionMapperStatic.when(() -> TransactionMapper.convertInputToTransactionDto(anyString())).thenReturn(dto);
		transactionMapperStatic.when(() -> TransactionMapper.convertToTransaction(any(), anyDouble(), anyString())).thenReturn(transaction);

		transactionService.processTransaction("20230626|AC001|D|100.00");
		verify(transactionRepository, Mockito.times(1)).save(Mockito.any());
		verify(printData, Mockito.times(1)).printTransactionTable(Mockito.any(), Mockito.any());

		transactionMapperStatic.close();
	}
	
	
	@Test
	public void testTransactionAccExistAndWithdraw() {
		TransactionDto dto = getTransactionDto("20230626", "AC001", "W", 100.00);
		Transaction transaction = getTransaction(dto, dto.getAmount(), "20230626-01");

		transactionMapperStatic.when(() -> TransactionMapper.convertInputToTransactionDto(anyString())).thenReturn(dto);
		transactionMapperStatic.when(() -> TransactionMapper.convertToTransaction(any(), anyDouble(), anyString())).thenReturn(transaction);

		when(transactionRepository.getTransactionIdByDate(anyString())).thenReturn("20230626-01");
		when(transactionRepository.existsByAccNo(anyString())).thenReturn(true);
		when(transactionRepository.getTotalAmountByDate(anyString())).thenReturn(150.00);

		
		transactionService.processTransaction("20230626|AC001|W|100.00");
		verify(transactionRepository, Mockito.times(1)).save(Mockito.any());
		verify(printData, Mockito.times(1)).printTransactionTable(Mockito.any(), Mockito.any());

		transactionMapperStatic.close();
	}
	
	
	@Test
	public void testTransactionAccExistAndWithdrawLessBalance() {
		TransactionDto dto = getTransactionDto("20230626", "AC001", "W", 100.00);
		Transaction transaction = getTransaction(dto, dto.getAmount(), "20230626-01");

		transactionMapperStatic.when(() -> TransactionMapper.convertInputToTransactionDto(anyString())).thenReturn(dto);
		transactionMapperStatic.when(() -> TransactionMapper.convertToTransaction(any(), anyDouble(), anyString())).thenReturn(transaction);

		when(transactionRepository.getTransactionIdByDate(anyString())).thenReturn("20230626-01");
		when(transactionRepository.existsByAccNo(anyString())).thenReturn(true);
		when(transactionRepository.getTotalAmountByDate(anyString())).thenReturn(90.00);
		
		transactionService.processTransaction("20230626|AC001|W|100.00");
		verify(transactionRepository, Mockito.times(0)).save(Mockito.any());
		verify(printData, Mockito.times(0)).printTransactionTable(Mockito.any(), Mockito.any());

		transactionMapperStatic.close();
	}
	
	
	@Test
	public void testTransactionAccExistAndDeposit() {
		TransactionDto dto = getTransactionDto("20230626", "AC001", "D", 100.00);
		Transaction transaction = getTransaction(dto, dto.getAmount(), "20230626-01");

		transactionMapperStatic.when(() -> TransactionMapper.convertInputToTransactionDto(anyString())).thenReturn(dto);
		transactionMapperStatic.when(() -> TransactionMapper.convertToTransaction(any(), anyDouble(), anyString())).thenReturn(transaction);

		when(transactionRepository.getTransactionIdByDate(anyString())).thenReturn("20230626-01");
		when(transactionRepository.existsByAccNo(anyString())).thenReturn(true);
		when(transactionRepository.getTotalAmountByDate(anyString())).thenReturn(90.00);
		
		transactionService.processTransaction("20230626|AC001|D|100.00");
		verify(transactionRepository, Mockito.times(1)).save(Mockito.any());
		verify(printData, Mockito.times(1)).printTransactionTable(Mockito.any(), Mockito.any());

		transactionMapperStatic.close();
	
	}
	
	
	private TransactionDto getTransactionDto(String date, String accNo, String transactionType, Double amount) {
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.setDate(date);
		transactionDto.setAccNo(accNo);
		transactionDto.setTransactionType(transactionType);
		transactionDto.setAmount(amount);
		
		return transactionDto;
	}
	
	
	private Transaction getTransaction(TransactionDto transactionDto, Double totalAmount, String transactionId) {
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
