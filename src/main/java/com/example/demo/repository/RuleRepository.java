package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dao.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long> {

	Optional<Rule> findByDateAndRuleId(String date, String ruleId);
	
	@Query(value = "SELECT r.rate FROM Rules r where r.date <= :date ORDER BY r.date DESC LIMIT 1", nativeQuery = true)
	Double getRateByDate(@Param("date") String date);
	
	List<Rule> findAllByOrderByDateAsc();

}
