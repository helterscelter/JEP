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
public class GList extends PostfixMathCommand {
	private GroupI group;
	/**
	 * 
	 */
	private GList() {	}
	public GList(GroupI group)
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
		if(!(group instanceof HasListI))
			throw new ParseException("List not defined for this group");
		Number res[] = new Number[curNumberOfParameters]; 
		// repeat summation for each one of the current parameters
		for(int i=curNumberOfParameters-1;i>=0;--i) {
			res[i] = (Number) stack.pop();
		}
		stack.push(((HasListI) group).list(res));
		return;
	}
}
