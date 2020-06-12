package com.tenpo.challenge.dto;

import java.math.BigDecimal;

/**
 * Represent the result of the calculator operation
 *
 * @author Fermin Recalt
 */

public class ResultDTO {
	
	private BigDecimal result;
	
	public ResultDTO() {
	}
	
	public ResultDTO(BigDecimal result) {
		this.result = result;
	}
	
	public BigDecimal getResult() {
		return result;
	}
	
	public void setResult(BigDecimal result) {
		this.result = result;
	}
}
