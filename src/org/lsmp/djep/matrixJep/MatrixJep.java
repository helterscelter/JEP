/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;

import org.lsmp.djep.djep.*;
import org.nfunk.jep.*;
import org.lsmp.djep.vectorJep.function.*;
import org.lsmp.djep.vectorJep.values.*;
import org.lsmp.djep.xjep.*;
import org.lsmp.djep.matrixJep.function.*;
import org.lsmp.djep.matrixJep.nodeTypes.*;
/**
 * An extension of JEP which allows advanced vector and matrix handeling and differentation.
 *
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public class MatrixJep extends DJep {

	public MatrixPreprocessor dec = new MatrixPreprocessor();
	public MatrixVariableFactory mvf = new MatrixVariableFactory();
	public MatrixEvaluator mev = new MatrixEvaluator();
	
	public MatrixJep() {
		super();
		nf = new MatrixNodeFactory();
		super.symTab = new DSymbolTable(mvf);
		
		Operator.OP_ADD.setPFMC(new MAdd());
		Operator.OP_SUBTRACT.setPFMC(new MSubtract());
		Operator.OP_MULTIPLY.setPFMC(new MMultiply());
		Operator.OP_POWER.setPFMC(new MPower());
		Operator.OP_UMINUS.setPFMC(new MUMinus());
		Operator.OP_DOT.setPFMC(new Dot());
		Operator.OP_CROSS.setPFMC(new ExteriorProduct());
		Operator.OP_ASSIGN.setPFMC(new Assignment());

		pv.addSpecialRule(MatrixOperatorSet.TENSOR,(PrintVisitor.PrintRulesI) MatrixOperatorSet.TENSOR.getPFMC());
		dv.addDiffRule(new PassThroughDiffRule("TENSOR",MatrixOperatorSet.TENSOR.getPFMC()));
	}

	public void addStandardFunctions()
	{
		super.addStandardFunctions();
		this.getFunctionTable().remove("if");
		this.getFunctionTable().put("if",new MIf());
	}
	public Node differentiate(Node node,String name) throws ParseException
	{
		Node res = dv.differentiate(node,name,this);
		return res;
	}

	public Object evaluate(Node node) throws ParseException
	{
		Object res = mev.evaluate((MatrixNodeI) node,this);
		if(res instanceof Scaler)
			return ((Scaler) res).getEle(0);
		else return res;
	}

	public MatrixJep(DJep j) {
		super(j);
		nf = new MatrixNodeFactory();
	}

}
