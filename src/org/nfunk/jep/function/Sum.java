/*****************************************************************************

@header@
@date@
@copyright@
@license@

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
