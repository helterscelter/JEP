/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/
package org.nfunk.jep.function;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
 
public class UMinus extends PostfixMathCommand
{
	public UMinus() {
		numberOfParameters = 1;
	}
	
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack

		Object param = inStack.pop();
		
		inStack.push(umin(param));
		return;
	}
	
	public Object umin(Object param) throws ParseException {
		if (param instanceof Complex)
			return ((Complex)param).neg();
		if (param instanceof Number)
			return new Double(-((Number)param).doubleValue());

		throw new ParseException("Invalid parameter type");
	}
}
