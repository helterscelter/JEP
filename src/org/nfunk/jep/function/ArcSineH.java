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

public class ArcSineH extends PostfixMathCommand
{
	public ArcSineH()
	{
		numberOfParameters = 1;
	}

	public void run(Stack inStack)
		throws ParseException
	{
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(asinh(param));//push the result on the inStack
		return;
	}

	public Object asinh(Object param)
		throws ParseException
	{
		if (param instanceof Complex)
		{
			return ((Complex)param).asinh();
		}
		else if (param instanceof Number)
		{
			Complex temp = new Complex(((Number)param).doubleValue(),0.0);
			
			return temp.asinh();
		}
		throw new ParseException("Invalid parameter type");
	}
}
