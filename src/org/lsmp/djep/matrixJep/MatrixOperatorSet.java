/* @author rich
 * Created on 27-Jul-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;
import org.lsmp.djep.matrixJep.function.*;
import org.lsmp.djep.vectorJep.function.*;
import org.nfunk.jep.*;
import org.lsmp.djep.xjep.*;
/**
 * The set of operators used in matricies.
 * 
 * @author Rich Morris
 * Created on 27-Jul-2003
 */
public class MatrixOperatorSet extends XOperatorSet {
	protected Operator TENSOR = new XOperator("TENSOR",new MList(),XOperator.NARY);
	public Operator getMList() { return TENSOR; }

	public MatrixOperatorSet() {
		super();
		OP_ADD.setPFMC(new MAdd());
		OP_SUBTRACT.setPFMC(new MSubtract());
		// TODO fix commutatitivity for matrix mult. How?
		OP_MULTIPLY.setPFMC(new MMultiply());
		OP_POWER.setPFMC(new MPower());
		OP_UMINUS.setPFMC(new MUMinus());
		OP_DOT.setPFMC(new MDot());
//		OP_CROSS= new XOperator("^^","^",new ExteriorProduct(),XOperator.BINARY+XOperator.RIGHT);
		OP_CROSS= new XOperator("^^","^",new ExteriorProduct(),XOperator.BINARY+XOperator.RIGHT,((XOperator) OP_CROSS).getPrecedence());
//		OP_CROSS.setPFMC(new ExteriorProduct());
		OP_ASSIGN.setPFMC(new MAssign());
	}
	
	public Operator[] getOperators()
	{
		Operator ops1[] = super.getOperators();
		Operator ops2[] = new Operator[ops1.length+1];
		System.arraycopy(ops1,0,ops2,0,ops1.length);
		ops2[ops1.length]= TENSOR;
		return ops2;
	}
}
