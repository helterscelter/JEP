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
public class GSubtract extends PostfixMathCommand {
	private GroupI group;
	/**
	 * 
	 */
	private GSubtract() {	}
	public GSubtract(GroupI group)
	{
		numberOfParameters = 2;
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
		param = stack.pop();
		sum = sub(param, sum);
		stack.push(sum);
		return;
	}

	public Object sub(Object param1, Object param2) throws ParseException {
		if (param1 instanceof Number) {
			if (param2 instanceof Number) {
				return group.sub((Number)param1, (Number)param2);
			}
		}
		
		throw new ParseException("Invalid parameter type");
	}
}
