/* @author rich
 * Created on 16-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;

import org.nfunk.jep.*;
import org.lsmp.djep.xjep.function.*;
import java.util.*;
import java.io.PrintStream;
import java.io.Reader;
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

	/**
	 * Create a new XJep will all the function of JEP plus printing and other features.
	 */
	public XJep()
	{
		this.symTab = new XSymbolTable(vf); 

		/* Creates new nodes */
		nf = new NodeFactory();
		/* Collects operators **/
		opSet = new XOperatorSet();
		/* A few utility functions. */
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
	/** Conversion constructor.
	 * Turns a JEP object into an XJep object.
	 * @param j 
	 */
	public XJep(JEP j)
	{
		ingrediant=j;
		/* Creates new nodes */
		nf = new NodeFactory();
		this.symTab = new XSymbolTable(vf); 
		this.funTab = j.getFunctionTable();
		/* Collects operators **/
		opSet = new XOperatorSet(j.getOperatorSet());
		/* A few utility functions. */
		tu = new TreeUtils();
		copier = new DeepCopyVisitor();
		subv = new SubstitutionVisitor();
		ev = new XEvaluatorVisitor();
		simpv = new SimplificationVisitor();
		commandv = new CommandVisitor();
		pv = new PrintVisitor();
	}
	/**
	 * Creates a new instance of XJep with the same componants as this one.
	 * Sub classes should overwrite this method to create objects of the correct type.
	 */
	public XJep newInstance()
	{
		XJep newJep = new XJep(this);
		return newJep;
	}
	/**
	 * Creates a new instance of XJep with the same componants as this one and the specified symbol table.
	 * Sub classes should overwrite this method to create objects of the correct type.
	 */
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
		addFunction("Sum",new Sum());
		addFunction("Product",new Product());
		addFunction("Min",new Min());
		addFunction("Max",new Max());
		addFunction("MinArg",new MinArg());
		addFunction("MaxArg",new MaxArg());
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
	/** Returns the node factory, used for constructing trees of Nodes. */ 
	public NodeFactory getNodeFactory() {return nf;}
	/** Returns the TreeUtilitities, used for examining properties of nodes. */ 
	public TreeUtils getTreeUtils() { return tu; }
//	public SimplificationVisitor getSimpV() { return simpv; }
	/** Returns the PrintVisitor, used for printing equations. */ 
	public PrintVisitor getPrintVisitor() {	return pv;	}

	/**
	 * Calculates the value for the variables equation and returns that value.  
	 * If the variable does not have an equation just return its value.
	 */
	public Object calcVarValue(String name) throws Exception
	{
		XVariable xvar = (XVariable) getVar(name);
		return xvar.calcValue(this);
	}

	/**
	 * Continue parsing without re-initilising the stream.
	 * Allows renetrancy of parser so that strings like
	 * "x=1; y=2; z=3;" can be parsed.
	 * When a semi colon is encountered parsing finishes leaving the rest of the string unparsed.
	 * Parsing can be resumed from the current position by using this method.
	 * For example
	 * <pre>
	 * XJep j = new XJep();
	 * j.restartParser("x=1;y=2; z=3;");
	 * Node node;
	 * try {
	 * while((node = j.continueParsing())!=null) {
	 *    j.println(node);
	 * } }catch(ParseException e) {}
	 * </pre>
	 * @return top node of equation parsed to date or null if empty equation
	 * @throws ParseException
	 * @see #restartParser
	 */
	public Node continueParsing() throws ParseException {
		return parser.continueParse();
	}

	/**
	 * Restarts the parser with the given string.
	 * @param str String containing a sequence of equations separated by semi-colons.
	 * @see #continueParsing
	 */
	public void restartParser(String str) {
		parser.restart(new java.io.StringReader(str), this);
	}

	/**
	 * Restarts the parser with the given Reader.
	 * @param reader Reader from which equations separated by semi-colons will be read.
	 * @see #continueParsing
	 */
	public void restartParser(Reader reader) {
		parser.restart(reader, this);
	}

}
