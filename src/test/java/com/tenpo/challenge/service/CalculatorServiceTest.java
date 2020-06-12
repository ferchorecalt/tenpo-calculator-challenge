package com.tenpo.challenge.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculatorServiceTest {
	
	@Autowired
	private CalculatorService service;
	
	@Test
	public void testAdd_Succeed() {
		assertTrue(BigDecimal.valueOf(4.0).equals(service.add(BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0))));
		assertTrue(BigDecimal.valueOf(8.0).equals(service.add(BigDecimal.valueOf(6.0), BigDecimal.valueOf(2.0))));
		assertTrue(BigDecimal.valueOf(2.0).equals(service.add(BigDecimal.valueOf(4.0), BigDecimal.valueOf(-2.0))));
	}
}
