/* @author rich
 * Created on 02-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.function;
import org.lsmp.djep.vectorJep.*;
import org.lsmp.djep.vectorJep.values.*;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommandI;

/**
 * A matrix enabled operator with N arguments.
 * This interface is primarilary used in the matrixJep package
 * but is here for convienience.
 *  
 * @author Rich Morris
 * Created on 02-Nov-2003
 */
public interface NaryOperatorI extends PostfixMathCommandI {
	/** Find the dimensions of this operator when applied to arguments with given dimensions. */
	public Dimensions calcDim(Dimensions dims[]) throws ParseException;
	/** Calculates the value of this operator for given input with results stored in res.
	 * res is returned.
	 */
	public MatrixValueI calcValue(MatrixValueI res,
		MatrixValueI inputs[]) throws ParseException;
}
