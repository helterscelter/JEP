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
 * The power operator
 * @author Rich Morris
 * Created on 26-Nov-2003
 */
public class MPower extends Power implements BinaryOperatorI 
{
//	private static Power pow = new Power();

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
		res.setEle(0,power(lhs.getEle(0),rhs.getEle(0)));
		return res;
	}
}
