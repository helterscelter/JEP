/* @author rich
 * Created on 02-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.function;
import org.lsmp.djep.vectorJep.*;
import org.lsmp.djep.vectorJep.values.*;
import org.nfunk.jep.ParseException;
/**
 * @author Rich Morris
 * Created on 02-Nov-2003
 */
public interface UnaryOperatorI {
	public Dimensions calcDim(Dimensions ldim);
	public MatrixValueI calcValue(
		MatrixValueI res,
		MatrixValueI lhs) throws ParseException;
}
