/* @author rich
 * Created on 07-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep;
import org.nfunk.jep.*;
import org.lsmp.djep.groupJep.function.*;
import org.nfunk.jep.function.*;
/**
 * 
 * @author Rich Morris
 * Created on 07-Mar-2004
 */
public class GOperatorSet extends OperatorSet {

	/**
	 * TODO Should really change the properties of operators, might muck up simplification.
	 */
	public GOperatorSet(GroupI group) {
		super();
		OP_ADD.setPFMC(new GAdd(group));
		OP_SUBTRACT.setPFMC(new GSubtract(group));
		OP_MULTIPLY.setPFMC(new GMultiply(group));
		OP_DIVIDE.setPFMC(new GDivide(group));
		OP_MOD.setPFMC(new GMod(group));
		OP_POWER.setPFMC(new GPower(group));
		OP_UMINUS.setPFMC(new GUMinus(group));
		OP_LT.setPFMC(new GComparative(group,Comparative.LT));
		OP_GT.setPFMC(new GComparative(group,Comparative.GT));
		OP_LE.setPFMC(new GComparative(group,Comparative.LE));
		OP_GE.setPFMC(new GComparative(group,Comparative.GE));
		OP_NE.setPFMC(new GComparative(group,Comparative.NE));
		OP_EQ.setPFMC(new GComparative(group,Comparative.EQ));

		OP_AND.setPFMC(new GLogical(Logical.AND));
		OP_AND.setPFMC(new GLogical(Logical.OR));
		OP_OR.setPFMC(new GNot());
		OP_LIST.setPFMC(new GList(group));
	}

}
