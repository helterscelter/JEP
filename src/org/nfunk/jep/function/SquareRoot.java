/*****************************************************************************

JEP - Java Expression Parser
    JEP is a Java package for parsing and evaluating mathematical 
	expressions. It currently supports user defined variables, 
	constant, and functions. A number of common mathematical 
	functions and constants are included.

Author: Nathan Funk
Copyright (C) 2000 Nathan Funk

    JEP is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    JEP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JEP; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*****************************************************************************/
package org.nfunk.jep.function;

import java.lang.Math;
import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

public class SquareRoot extends PostfixMathCommand
{
	public SquareRoot() {
		numberOfParameters = 1;
	}
	
	/**
	 * Applies the function to the parameters on the stack.
	 */
	public void run(Stack inStack) throws ParseException {
			
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(sqrt(param));//push the result on the inStack
		return;
	}

	/**
	 * Calculates the square root of the parameter. The parameter must
	 * either be of type Double or Complex.
	 *
	 * @return The square root of the parameter.
	 */
	public Object sqrt(Object param) throws ParseException {
		
		if (param instanceof Number) {
			double value = ((Number)param).doubleValue();
			
			// a value less than 0 will produce a complex result
			if (value < 0) {
				return (new Complex(value).sqrt());
			} else {
				return new Double(Math.sqrt(value));
			}
		} else if (param instanceof Complex) {
			return ((Complex)param).sqrt();
		}

		throw new ParseException("Invalid parameter type");
	}
}
