/* @author rich
 * Created on 07-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import org.lsmp.djep.vectorJep.function.*;
/**
 * @author Rich Morris
 * Created on 07-Mar-2004
 */
public class VOperatorSet extends OperatorSet {

	/**
	 * 
	 */
	public VOperatorSet() {
		super();
		OP_ADD.setPFMC(new MAdd());
		OP_SUBTRACT.setPFMC(new MSubtract());
		OP_MULTIPLY.setPFMC(new MMultiply());
//		OP_MULTIPLY.setPFMC(new ElementMultiply());
		OP_POWER.setPFMC(new MPower());
		OP_UMINUS.setPFMC(new MUMinus());
		OP_DOT.setPFMC(new MDot());
		OP_CROSS.setPFMC(new ExteriorProduct());
		OP_LIST.setPFMC(new VList());
	}

	/** When set the multiplication of vectors and matricies will be element by element.
	 * Otherwise multiplication will be matrix multiplication (the default).
	 * 
	 * @param flag
	 */
	public void setElementMultiply(boolean flag)
	{
		if(flag)
		{
			OP_MULTIPLY.setPFMC(new ElementMultiply());
			OP_DIVIDE.setPFMC(new ElementDivide());
		}
		else
		{
			OP_MULTIPLY.setPFMC(new MMultiply());
			OP_DIVIDE.setPFMC(new Divide());
		}
	}
}
