/* @author rich
 * Created on 07-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep;
import org.nfunk.jep.*;
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
		OP_POWER.setPFMC(new MPower());
		OP_UMINUS.setPFMC(new MUMinus());
		OP_DOT.setPFMC(new MDot());
		OP_CROSS.setPFMC(new ExteriorProduct());
		OP_LIST.setPFMC(new VList());
	}

}
