package com.tenpo.challenge.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

/**
 * Service in charge of perform calculator operations
 *
 * @author Fermin Recalt
 */

@Service
public class CalculatorService {
	
	public BigDecimal add(BigDecimal firstOperator, BigDecimal secondOperator) {
		return firstOperator.add(secondOperator);
	}
}
