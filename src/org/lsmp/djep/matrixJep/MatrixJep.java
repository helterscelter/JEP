/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;

import org.lsmp.djep.djep.*;
import org.lsmp.djep.djep.diffRules.*;
import org.nfunk.jep.*;
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

	protected MatrixPreprocessor dec = new MatrixPreprocessor();
	protected MatrixVariableFactory mvf = new MatrixVariableFactory();
	protected MatrixEvaluator mev = new MatrixEvaluator();
	
	public MatrixJep() {
		super();
		nf = new MatrixNodeFactory();
		symTab = new DSymbolTable(mvf);
		opSet = new MatrixOperatorSet();
		
/*		Operator.OP_ADD.setPFMC(new MAdd());
		Operator.OP_SUBTRACT.setPFMC(new MSubtract());
		Operator.OP_MULTIPLY.setPFMC(new MMultiply());
		Operator.OP_POWER.setPFMC(new MPower());
		Operator.OP_UMINUS.setPFMC(new MUMinus());
		Operator.OP_DOT.setPFMC(new MDot());
		Operator.OP_CROSS.setPFMC(new ExteriorProduct());
		Operator.OP_ASSIGN.setPFMC(new Assignment());
*/
		Operator tens = ((MatrixOperatorSet) opSet).getMTensorFun();
		pv.addSpecialRule(tens,(PrintVisitor.PrintRulesI) tens.getPFMC());
		dv.addDiffRule(new PassThroughDiffRule(tens.getName(),tens.getPFMC()));
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
		else 
			return res;
	}

	public Object evaluateRaw(Node node) throws ParseException
	{
		Object res = mev.evaluate((MatrixNodeI) node,this);
		return res;
	}

	/** Preprocesses an equation to allow the diff and eval operators to be used. */
	public Node preprocess(Node node) throws ParseException
	{
		return dec.preprocess(node,this);
	}

	public MatrixJep(DJep j) {
		super(j);
		nf = new MatrixNodeFactory();
	}

	/* (non-Javadoc)
	 * @see org.nfunk.jep.JEP#getValueAsObject()
	 */
	public Object getValueAsObject() {
		try
		{
			Object res = mev.evaluate((MatrixNodeI) getTopNode(),this);
			if(res instanceof Scaler)
				return ((Scaler) res).getEle(0);
			else 
				return res;
		}
		catch(Exception e)
		{
			this.errorList.addElement("Error during evaluation:");
			this.errorList.addElement(e.getMessage());
			return null;
		}
	}

}
