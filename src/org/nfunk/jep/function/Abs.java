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

public class Abs extends PostfixMathCommand
{
	public Abs()
	{
		numberOfParameters = 1;
	}
	
	public void run(Stack inStack)
		throws ParseException 
	{
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(abs(param));//push the result on the inStack
		return;
	}


	public Object abs(Object param)
		throws ParseException
	{
		if (param instanceof Number)
		{
			return new Double(Math.abs(((Number)param).doubleValue()));
		}
		else if (param instanceof Complex)
		{
			return new Double(((Complex)param).abs());
		}

		throw new ParseException("Invalid parameter type");
	}

}
