package org.nfunk.jeptesting;

import org.junit.jupiter.api.Test;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.NaturalLogarithm;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NaturalLogarithmTest {
	/**
	 * Test method for 'org.nfunk.jep.function.Logarithm.run(Stack)'
	 * Tests the return value of log(NaN). This is a test for bug #1177557
	 */
	@Test
	public void testNaturalLogarithm() throws ParseException {
		NaturalLogarithm logFunction = new NaturalLogarithm();
		Stack<Double> stack = new Stack<>();
		stack.push(Double.NaN);
		logFunction.run(stack);
		assertTrue(Double.isNaN(stack.pop()));
	}
}
