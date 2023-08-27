package com.example.demo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.adapter.PrintDataAdapter;
import com.example.demo.dao.Rule;
import com.example.demo.dto.RuleDto;
import com.example.demo.mapper.RuleMapper;
import com.example.demo.repository.RuleRepository;

@Service
public class RuleService {
	
	@Autowired
	private RuleRepository ruleRepository;
	
	@Autowired
	private PrintDataAdapter printData;
	
	
	/**
	 * Input new Rule and update existing Rule
	 * @param input
	 */
	public void processRules(final String input) {
		Rule rule = null;
		RuleDto ruleDto = RuleMapper.convertInputToRuleDto(input);
		Optional<Rule> ruleOp = ruleRepository.findByDateAndRuleId(ruleDto.getDate(), ruleDto.getRuleId());
		
		if(ruleOp.isPresent()) {
			rule = ruleOp.get();
			rule.setRate(ruleDto.getRate());
		}  else  {
			rule = RuleMapper.convertToRule(ruleDto);
		}
		
		if(Objects.nonNull(rule)) {
			ruleRepository.save(rule);
			List<Rule> rules = ruleRepository.findAll();
			printData.printRuleTable(rules);
		}
	}

}
