/* @author rich
 * Created on 14-Feb-2005
 *
 * See LICENSE.txt for license information.
 */
package org.lsmp.djep.vectorJep.function;

import java.util.Stack;

import org.lsmp.djep.vectorJep.*;
import org.lsmp.djep.vectorJep.values.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.PostfixMathCommand;
import org.nfunk.jep.function.SpecialEvaluationI;

/**
 * Generate vectors and matricies.
 * First argument gives the size of the vector or matrix
 * second argument is the function to use to generate elements of vector or matrix.
 * Third argument (if present gives free variables used in the function, these will
 * run from 1 to number of rows or columns).
 * For example
 * <pre>
 * GenMat(3,1) -> [1,1,1]
 * GenMat(3,ii,ii) -> [1,2,3]
 * GenMat(3,rand()) -> [0.343,0.974,0.567]
 * GenMat([2,2],ii+jj,[ii,jj]) -> [[2,3],[3,4]]
 * </pre>
 * Note the free variables should not be the same name as variables already
 * in use, in particular i is often set to be the complex root of unity, and should be avoided.
 * 
 * @author Rich Morris
 * Created on 14-Feb-2005
 */
public class GenMat extends PostfixMathCommand implements SpecialEvaluationI
{
	public GenMat()
	{
		super();
		this.numberOfParameters = -1;
	}

	public Object evaluate(
		Node node,
		Object data,
		ParserVisitor pv,
		Stack stack,
		SymbolTable symTab)
		throws ParseException
	{
		node.jjtGetChild(0).jjtAccept(pv,data);
		Object sizeObj = stack.pop();
		int sizes[] = null;
		if( sizeObj instanceof Scaler)
		{
			sizes = new int[1];
			sizes[0] = ((Number) sizeObj).intValue();
		}
		else if( sizeObj instanceof MVector)
		{
			MVector vec = (MVector) sizeObj;
			int n = vec.getNumEles();
			sizes = new int[n];
			for(int i=0;i<n;++i)
				sizes[i] = ((Number) vec.getEle(i)).intValue();
		}
		else if(sizeObj instanceof MatrixValueI)
			throw new ParseException("GenMat: First arg should be scaler or vector");
		else
		{
			sizes = new int[1];
			sizes[0] = ((Number) sizeObj).intValue();
		}
		
		// Create result object
		Dimensions dim = Dimensions.valueOf(sizes);
		MatrixValueI res = Tensor.getInstance(dim);
		
		if(node.jjtGetNumChildren() == 2)
		{
			// no need to set variables
			for(int i=0;i<res.getNumEles();++i)
			{
				node.jjtGetChild(1).jjtAccept(pv,data);
				Object val = stack.pop();
				res.setEle(i,val);
			}
			stack.push(res);
			return res;
		}
		
		// Need to set variables
		
		Variable vars[] = VMap.getVars(node.jjtGetChild(2));
		if(vars.length != sizes.length)
			throw new ParseException("GenMat: number of variables must match number of dimensions");
		if(vars.length == 1)
		{
			for(int i=0;i<sizes[0];++i)
			{
				vars[0].setValue(new Integer(i+1));
				node.jjtGetChild(1).jjtAccept(pv,data);
				Object val = stack.pop();
				res.setEle(i,val);
			}
			stack.push(res);
			return res;
		}
		else if(vars.length == 2)
		{
			Matrix mat = (Matrix) res;
			for(int i=0;i<sizes[0];++i)
			{
				vars[0].setValue(new Integer(i+1));
				for(int j=0;j<sizes[1];++j)
				{
					vars[1].setValue(new Integer(j+1));
					node.jjtGetChild(1).jjtAccept(pv,data);
					Object val = stack.pop();
					mat.setEle(i,j,val);
				}
			}
			stack.push(res);
			return res;
		}
		else
			throw new ParseException("GenMat: can currently only generate vectors and matricies");
	}
}
