/* @author rich
 * Created on 18-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep.function;

import java.util.*;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

/**
 * A sum function Sum(x^2,x,1,10) finds the sum of x^2 with x running from 1 to 10.
 * Sum(x^2,x,1,10,2) calculates the 1^2+3^2+5^2+7^2+9^2 i.e. in steps of 2.
 * @author Rich Morris
 * Created on 10-Sept-2004
 */
public class Simpson extends SumType {

	static Add add = new Add();
	static Multiply mul = new Multiply();

	public Simpson()
	{
		super("Simpson");
	}

		
	public Object evaluate(Object elements[]) throws ParseException
	{
		Object ret;
		if(elements.length % 2 != 1)
			throw new ParseException("Simpson: there should be an odd number of ordinates, its"+elements.length);

		ret = add.add(elements[0],elements[elements.length-1]);
		for(int i=1;i<elements.length-2;i+=2)
		{
			ret = add.add(ret,elements[i]);
		}
		return ret;
	}
	/* (non-Javadoc)
	 * @see org.lsmp.djep.xjep.function.SumType#evaluate(org.nfunk.jep.Node, org.nfunk.jep.Variable, double, double, double, java.lang.Object, org.nfunk.jep.ParserVisitor, java.util.Stack)
	 */
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
		// TODO Auto-generated method stub
		int i=0;
		double val;
		Object[] res=new Object[(int) ((max-min)/inc)+1];	
		for(i=0,val=min;val<=max;++i,val=min+i*inc)
		{
			var.setValue(new Double(val));
				
			node.jjtGetChild(0).jjtAccept(pv,data);	
			checkStack(stack); // check the stack
			res[i] = stack.pop();
		}
		Object ret = evaluate(res);
		stack.push(ret);
		return ret;
	}

}
