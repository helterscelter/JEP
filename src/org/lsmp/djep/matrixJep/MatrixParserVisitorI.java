/* @author rich
 * Created on 29-Oct-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;
import org.lsmp.djep.matrixJep.nodeTypes.*;
import org.nfunk.jep.SimpleNode;
import org.nfunk.jep.ASTStart;
import org.nfunk.jep.ParseException;
/**
 * @author Rich Morris
 * Created on 29-Oct-2003
 */
public interface MatrixParserVisitorI {
	public Object visit(SimpleNode node, Object data) throws ParseException;
	public Object visit(ASTStart node, Object data) throws ParseException;

	/** constants **/
	public Object visit(ASTMConstant node, Object data) throws ParseException;
	/** multidimensions differentiable variables */
	public Object visit(ASTMVarNode node, Object data) throws ParseException;

	/** other functions **/
	public Object visit(ASTMFunNode node, Object data) throws ParseException;


}
