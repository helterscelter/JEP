/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.nfunk.jep.function;

import java.util.*;
import org.nfunk.jep.*;

public class Logical extends PostfixMathCommand
{
	int id;
	public static final int AND = 0;
	public static final int OR = 1;
	public Logical(int id_in)
	{
		id = id_in;
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
			double x = ((Number)param1).doubleValue();
			double y = ((Number)param2).doubleValue();
			int r;
			
			switch (id)
			{
				case 0:
					// AND
					r = ((x!=0d) && (y!=0d)) ? 1 : 0;
					break;
				case 1:
					// OR
					r = ((x!=0d) || (y!=0d)) ? 1 : 0;
					break;
				default:
					r = 0;
			}
			
			inStack.push(new Double(r)); // push the result on the inStack
		}
		else
		{
			throw new ParseException("Invalid parameter type");
		}
		return;
	}
}
