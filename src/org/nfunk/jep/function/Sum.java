/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.nfunk.jep.function;

import java.util.*;
import org.nfunk.jep.*;

/**
 * This class serves mainly as an example of a function that accepts any
 * number of parameters. Note that the numberOfParameters is initialized to 
 * -1.
 */
public class Sum extends PostfixMathCommand
{
	private Add addFun = new Add();
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
        
        Object param = stack.pop();
        Number result;
		if (param instanceof Number)
			result = (Number) param;
		else 
			throw new ParseException("Invalid parameter type");
        
        // repeat summation for each one of the current parameters
        for(int i=1;i < curNumberOfParameters;++i)
        {
        	// get the parameter from the stack
            param = stack.pop();
            if (param instanceof Number)   {
                // calculate the result
                result = addFun.add((Number) param,result);
            } else {
                throw new ParseException("Invalid parameter type");
            }
                
            i++;
        }
        // push the result on the inStack
        stack.push(result);
	}
}
