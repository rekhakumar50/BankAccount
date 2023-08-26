package com.example.demo.mapper;

import org.apache.commons.lang3.StringUtils;

import com.example.demo.dao.Rule;
import com.example.demo.dto.RuleDto;

public class RuleMapper {

	public static RuleDto convertInputToRuleDto(final String input) {
		String[] inputArr = StringUtils.split(input, "|");
		
		RuleDto ruleDto = new RuleDto();
		ruleDto.setDate(inputArr[0]);
		ruleDto.setRuleId(inputArr[1]);
		ruleDto.setRate(Double.valueOf(inputArr[2]));
		
		return ruleDto;
	}
	
	public static Rule convertToRule(RuleDto ruleDto) {
		Rule rule = new Rule();
		rule.setRuleId(ruleDto.getRuleId());
		rule.setRate(ruleDto.getRate());
		rule.setDate(ruleDto.getDate());

		return rule;
	}
	
}
