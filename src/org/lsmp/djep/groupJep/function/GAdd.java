/* @author rich
 * Created on 05-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.function;
import org.nfunk.jep.function.*;
import org.lsmp.djep.groupJep.*;

import java.util.*;
import org.nfunk.jep.*;
/**
 * @author Rich Morris
 * Created on 05-Mar-2004
 */
public class GAdd extends PostfixMathCommand {
	private GroupI group;
	/**
	 * 
	 */
	private GAdd() {	}
	public GAdd(GroupI group)
	{
		numberOfParameters = -1;
		this.group = group;
	}

	/**
	 * Calculates the result of applying the "+" operator to the arguments from
	 * the stack and pushes it back on the stack.
	 */
	public void run(Stack stack) throws ParseException {
		checkStack(stack);// check the stack
		
		Object sum = stack.pop();
		Object param;
		int i = 1;
        
		// repeat summation for each one of the current parameters
		while (i < curNumberOfParameters) {
			// get the parameter from the stack
			param = stack.pop();
			// add it to the sum (order is important for String arguments)
			sum = add(param, sum);
			i++;
		}
		stack.push(sum);
		return;
	}

	public Object add(Object param1, Object param2) throws ParseException {
		if (param1 instanceof Number) {
			if (param2 instanceof Number) {
				return group.add((Number)param1, (Number)param2);
			}
		}
		
		throw new ParseException("Invalid parameter type");
	}
}
