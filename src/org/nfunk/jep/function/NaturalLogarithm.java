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

/**
 * Natural logarithm.
 *
 * RJM Change: fixed so ln(positive Double) is Double.
 */
public class NaturalLogarithm extends PostfixMathCommand
{
	public NaturalLogarithm()
	{
		numberOfParameters = 1;

	}

	public void run(Stack inStack)
		throws ParseException
	{
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(ln(param));//push the result on the inStack
		return;
	}

	public Object ln(Object param)
		throws ParseException
	{
		if (param instanceof Complex)
		{
			return ((Complex)param).log();
		}
		else if (param instanceof Number)
		{
			// TODODONE: think about only returning Complex if param is <0
			double num = ((Number) param).doubleValue();
			if( num > 0)
				return new Double(Math.log(num));
			else
			{	
				Complex temp = new Complex(num);
				return temp.log();
			}
		}

		throw new ParseException("Invalid parameter type");
	}
}
