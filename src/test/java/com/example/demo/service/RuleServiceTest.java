package com.example.demo.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.example.demo.adapter.PrintDataAdapter;
import com.example.demo.dao.Rule;
import com.example.demo.dto.RuleDto;
import com.example.demo.mapper.RuleMapper;
import com.example.demo.repository.RuleRepository;

public class RuleServiceTest {
	
	@InjectMocks
	private RuleService ruleService;
	
	@Mock
	private RuleRepository ruleRepository;
	
	@Mock
	private PrintDataAdapter printData;
	
	private MockedStatic<RuleMapper> ruleMapperStatic;

	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		ruleMapperStatic = Mockito.mockStatic(RuleMapper.class);
	}
	
	
	@Test
	public void testProcessExistingRules() {
		ruleMapperStatic.when(() -> RuleMapper.convertInputToRuleDto(anyString())).thenReturn(getRuleDto());

		when(ruleRepository.findByDateAndRuleId(anyString(), anyString())).thenReturn(Optional.of(getRule()));
		when(ruleRepository.findAll()).thenReturn(null);

		ruleService.processRules("20230615|RULE03|2.20");
		verify(ruleRepository, Mockito.times(1)).save(Mockito.any());
		verify(printData, Mockito.times(1)).printRuleTable(Mockito.any());

		ruleMapperStatic.close();
	}
	
	
	@Test
	public void testProcessNewRules() {
		ruleMapperStatic.when(() -> RuleMapper.convertInputToRuleDto(anyString())).thenReturn(getRuleDto());
		ruleMapperStatic.when(() -> RuleMapper.convertToRule(any())).thenReturn(getRule());

		when(ruleRepository.findByDateAndRuleId(anyString(), anyString())).thenReturn(Optional.empty());
		when(ruleRepository.findAll()).thenReturn(null);

		ruleService.processRules("20230615|RULE03|2.20");
		verify(ruleRepository, Mockito.times(1)).save(Mockito.any());
		verify(printData, Mockito.times(1)).printRuleTable(Mockito.any());

		ruleMapperStatic.close();
	}
	
	
	private RuleDto getRuleDto() {
		RuleDto ruleDto = new RuleDto();
		ruleDto.setDate("20230615");
		ruleDto.setRate(2.20);
		ruleDto.setRuleId("RULE03");
		
		return ruleDto;
	}
	
	
	private Rule getRule() {
		Rule rule = new Rule();
		rule.setDate("20230615");
		rule.setRate(2.50);
		rule.setRuleId("RULE03");
		
		return rule;
	}

}
