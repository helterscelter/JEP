/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;
import org.nfunk.jep.*;
import org.lsmp.djep.vectorJep.*;
import org.lsmp.djep.vectorJep.values.MatrixValueI;

/**
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public interface MatrixVariableI {
	public abstract Dimensions getDimensions();
	public abstract void setDimensions(Dimensions dims);
	public abstract MatrixValueI getMValue();
	//public abstract void setMValue(VectorMatrixTensorI value);
	public abstract boolean hasValidValue();
	public abstract void setValidValue(boolean b);
	public abstract Node getEquation();
}
