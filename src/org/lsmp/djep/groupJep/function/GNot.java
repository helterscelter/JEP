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

public class GNot extends PostfixMathCommand
{
	public GNot()
	{
		numberOfParameters = 2;
	}
	
	public void run(Stack inStack)
		throws ParseException 
	{
		checkStack(inStack);// check the stack
		
		Object param = inStack.pop();

		if (param instanceof Boolean)
		{
			boolean a = ((Boolean)param).booleanValue();
			inStack.push(a ? Boolean.FALSE : Boolean.TRUE);//push the result on the inStack
		}
		else
		{
			throw new ParseException("Invalid parameter type");
		}
		return;
	}
}
