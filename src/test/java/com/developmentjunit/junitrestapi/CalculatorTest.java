package com.developmentjunit.junitrestapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.developmentjunit.junitrestapi.web.Calculator;

public class CalculatorTest {

	Calculator calculator;
	
	@Test
	public void testMultiply() {
		calculator = new Calculator();
		assertEquals(20, calculator.multiply(4, 5));
		//assertEquals(25, calculator.multiply(5, 5));
	}
	@Test
	public void testDivide() {
		calculator = new Calculator();
		assertEquals(2, calculator.divide(4, 2));
		//assertEquals(25, calculator.multiply(5, 5));
	}
}
