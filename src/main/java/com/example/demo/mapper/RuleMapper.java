package com.example.demo.mapper;

import org.apache.commons.lang3.StringUtils;

import com.example.demo.dao.Rule;
import com.example.demo.dto.RuleDto;

import static com.example.demo.constant.Constants.PIPE;

public class RuleMapper {

	/**
	 * Convert Input string to RuleDto Object
	 * @param input
	 * @return
	 */
	public static RuleDto convertInputToRuleDto(final String input) {
		String[] inputArr = StringUtils.split(input, PIPE);
		
		RuleDto ruleDto = new RuleDto();
		ruleDto.setDate(inputArr[0]);
		ruleDto.setRuleId(inputArr[1]);
		ruleDto.setRate(Double.valueOf(inputArr[2]));
		
		return ruleDto;
	}
	
	
	/**
	 * Convert RuleDto to Rule Object
	 * @param ruleDto
	 * @return
	 */
	public static Rule convertToRule(RuleDto ruleDto) {
		Rule rule = new Rule();
		rule.setRuleId(ruleDto.getRuleId());
		rule.setRate(ruleDto.getRate());
		rule.setDate(ruleDto.getDate());

		return rule;
	}
	
}
