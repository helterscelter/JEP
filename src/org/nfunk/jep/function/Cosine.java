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

public class Cosine extends PostfixMathCommand
{
	public Cosine()
	{
		numberOfParameters = 1;
	}
	
	public void run(Stack inStack)
		throws ParseException 
	{
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(cos(param));//push the result on the inStack
		return;
	}
	
	public Object cos(Object param)
		throws ParseException
	{
		if (param instanceof Number)
		{
			return new Double(Math.cos(((Number)param).doubleValue()));
		}
		else if (param instanceof Complex)
		{
			return ((Complex)param).cos();
		}

		throw new ParseException("Invalid parameter type");
	}

}
