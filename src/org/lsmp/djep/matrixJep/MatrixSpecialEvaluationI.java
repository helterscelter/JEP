/* @author rich
 * Created on 26-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;
import org.nfunk.jep.*;
import org.lsmp.djep.matrixJep.nodeTypes.*;
import org.lsmp.djep.vectorJep.values.*;

/**
 * If a function requires a special form of evaluation it should
 * implement this interface.
 * 
 * @author Rich Morris
 * Created on 26-Nov-2003
 */
public interface MatrixSpecialEvaluationI {
	/** 
	 * Returns the result of evaluating this node and the tree below.
	 * This method has the responsability for evaluating the children of the node
	 * and it should generally call
	 * <pre>
	 * 		MatrixValueI res = (MatrixValueI) node.jjtGetChild(i).jjtAccept(visitor,null);	
	 * </pre>
	 * for each child.
	 * 
	 * @param node The top node.
	 * @param visitor The parser visitor
	 * @param jep The current MatrixJep instance.
	 * @return Value after evaluation.
	 * @throws ParseException
	 */
	public MatrixValueI evaluate(MatrixNodeI node,MatrixEvaluator visitor,MatrixJep jep) throws ParseException;
}
