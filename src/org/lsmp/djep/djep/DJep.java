/* @author rich
 * Created on 16-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;
import org.lsmp.djep.xjep.*;
import org.nfunk.jep.*;
/**
 * Adds differentation facilities to JEP.
 * For example
 * <pre>
 * DJep j = new DJep();
 * j.addStandardDiffRules();
 * ....
 * Node node = j.parse("x^3");
 * Node diff = j.differentiate(node,"x");
 * Node simp = j.simplify(diff);
 * j.println(simp);
 * Node node2 = j.parse("diff(x^4,x)");
 * Node proc = j.preprocess(node2);
 * Node simp2 = j.simplify(proc);
 * j.println(simp2);
 * </pre>
 * @author Rich Morris
 * Created on 16-Nov-2003
 */
public class DJep extends XJep {
	protected DifferentiationVisitor dv = new DifferentiationVisitor(this);
	/**
	 * Standard constructor.
	 * Use this instead of JEP or XJep if differentation facilities are required.
	 */
	public DJep()
	{
		this.pv = new DPrintVisitor();
//		this.vf = ;
		this.symTab = new DSymbolTable(new DVariableFactory());

		addFunction("diff",new Diff());
	}
	/**
	 * Differentiate an equation with respect to a variable.
	 * @param node top node of the expresion tree to differentiate.
	 * @param name differentiate with respect to this variable.
	 * @return the top node of a new parse tree representing the derivative.
	 * @throws ParseException if for some reason equation cannot be differentiated,
	 * ususaly if it has not been taught how to differentiate a particular function.
	 */
	public Node differentiate(Node node,String name) throws ParseException
	{
		return dv.differentiate(node,name,this);
	}
	protected DJep(DJep j)
	{
		super((XJep) j);
		this.dv=j.dv;
	}

	public XJep newInstance()
	{
		DJep newJep = new DJep(this);
		return newJep;
	}
	public XJep newInstance(SymbolTable st)
	{
		DJep newJep = new DJep(this);
		newJep.symTab = st;
		return newJep;
	}

	/** 
	 * Returns the visitor used for differentiation. Allows more advanced functions.
	 */
	public DifferentiationVisitor getDifferentationVisitor() { return dv; }
	/** 
	 * Adds the standard set of differentation rules.
	 * @return false if there was a parse error. 
	 */ 
	public boolean addStandardDiffRules() { return dv.addStandardDiffRules(); }
	//public DPrintVisitor getDPrintVisitor() { return (DPrintVisitor) pv; }

	/**
	 * Adds a rule with instruction on how to differentiate a function.
	 * @param rule
	 */
	public void addDiffRule(DiffRulesI rule) {
		dv.addDiffRule(rule);
	}

}
