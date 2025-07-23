package com.testcode;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.sourcecode.MathOperations;

public class MathTest {
    @Test
    public void C1_testAdd() {
		int result = MathOperations.add(5, 3);
		assertEquals(8, result);
	}
    
    @Test
    public void C2_testSubtract() {
    	int result = MathOperations.subtract(5, 3);
    	assertEquals(2, result);
    }
    
    @Test
    public void C3_testMultiply() {
		int result = MathOperations.multiply(5, 3);
		assertEquals(15, result);
	}
    
	@Test
	public void C4_testDivide() {
		int result = MathOperations.divide(6, 3);
		assertEquals(2, result);
	}
	
	@Test
	public void C5_testOneMore() {
		int result = MathOperations.divide(6, 4);
		assertEquals(1, result);
	}
	
	@Test(expectedExceptions = ArithmeticException.class)
	public void C6_testDivideByZero() {
		MathOperations.divide(5, 0);
	}
}
    
