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

/**
 * This class serves mainly as an example of a function that accepts any
 * number of parameters. Note that the numberOfParameters is initialized to 
 * -1.
 */
public class Sum extends PostfixMathCommand
{
	/**
	 * Constructor.
	 */
	public Sum() {
		// Use a variable number of arguments
		numberOfParameters = -1;
	}
	
	/**
	 * Calculates the result of summing up all parameters, which are assumed
	 * to be of the Double type.
	 */
	public void run(Stack stack) throws ParseException {
		
  		// Check if stack is null
  		if (null == stack) {
			throw new ParseException("Stack argument null");
		}
        
        Object param = null;
        double result = 0;
        int i = 0;
        
        // repeat summation for each one of the current parameters
        while (i < curNumberOfParameters) {
        	// get the parameter from the stack
            param = stack.pop();
            if (param instanceof Number)   {
                // calculate the result
                result += ((Number) param).doubleValue();
            } else {
                throw new ParseException("Invalid parameter type");
            }
                
            i++;
        }
        
        // push the result on the inStack
        stack.push(new Double(result));
	}
}
