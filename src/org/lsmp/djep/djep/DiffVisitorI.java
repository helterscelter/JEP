/* @author rich
 * Created on 16-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;

import java.io.PrintStream;

import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

/**
 * An interface which defines the action of a DifferationVisitor.
 * Not currently of much functionality, might be useful
 * if the need to subclass DifferentationVisitor arises.
 * TODO remove this class.
 *
 * @author Rich Morris
 * Created on 16-Nov-2003
 */
public interface DiffVisitorI {
	/**
	   * Differentiates an expression tree wrt a variable var.
	   * @param node the top node of the expresion tree
	   * @param var the variable to differentiate wrt
	   * @return the top node of the differentiated expression 
	   * @throws ParseException if some error occured while trying to differentiate, for instance of no rule supplied for given function.
	   * @throws IllegalArgumentException
	   */
	public abstract Node differentiate(Node node, String var,DJepI djep)
		throws ParseException, IllegalArgumentException;
	/** 
	   * Adds the standard set of differentation rules. 
	   * Corresponds to all standard functions in the JEP plus a few more.
	   * <pre>
	   * sin,cos,tan,asin,acos,atan,sinh,cosh,tanh,asinh,acosh,atanh
	   * sqrt,log,ln,abs,angle
	   * sum,im,re are handled seperatly.
	   * rand and mod currently unhandled
	   * 
	   * Also adds rules for functions not in JEP function list:
	   * 	sec,cosec,cot,exp,pow,sgn 
	   * 
	   * TODO include if, min, max, sgn, dot, cross
	   * </pre>
	   * @return false on error, (i.e. if parsing of a rule failed)
	   */
	public abstract boolean addStandardDiffRules() throws ParseException;
	/** Adds the rules for a given function. */
	public abstract void addDiffRule(DiffRulesI rule);
	/** finds the rule for function with given name. */
	public abstract DiffRulesI getDiffRule(String name);
	/**
	   * Prints all the differentation rules for all functions on System.out.
	   */
	public abstract void printDiffRules();
	/**
	   * Prints all the differentation rules for all functions on specifed stream.
	   */
	public abstract void printDiffRules(PrintStream out);
}
