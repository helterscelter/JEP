/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.lsmp.djep.vectorJep.function;

import java.util.*;
import org.nfunk.jep.*;
import org.lsmp.djep.vectorJep.Dimensions;
import org.lsmp.djep.vectorJep.values.*;
import org.lsmp.djep.xjep.*;
import org.nfunk.jep.function.*;
import org.lsmp.djep.matrixJep.nodeTypes.*;

/**
 * A enhanced version of list, allows matricies and tensors.
 * 
 * @author Rich Morris
 * Created on 27-Nov-2003
 */
public class TensorFun extends PostfixMathCommand 
	implements PrintVisitor.PrintRulesI,NaryOperatorI
{
	public TensorFun()
	{
		numberOfParameters = -1;
	}

	public Dimensions calcDim(Dimensions dims[]) throws ParseException
	{
		return Dimensions.valueOf(dims.length,dims[0]);
	}
	
	public MatrixValueI calcValue(MatrixValueI res,
		MatrixValueI inputs[]) throws ParseException
	{
		int eleSize = inputs[0].getNumEles();
		for(int i=0;i<inputs.length;++i)
		{
			for(int j=0;j<eleSize;++j)
			{
				res.setEle(i*eleSize+j,inputs[i].getEle(j));
			}
		}
		return res;
	}
	
	public void run(Stack inStack) throws ParseException 
	{
		checkStack(inStack); // check the stack
		if(curNumberOfParameters <1)
			throw new ParseException("Empty list");
		Object param1 = inStack.pop();
		
		if(param1 instanceof Vector)
		{
			Vector vec1 = (Vector) param1;
			int rows = curNumberOfParameters;
			int cols = vec1.size();
			Matrix res = new Matrix(rows,cols);
			for(int j=0;j<cols;++j)
				res.setEle(rows-1,j,vec1.elementAt(j));					
			for(int i=rows-2;i>=0;--i)
			{
				Vector vec = (Vector) inStack.pop();
				for(int j=0;j<cols;++j)
					res.setEle(i,j,vec.elementAt(j));					
			}
			inStack.push(res);
			return;
		}
		else if(param1 instanceof MatrixValueI)
		{
			MatrixValueI mat1 = (MatrixValueI) param1;
			int rows = curNumberOfParameters;
			int neles = mat1.getNumEles();
			MatrixValueI res = Tensor.getInstance(Dimensions.valueOf(rows,mat1.getDim()));
			for(int j=0;j<neles;++j)
				res.setEle((rows-1)*neles+j,mat1.getEle(j));				
			for(int i=rows-2;i>=0;--i)
			{
				MatrixValueI mat = (MatrixValueI) inStack.pop();
				for(int j=0;j<neles;++j)
					res.setEle(i*neles+j,mat.getEle(j));				
			}
			inStack.push(res);
			return;
		}
		else
		{
			MVector res = new MVector(curNumberOfParameters);
			res.setEle(curNumberOfParameters-1,param1);
			for(int i=curNumberOfParameters-2;i>=0;--i)
			{
				Object param = inStack.pop();
				res.setEle(i,param);
			}
			inStack.push(res);
			return;
		}
	}
	
	int curEle;
	/** recursive procedure to print the tensor with lots of brackets. **/
	protected void bufferAppend(MatrixNodeI node,PrintVisitor pv,int currank) throws ParseException
	{
		pv.append("[");
		if(currank+1 >= node.getDim().rank())
		{
			// bottom of tree
			for(int i=0;i<node.getDim().getIthDim(currank);++i)
			{
				if(i!=0) pv.append(",");
				node.jjtGetChild(curEle++).jjtAccept(pv,null);
			}
		}
		else
		{
			// not bottom of tree
			for(int i=0;i<node.getDim().getIthDim(currank);++i)
			{
				if(i!=0) pv.append(",");
				bufferAppend(node,pv,currank+1);
			}
		}
		pv.append("]");
	}

	/**
	 * Used to print the TensorNode with all its children.
	 * Method implements PrintVisitor.PrintRulesI.
	 */
	public void append(Node node,PrintVisitor pv) throws ParseException
	{
		curEle = 0;
		bufferAppend((MatrixNodeI) node,pv,0);
	}

}
