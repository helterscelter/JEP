/* @author rich
 * Created on 15-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.function;

import org.lsmp.djep.vectorJep.Dimensions;
import org.lsmp.djep.vectorJep.values.MatrixValueI;
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
		MatrixValueI lhs,MatrixValueI rhs) throws ParseException
	{
		Number num = (Number) rhs.getEle(0);
		res.setEle(0,lhs.getEle(num.intValue()+1));		
		return res;
	}
}
