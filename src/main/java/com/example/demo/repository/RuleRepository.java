package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dao.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long> {

	Optional<Rule> findByDateAndRuleId(String date, String ruleId);

}
