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
/**
 * Converts a pair of real numbers to a complex number Complex(x,y)=x+i y.
 * 
 * @author Rich Morris
 * Created on 24-Mar-2004
 */
public class ComplexPFMC extends PostfixMathCommand
{
	public ComplexPFMC()
	{
		numberOfParameters = 2;
	}
	
	public void run(Stack inStack)
		throws ParseException 
	{
		checkStack(inStack);// check the stack
		Object param2 = inStack.pop();
		Object param1 = inStack.pop();
		
		if ((param1 instanceof Number) && (param2 instanceof Number))
		{
			double real = ((Number)param1).doubleValue();
			double imag = ((Number)param2).doubleValue();
		
			inStack.push(new Complex(real,imag));
		}
		else
		{
			throw new ParseException("Complex: Invalid parameter types "+param1.getClass().getName()+" "+param1.getClass().getName());
		}
		return;
	}
}
