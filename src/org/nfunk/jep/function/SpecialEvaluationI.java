/* @author rich
 * Created on 18-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.nfunk.jep.function;
import org.nfunk.jep.*;
import java.util.Stack;
/**
 * @author Rich Morris
 * Created on 18-Nov-2003
 */
public interface SpecialEvaluationI {

	/**
	 * Performs the specified action on an expression tree.
	 * Serves no function in standard JEP but 
	 * @param node top node of the tree
	 * @param data	The data passed to visitor, typically not used.
	 * @param pv	The visitor, can be used decend on the children.
	 * @return top node of the results.
	 * @throws ParseException
	 */
//	public Node process(Node node,Object data,ParserVisitor pv) throws ParseException;

	/**
	 * Performs some special evaluation on the node.
	 * @param node	The current node
	 * @param data	The data passed to visitor, typically not used
	 * @param pv	The visitor, can be used evaluate the children
	 * @param stack	The stack of the evaluator
	 * @param symTab The symbol table 
	 * @return the value after evaluation
	 * @throws ParseException
	 */
	public Object evaluate(Node node,Object data,ParserVisitor pv,Stack stack,SymbolTable symTab) throws ParseException;
}
