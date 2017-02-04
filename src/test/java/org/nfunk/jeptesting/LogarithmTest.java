package org.nfunk.jeptesting;

import org.junit.jupiter.api.Test;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.Logarithm;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogarithmTest {
	/**
	 * Test method for 'org.nfunk.jep.function.Logarithm.run(Stack)'
	 * Tests the return value of log(NaN). This is a test for bug #1177557
	 */
	@Test
	public void testLogarithm() throws ParseException {
		Logarithm logFunction = new Logarithm();
		Stack<Double> stack = new Stack<>();
		stack.push(Double.NaN);
		logFunction.run(stack);
		assertTrue(Double.isNaN(stack.pop()));
	}

}
