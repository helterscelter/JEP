/* @author rich
 * Created on 19-Jun-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;
import org.nfunk.jep.*;

/**
 * Interface for commands used by the CommandVisitor
 */
public interface CommandVisitorI {

	/**
	 * Performs the specified action on an expression tree.
	 * @param node top node of the tree
	 * @return top node of the results.
	 * @throws ParseException
	 */
	public Node process(Node node,Node children[],XJepI xjep) throws ParseException;

	/**
	 * Performs the specified action during evaluation.
	 * @param node top node of the tree
	 * @return Result of evaluating the node.
	 * @throws ParseException
	 */
//	public Object evaluate(Node node,Node children[]) throws ParseException;
}
