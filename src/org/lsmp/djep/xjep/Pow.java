/*****************************************************************************

JEP - Java Math Expression Parser 2.24
	  December 30 2002
	  (c) Copyright 2002, Nathan Funk
	  See LICENSE.txt for license information.

*****************************************************************************/
package org.lsmp.djep.xjep;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

/**
 * The pow(x,y) = x^y function.
 * Will come in handy when we overload the ^ to do 
 * cross or exterior product.
 * @author Rich Morris
 * Created on 20-Jun-2003
 */
public class Pow extends PostfixMathCommand
{
	Power powerPfmc = new Power();
	
	public Pow()
	{
		numberOfParameters = 2;
	}
	
	public void run(Stack inStack)
		throws ParseException 
	{
		checkStack(inStack); // check the stack
		
		Object param2 = inStack.pop();
		Object param1 = inStack.pop();
		
		inStack.push(powerPfmc.power(param1, param2));
	}
}
