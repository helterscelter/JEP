/* @author rich
 * Created on 19-Dec-2003
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
 * Created on 19-Dec-2003
 */
public class VectorJep extends JEP {

	
	public VectorJep() {
		super();
		
		Operator.OP_ADD.setPFMC(new MAdd());
		Operator.OP_SUBTRACT.setPFMC(new MSubtract());
		Operator.OP_MULTIPLY.setPFMC(new MMultiply());
		Operator.OP_POWER.setPFMC(new MPower());
		Operator.OP_UMINUS.setPFMC(new MUMinus());
		Operator.OP_DOT.setPFMC(new Dot());
		Operator.OP_CROSS.setPFMC(new ExteriorProduct());
		Operator.OP_ASSIGN.setPFMC(new Assignment());
		Operator.OP_LIST.setPFMC(new TensorFun());
	}

	public void addStandardFunctions()
	{
		super.addStandardFunctions();
		//this.getFunctionTable().remove("if");
	}


	public VectorJep(JEP j) {
		super(j);
	}

}
