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

public class ArcCosine extends PostfixMathCommand
{
	public ArcCosine()
	{
		numberOfParameters = 1;
	
	}
	
	public void run(Stack inStack)
		throws ParseException 
	{
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(acos(param));//push the result on the inStack
		return;
	}

	public Object acos(Object param)
		throws ParseException
	{
		if (param instanceof Complex)
		{
			return ((Complex)param).acos();
		}
		else if (param instanceof Number)
		{
			return new Double(Math.acos(((Number)param).doubleValue()));
		}

		throw new ParseException("Invalid parameter type");
	}
	
}
