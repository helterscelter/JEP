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
 * @author Rich Morris
 * Created on 26-Nov-2003
 */
public interface MatrixSpecialEvaluationI {
	public MatrixValueI evaluate(MatrixNodeI node,MatrixEvaluator visitor,MatrixDJep j) throws ParseException;
}
