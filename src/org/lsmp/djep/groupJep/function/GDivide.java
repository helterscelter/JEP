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
import org.lsmp.djep.groupJep.interfaces.*;

import java.util.*;
import org.nfunk.jep.*;
/**
 * @author Rich Morris
 * Created on 05-Mar-2004
 */
public class GDivide extends PostfixMathCommand {
	private HasDivI group=null;
	/**
	 * 
	 */
	private GDivide() {	}
	public GDivide(GroupI group)
	{
		numberOfParameters = 2;
		if(group instanceof HasDivI)
			this.group = (HasDivI) group;
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
		sum = div(param, sum);
		stack.push(sum);
		return;
	}

	public Object div(Object param1, Object param2) throws ParseException {
		if(group==null) throw new ParseException("Divide not implemented for this group.");
		if (param1 instanceof Number) {
			if (param2 instanceof Number) {
				return group.div((Number)param1, (Number)param2);
			}
		}
		
		throw new ParseException("Invalid parameter type");
	}
}
