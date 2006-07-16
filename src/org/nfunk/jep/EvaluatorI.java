/* @author rich
 * Created on 22-Apr-2005
 *
 * See LICENSE.txt for license information.
 */
package org.nfunk.jep;

/**
 * @author Rich Morris
 * Created on 22-Apr-2005
 */
public interface EvaluatorI {
	
	/**
	 * Evaluates a node and returns and object with the value of the node.
	 * 
	 * @throws ParseException if errors occur during evaluation;
	 */
	public abstract Object eval(Node node) throws ParseException;
}
