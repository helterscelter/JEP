/* @author rich
 * Created on 15-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.function;

import java.util.*;
import org.lsmp.djep.vectorJep.Dimensions;
import org.lsmp.djep.vectorJep.values.*;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * ele(x,i) returns the i-th element of x.
 * @author Rich Morris
 * Created on 15-Nov-2003
 */
public class Ele extends PostfixMathCommand implements BinaryOperatorI {

	public Ele() {
		super();
		numberOfParameters = 2;
	}

	public Dimensions calcDim(Dimensions ldim, Dimensions rdim)
		throws ParseException {
		return Dimensions.ONE;
	}

	public MatrixValueI calcValue(MatrixValueI res,
		MatrixValueI param1,MatrixValueI param2) throws ParseException
	{
//		Number num = (Number) rhs.getEle(0);
//		res.setEle(0,lhs.getEle(num.intValue()-1));		

		if(param1 instanceof MVector)
		{
			if(param2 instanceof Scaler)
			{
				int index = ((Double) param2.getEle(0)).intValue()-1;
				Object val = ((MVector) param1).getEle(index);
				res.setEle(0,val); 
			}
			else throw new ParseException("Bad second argument to ele, expecting a double "+param2.toString());
		}
		else if(param1 instanceof Matrix)
		{
			if(param2 instanceof MVector)
			{
				MVector vec = (MVector) param2;
				if(vec.getDim().equals(Dimensions.TWO))
				{
					Double d1 = (Double) vec.getEle(0);
					Double d2 = (Double) vec.getEle(1);
					Object val = ((Matrix) param1).getEle(d1.intValue()-1,d2.intValue()-1);
					res.setEle(0,val);
				}
			}
			else throw new ParseException("Bad second argument to ele, expecting [i,j] "+param2.toString());
		}
		else if(param1 instanceof Tensor)
		{
			throw new ParseException("Sorry don't know how to find elements for a tensor");
		}
		else
			throw new ParseException("ele requires a vector matrix or tensor for first argument it has "+param1.toString());
		return res;
	}
	
	public void run(Stack stack) throws ParseException 
	{
		checkStack(stack); // check the stack
	
		Object param1,param2;
	 
		// get the parameter from the stack
	        
		param2 = stack.pop();
		param1 = stack.pop();
	            
		if(param1 instanceof MVector)
		{
			if(param2 instanceof Double)
			{
				Object val = ((MVector) param1).getEle(((Double) param2).intValue()-1);
				stack.push(val);
				return; 
			}
			else throw new ParseException("Bad second argument to ele, expecting a double "+param2.toString());
		}
		else if(param1 instanceof Matrix)
		{
			if(param2 instanceof MVector)
			{
				MVector vec = (MVector) param2;
				if(vec.getDim().equals(Dimensions.TWO))
				{
					Double d1 = (Double) vec.getEle(0);
					Double d2 = (Double) vec.getEle(1);
					Object val = ((Matrix) param1).getEle(d1.intValue()-1,d2.intValue()-1);
					stack.push(val);
					return; 
				}
			}
			else throw new ParseException("Bad second argument to ele, expecting [i,j] "+param2.toString());
		}
		else if(param1 instanceof Tensor)
		{
			throw new ParseException("Sorry don't know how to find elements for a tensor");
		}
		throw new ParseException("ele requires a vector matrix or tensor for first argument it has "+param1.toString());
	}
}
