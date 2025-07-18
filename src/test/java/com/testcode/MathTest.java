package com.testcode;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.sourcecode.MathOperations;

public class MathTest {
    @Test
    public void testAdd() {
		int result = MathOperations.add(5, 3);
		assertEquals(8, result);
	}
    
    @Test
    public void testSubtract() {
    	int result = MathOperations.subtract(5, 3);
    	assertEquals(2, result);
    }
    
    @Test
    public void testMultiply() {
		int result = MathOperations.multiply(5, 3);
		assertEquals(15, result);
	}
    
	@Test
	public void testDivide() {
		int result = MathOperations.divide(6, 3);
		assertEquals(2, result);
	}
	
	@Test(expectedExceptions = ArithmeticException.class)
	public void testDivideByZero() {
		MathOperations.divide(5, 0);
	}
}
    
