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

public class ArcCosineH extends PostfixMathCommand
{
	public ArcCosineH()
	{
		numberOfParameters = 1;
	}

	public void run(Stack inStack)
		throws ParseException
	{
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(acosh(param));//push the result on the inStack
		return;
	}

	public Object acosh(Object param)
		throws ParseException
	{
		if (param instanceof Complex)
		{
			return ((Complex)param).acosh();
		}
		else if (param instanceof Number)
		{
			Complex temp = new Complex(((Number)param).doubleValue(),0.0);
			
			return temp.acosh();
		}

		throw new ParseException("Invalid parameter type");
	}
}
