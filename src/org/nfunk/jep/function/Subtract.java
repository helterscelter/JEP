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

public class Subtract extends PostfixMathCommand
{
	public Subtract()
	{
		numberOfParameters = 2;
	}
	
	public void run(Stack inStack)
		throws ParseException 
	{
		checkStack(inStack); // check the stack
		
		Object param2 = inStack.pop();
		Object param1 = inStack.pop();
		
		inStack.push(sub(param1, param2));

		return;
	}
	
	public Object sub(Object param1, Object param2)
		throws ParseException
	{
		if (param1 instanceof Number)
		{
			if (param2 instanceof Number)
			{
				return sub((Number)param1, (Number)param2);
			}
			else if (param2 instanceof Complex)
			{
				return sub((Number)param1, (Complex)param2);
			}
		} else if (param1 instanceof Complex)
		{
			if (param2 instanceof Number)
			{
				return sub((Complex)param1, (Number)param2);
			}
			else if (param2 instanceof Complex)
			{
				return sub((Complex)param1, (Complex)param2);
			}
		}

		throw new ParseException("Invalid parameter type");
	}
	

	public Double sub(Number d1, Number d2)
	{
		return new Double(d1.doubleValue() - d2.doubleValue());
	}
	
	public Complex sub(Complex c1, Complex c2)
	{
		return new Complex(c1.re() - c2.re(), c1.im() - c2.im());
	}
	
	public Complex sub(Complex c, Number d)
	{
		return new Complex(c.re() - d.doubleValue(), c.im());
	}

	public Complex sub(Number d, Complex c)
	{
		return new Complex(d.doubleValue() - c.re(), -c.im());
	}
}
