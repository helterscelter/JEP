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

public class Add extends PostfixMathCommand
{
	
	public Add()
	{
		numberOfParameters = 2;
	}
	
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		
		Object param2 = inStack.pop();
		Object param1 = inStack.pop();
		
		inStack.push(add(param1, param2));		
		
		return;
	}

	public Object add(Object param1, Object param2) throws ParseException {
		if (param1 instanceof Double) {
			if (param2 instanceof Double) {
				return add((Double)param1, (Double)param2);
			} else if (param2 instanceof Complex) {
				return add((Complex)param2, (Double)param1);
			}
		} else if (param1 instanceof Complex) {
			if (param2 instanceof Double) {
				return add((Complex)param1, (Double)param2);
			} else if (param2 instanceof Complex) {
				return add((Complex)param1, (Complex)param2);
			}
		} else if ((param1 instanceof String) && (param2 instanceof String)) {
			return (String)param1 + (String)param2;
		}
		
		throw new ParseException("Invalid parameter type");
	}
	
	public Double add(Double d1, Double d2) {
		return new Double(d1.doubleValue()+d2.doubleValue());
	}
	
	public Complex add(Complex c1, Complex c2) {
		return new Complex(c1.re() + c2.re(), c1.im() + c2.im());
	}
	
	public Complex add(Complex c, Double d) {
		return new Complex(c.re() + d.doubleValue(), c.im());
	}	
}
