/* @author rich
 * Created on 10-Sep-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep.function;

import java.util.Stack;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

/**
 * @author Rich Morris
 * Created on 10-Sep-2004
 */
public class MaxArg extends SumType {

	static Comparative comp = new Comparative(Comparative.LE);

	public MaxArg() {
		super("MaxArg");
	}

	public Object evaluate(Object[] elements) throws ParseException {
		throw new ParseException("MaxArg: call to evaluate(Object[] elements) should not have happened.");
	}

	public Object evaluate(
		Node node,
		Variable var,
		double min,
		double max,
		double inc,
		Object data,
		ParserVisitor pv,
		Stack stack)
		throws ParseException {

			int i=0;
			double val;
			Object maxVal=null;
			Object maxArg=null;
			for(i=0,val=min;val<=max;++i,val=min+i*inc)
			{
				Object curArg = new Double(val); 
				var.setValue(curArg);
				
				node.jjtGetChild(0).jjtAccept(pv,data);	
				checkStack(stack); // check the stack
				Object res = stack.pop();
				if(i==0)
				{
					maxVal = res;
					maxArg = curArg;
				}
				else if(comp.gt(res,maxVal))
				{
					maxVal = res;
					maxArg = curArg;
				}
			}
			stack.push(maxArg);
			return maxArg;
	}

}
