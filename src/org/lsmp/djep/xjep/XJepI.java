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
 * @author Rich Morris
 * Created on 17-Nov-2003
 */
public interface XJepI {
	public abstract Node deepCopy(Node node) throws ParseException;
	public abstract Object evaluate(Node node) throws Exception;
//	public abstract Object evaluate(Node node, SymbolTable sTab)
//		throws Exception;
	public abstract Node simplify(Node node) throws ParseException;
	public abstract Node preprocess(Node node) throws ParseException;
	public Node substitute(Node orig,String name,Node replacement) throws ParseException;
	public Node substitute(Node orig,String names[],Node replacements[]) throws ParseException;
	public void print(Node node);
	public void print(Node node,PrintStream out);
	public void println(Node node);
	public void println(Node node,PrintStream out);
	public String toString(Node node);

	public Node parse(String expression) throws ParseException;
	public SymbolTable getSymbolTable();
	public void addFunction(String functionName,PostfixMathCommandI function);
	public FunctionTable getFunctionTable();

	/**
	 * Prints a node on System.out.
	 * See PrintVisitor for other methods
	 */
	public abstract NodeFactory getNodeFactory();
	public abstract OperatorSet getOperatorSet();
	public abstract TreeUtils getTreeUtils();
	public abstract XJepI newInstance();
	public abstract XJepI newInstance(SymbolTable st);
//	public abstract XJepI newInstance(Object comps[]);
	
}
