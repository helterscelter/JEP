/* @author rich
 * Created on 17-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;

import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import java.io.PrintStream;
/**
 * An interface specifying the methods needed for a sub class of JEP
 * to implement the xjep utilities.
 *  
 * @author Rich Morris
 * Created on 17-Nov-2003
 */
public interface XJepI {
	/** Returns a deep copy of an expression tree. */
	public Node deepCopy(Node node) throws ParseException;
	/** Evaluates a node. */
	public Object evaluate(Node node) throws Exception;
	/** Returns a simplification of an expression tree. */
	public Node simplify(Node node) throws ParseException;
	/** Preprocesses an equation to allow the diff and eval operators to be used. */
	public Node preprocess(Node node) throws ParseException;
	/** Substitute all occurences of a named variable with an expression tree. */ 
	public Node substitute(Node orig,String name,Node replacement) throws ParseException;
	/** Substitute all occurences of a set of named variable with a set of expression tree. */ 
	public Node substitute(Node orig,String names[],Node replacements[]) throws ParseException;
	/** Prints the expresion tree on standard output. */
	public void print(Node node);
	/** Prints the expresion tree on given stream. */
	public void print(Node node,PrintStream out);
	/** Prints the expresion tree on standard output with newline at end. */
	public void println(Node node);
	/** Prints the expresion tree on given stream with newline at end. */
	public void println(Node node,PrintStream out);
	/** Returns a string representation of an expresion tree. */
	public String toString(Node node);
	/** Parses a string and returns the expresion tree. */
	public Node parse(String expression) throws ParseException;
	public SymbolTable getSymbolTable();
	/** Adds a function. */
	public void addFunction(String functionName,PostfixMathCommandI function);
	public FunctionTable getFunctionTable();
	public NodeFactory getNodeFactory();
	public OperatorSet getOperatorSet();
	public TreeUtils getTreeUtils();
	/** Returns a new XJepI instance with same symbol table */
	public XJepI newInstance();
	/** Returns a new XJepI instance with given symbol table */
	public XJepI newInstance(SymbolTable st);
}
