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
* Encapsulates the Math.random() function.
*/
public class Random extends PostfixMathCommand
{
	public Random()
	{
		numberOfParameters = 0;

	}

	public void run(Stack inStack)
		throws ParseException
	{
		checkStack(inStack);// check the stack
		inStack.push(new Double(Math.random()));
		return;
	}
}
