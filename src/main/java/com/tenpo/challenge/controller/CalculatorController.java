package com.tenpo.challenge.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tenpo.challenge.annotation.TransactionRecorder;
import com.tenpo.challenge.dto.ResultDTO;
import com.tenpo.challenge.service.CalculatorService;

/**
 * Rest calculator controller
 *
 * @author Fermin Recalt
 */

@RestController
public class CalculatorController {
	
	private static final String ADD = "ADD";
	private static final String FIRST_OPERATOR = "first_operator";
	private static final String SECOND_OPERATOR = "second_operator";
	
	private CalculatorService service;
	
	public CalculatorController(CalculatorService service) {
		this.service = service;
	}
	
	@TransactionRecorder(transactionName = ADD)
	@GetMapping({"/add"})
	public ResponseEntity<ResultDTO> add(@RequestParam(name = FIRST_OPERATOR) BigDecimal firstOperator,
										 @RequestParam(name = SECOND_OPERATOR) BigDecimal secondOperator) {
		BigDecimal calculatorResult = this.service.add(firstOperator, secondOperator);
		
		ResultDTO resultDTO = new ResultDTO(calculatorResult);
		return ResponseEntity.ok(resultDTO);
	}
}
