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

public class Logarithm extends PostfixMathCommand
{
	public Logarithm() {
		numberOfParameters = 1;
	}

	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(log(param));//push the result on the inStack
		return;
	}
	

	public Object log(Object param) throws ParseException {

		if (param instanceof Number) {
				
			Complex temp = new Complex(((Number)param).doubleValue());
			Complex temp2 = new Complex(Math.log(10), 0);
			return temp.log().div(temp2);
			
		} else if (param instanceof Complex) {
				
			Complex temp = new Complex(Math.log(10), 0);
			return ((Complex)param).log().div(temp);
		}

		throw new ParseException("Invalid parameter type");
	}
	

}
