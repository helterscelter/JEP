/* @author rich
 * Created on 18-Jun-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;

import org.nfunk.jep.function.PostfixMathCommand;
import org.nfunk.jep.*;
import java.util.*;
/**
 * A function specified by a string.
 * For example
 * <pre>
 * XJepI jep = new XJep();
 * j.addFunction("zap",new MacroFunction("zap",1,"x*(x-1)/2",j));
 * Node node = j.parse("zap(10)");
 * System.out.println(j.evaluate(node)); // print 45
 * </pre>
 * The names of the variables used inside the fuction depends on the number of arguments:
 * <ul>
 * <li>One argument variable must be x: <tt>new MacroFunction("sec",1,"1/cos(x)",j)</tt></li>
 * <li>Two arguments variables must be x or y: <tt>new MacroFunction("myPower",2,"x^y",j)</tt></li>
 * <li>Three or more arguments variables must be x1, x2, x3,...: <tt>new MacroFunction("add3",3,"x1+x2+x3",j)</tt></li>
 * </ul>
 * @author R Morris.
 * Created on 18-Jun-2003
 */
public class MacroFunction extends PostfixMathCommand
{
	private String name;
	private String macroExpression;
	private Node topNode;
	private EvaluatorVisitor ev = new EvaluatorVisitor();
	private XJepI localJep;
	private XSymbolTable mySymTab;
	
	public String getName() { return name; }
	public Node getTopNode() { return topNode; }
	
	/**
	 * Create a function specified by a string.
	 * For example <tt>new MacroFunction("sec",1,"1/cos(x)",tu)</tt> creates the function for sec.
	 * Variable names must be x,y for 1 or 2 variables or x1,x2,x3,.. for 3 or more variables.
	 * @param inName name of function
	 * @param nargs number of arguments
	 * @param expression a string representing the expression.
	 * @param inTu a reference to the TreeUtils object.
	 */
	public MacroFunction(String inName,int nargs,String expression,XJepI jep) throws IllegalArgumentException,ParseException
	{
		super();
		name = inName;

		XSymbolTable jepSymTab = (XSymbolTable) jep.getSymbolTable();
		mySymTab = (XSymbolTable) jepSymTab.newInstance(); 
		//new SymbolTable();
		mySymTab.copyConstants(jepSymTab);
		localJep = jep.newInstance(mySymTab);
		macroExpression = expression;
		numberOfParameters = nargs;
		topNode = localJep.parse(expression);
	}
	
	/**
	 * Calculates the value of the expression.
	 * @throws ParseException if run.
	 */
	public void run(Stack stack) throws ParseException 
	{

		if(numberOfParameters == 0) {}
		else if(numberOfParameters == 1)
			mySymTab.makeVarIfNeeded("x",stack.pop());
		else if(numberOfParameters == 2)
		{
			mySymTab.makeVarIfNeeded("y",stack.pop());
			mySymTab.makeVarIfNeeded("x",stack.pop());
		}
		else
		{
			for(int i=numberOfParameters-1;i>0;)
				mySymTab.makeVarIfNeeded("x"+String.valueOf(i),stack.pop());
		}
		try
		{
			Object res = ev.getValue(topNode,null,mySymTab);
			stack.push(res);
		}
		catch(Exception e1) { throw new ParseException("MacroFunction eval: "+e1.getMessage()); }
	}
}
