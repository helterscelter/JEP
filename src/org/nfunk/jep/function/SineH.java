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

public class SineH extends PostfixMathCommand
{
	public SineH()
	{
		numberOfParameters = 1;
	}

	public void run(Stack inStack)
		throws ParseException
	{
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(sinh(param));//push the result on the inStack
		return;
	}

	public Object sinh(Object param)
		throws ParseException
	{
		if (param instanceof Number)
		{
			double value = ((Number)param).doubleValue();
			return new Double((Math.exp(value)-Math.exp(-value))/2);
		}
		else if (param instanceof Complex)
		{
			return ((Complex)param).sinh();
		}

		throw new ParseException("Invalid parameter type");
	}

}
