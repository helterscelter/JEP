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

public class CosineH extends PostfixMathCommand
{
	public CosineH()
	{
		numberOfParameters = 1;
	}

	public void run(Stack inStack)
		throws ParseException
	{
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(cosh(param));//push the result on the inStack
		return;
	}

	public Object cosh(Object param)
		throws ParseException
	{
		if (param instanceof Complex)
		{
			return ((Complex)param).cosh();
		}
		else if (param instanceof Number)
		{
			double value = ((Number)param).doubleValue();
			return new Double((Math.exp(value) + Math.exp(-value))/2);
		}

		throw new ParseException("Invalid parameter type");
	}

}
