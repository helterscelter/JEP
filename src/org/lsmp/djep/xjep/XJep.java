/* @author rich
 * Created on 16-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;

import org.nfunk.jep.*;
import java.util.*;
import java.io.PrintStream;
/**
 * An extended version of JEP adds various routines for working with trees.
 * Has a NodeFactory, and OperatorSet, TreeUtils
 * and Visitors DoNothingVisitor,
 * @author Rich Morris
 * Created on 16-Nov-2003
 */
public class XJep extends JEP {
	/** Creates new nodes */
	protected NodeFactory nf = null;
	/** A few utility functions. */
	protected TreeUtils tu = null;
	protected DeepCopyVisitor copier = null;
	protected SubstitutionVisitor subv = null;
	protected SimplificationVisitor simpv = null;
	protected CommandVisitor commandv = null;
	protected PrintVisitor pv = null;
	private VariableFactory vf = new XVariableFactory();

	public XJep()
	{
		this.symTab = new XSymbolTable(vf); 

		/** Creates new nodes */
		nf = new NodeFactory();
		/** Collects operators **/
		opSet = new XOperatorSet();
		/** A few utility functions. */
		tu = new TreeUtils();
		
		copier = new DeepCopyVisitor();
		subv = new SubstitutionVisitor();
		ev = new XEvaluatorVisitor();
		simpv = new SimplificationVisitor();
		commandv = new CommandVisitor();
		pv = new PrintVisitor();
	}

	/** Copy constructions, reuses all the componants of argument. */
	protected XJep(XJep j)
	{
		super((JEP) j);
		this.commandv=j.commandv;
		this.copier=j.copier;
		this.ev=j.ev;
		this.nf=j.nf;
		this.opSet=j.opSet;
		this.pv=j.pv;
		this.simpv=j.simpv;
		this.subv=j.subv;
		this.tu=j.tu;
	}

	private JEP ingrediant = null;
	public XJep(JEP j)
	{
		ingrediant=j;
		/** Creates new nodes */
		nf = new NodeFactory();
		this.symTab = new XSymbolTable(vf); 
		this.funTab = j.getFunctionTable();
		/** Collects operators **/
		opSet = new XOperatorSet(j.getOperatorSet());
		/** A few utility functions. */
		tu = new TreeUtils();
		copier = new DeepCopyVisitor();
		subv = new SubstitutionVisitor();
		ev = new XEvaluatorVisitor();
		simpv = new SimplificationVisitor();
		commandv = new CommandVisitor();
		pv = new PrintVisitor();
	}
	public XJep newInstance()
	{
		XJep newJep = new XJep(this);
		return newJep;
	}
	public XJep newInstance(SymbolTable st)
	{
		XJep newJep = new XJep(this);
		newJep.symTab = st;
		return newJep;
	}
	public void addStandardFunctions()
	{
		if(ingrediant!=null)
		{
			ingrediant.addStandardFunctions();
		} 
		else super.addStandardFunctions();
		addFunction("eval",new Eval());
	}

	public void addStandardConstants()
	{
		if(ingrediant!=null)
		{
			ingrediant.addStandardConstants();
			for(Enumeration enum=ingrediant.getSymbolTable().elements();enum.hasMoreElements();)
			{
				Variable var = (Variable) enum.nextElement();
				if(var.isConstant())
					this.symTab.addConstant(var.getName(),var.getValue());
				//else
				//	this.symTab.addVariable(var.getName(),var.getValue());
			}
		} 
		else super.addStandardConstants();
	}

	/** Returns a deep copy of an expression tree. */
	public Node deepCopy(Node node) throws ParseException
	{
		return copier.deepCopy(node,this);
	}
	/** Evaluates a node. */
	public Object evaluate(Node node) throws Exception
	{
		return ev.getValue(node,new Vector(),this.symTab);
	}
	/** Returns a simplification of an expression tree. */
	public Node simplify(Node node) throws ParseException
	{
		return simpv.simplify(node,this);
	}
	/** Preprocesses an equation to allow the diff and eval operators to be used. */
	public Node preprocess(Node node) throws ParseException
	{
		return commandv.process(node,this);
	}
	/** Substitute all occurences of a named variable with an expression tree. */ 
	public Node substitute(Node orig,String name,Node replacement) throws ParseException
	{
		return subv.substitute(orig,name,replacement,this);
	}
	/** Substitute all occurences of a set of named variable with a set of expression tree. */ 
	public Node substitute(Node orig,String names[],Node replacements[]) throws ParseException
	{
		return subv.substitute(orig,names,replacements,this);
	}
	/** Prints the expresion tree on standard output. */
	public void print(Node node) { pv.print(node); }
	/** Prints the expresion tree on given stream. */
	public void print(Node node,PrintStream out) { pv.print(node,out); }
	/** Prints the expresion tree on standard output with newline at end. */
	public void println(Node node) { pv.println(node); }
	/** Prints the expresion tree on given stream with newline at end. */
	public void println(Node node,PrintStream out) { pv.println(node,out); }
	/** Returns a string representation of a expresion tree. */
	public String toString(Node node) { return pv.toString(node); }
		
	public NodeFactory getNodeFactory() {return nf;}
	public TreeUtils getTreeUtils() { return tu; }
//	public SimplificationVisitor getSimpV() { return simpv; }
	public PrintVisitor getPrintVisitor() {	return pv;	}

	public Object findVarValue(String name) throws Exception
	{
		XVariable xvar = (XVariable) getVar(name);
		return xvar.findValue(this);
	}


}
