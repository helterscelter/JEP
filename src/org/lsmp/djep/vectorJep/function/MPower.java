/* @author rich
 * Created on 26-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.function;

import org.lsmp.djep.vectorJep.*;
import org.lsmp.djep.vectorJep.values.*;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.*;
/**
 * @author Rich Morris
 * Created on 26-Nov-2003
 */
public class MPower extends Power implements BinaryOperatorI 
{
	private static Power pow = new Power();

	public MPower() {
		super();
	}
	public Dimensions calcDim(Dimensions ldim,Dimensions rdim) throws ParseException
	{
		if(ldim.equals(Dimensions.ONE) && rdim.equals(Dimensions.ONE))
			return Dimensions.ONE;
		throw new ParseException("Power: both sides must be 0 dimensional");
	}

	public MatrixValueI calcValue(
		MatrixValueI res,
		MatrixValueI lhs,
		MatrixValueI rhs) throws ParseException
	{
		res.setEle(0,pow.power(lhs.getEle(0),rhs.getEle(0)));
		return res;
	}

/*
	public MatrixNodeI preprocess(MatrixNodeI node,
			MatrixPreprocessVisitor visitor,MatrixJep jep)	throws ParseException {

		MatrixNodeI children[] = visitor.visitChildren(node,null);
		ASTMOpNode opNode = (ASTMOpNode) node; 
		if(children.length != 2)
			throw new ParseException("Hat opperator should have two children, it has "+children.length);
		if( children[0].getDim().equals(Dimensions.ONE)
		 && children[1].getDim().equals(Dimensions.ONE))
		{
			opNode.setOperator(jep.opSet.getPower());
			//opNode.s
		}
		else if( children[0].getDim().equals(Dimensions.THREE)
		 && children[1].getDim().equals(Dimensions.THREE))
		{
			opNode.setOperator(jep.opSet.getCross());
		}
		else
			throw new ParseException("Children of hat should have dimensions of 1 (power) or 3 (cross product)");
		return node;		
	}
*/
}
