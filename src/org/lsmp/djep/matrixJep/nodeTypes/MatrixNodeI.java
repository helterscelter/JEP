/* @author rich
 * Created on 01-Feb-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep.nodeTypes;
import org.nfunk.jep.*;
import org.lsmp.djep.vectorJep.*;
import org.lsmp.djep.vectorJep.values.*;
/**
 * @author Rich Morris
 * Created on 01-Feb-2004
 */
public interface MatrixNodeI extends Node {
	public Dimensions getDim();
//	public void setDim(Dimensions dim);
	public MatrixValueI getMValue();
	//public void setMValue(VectorMatrixTensorI val); 
}
