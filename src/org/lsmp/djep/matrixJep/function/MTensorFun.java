/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.lsmp.djep.matrixJep.function;

import org.nfunk.jep.*;
import org.lsmp.djep.vectorJep.values.*;
import org.lsmp.djep.vectorJep.function.*;
import org.lsmp.djep.matrixJep.nodeTypes.*;
import org.lsmp.djep.xjep.*;
//import org.lsmp.djep.matrixJep.nodeTypes.*;

/**
 * A enhanced version of list, allows matricies and tensors.
 * 
 * @author Rich Morris
 * Created on 27-Nov-2003
 */
public class MTensorFun extends TensorFun 
	implements PrintVisitor.PrintRulesI,NaryOperatorI
{
	public MTensorFun()
	{
		numberOfParameters = -1;
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
