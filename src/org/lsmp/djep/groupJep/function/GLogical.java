/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.lsmp.djep.groupJep.function;
import org.nfunk.jep.function.*;

import java.util.*;
import org.nfunk.jep.*;

public class GLogical extends PostfixMathCommand
{
	int id;
	
	public GLogical(int id_in)
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

		if ((param1 instanceof Boolean) && (param2 instanceof Boolean))
		{
			boolean a = ((Boolean)param1).booleanValue();
			boolean b = ((Boolean)param2).booleanValue();
			boolean flag=false;
			
			switch (id)
			{
				case Logical.AND:
					flag = a && b;
					break;
				case Logical.OR:
					flag = a || b;
					break;
				default:
					throw new ParseException("Illegal logical operator");
			}
			inStack.push(flag ? Boolean.TRUE : Boolean.FALSE);//push the result on the inStack
		}
		else
		{
			throw new ParseException("Invalid parameter type");
		}
		return;
	}
}
