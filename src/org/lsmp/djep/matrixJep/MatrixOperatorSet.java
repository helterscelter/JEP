/* @author rich
 * Created on 27-Jul-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;
import org.lsmp.djep.matrixJep.function.*;
import org.nfunk.jep.Operator;

/**
 * The set of operators used in matricies.
 * 
 * @author Rich Morris
 * Created on 27-Jul-2003
 */
public class MatrixOperatorSet {
	public static final Operator TENSOR = new Operator("TENSOR",new MTensorFun(),Operator.NARY);
}
