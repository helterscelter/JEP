/* @author rich
 * Created on 02-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.values;

import org.lsmp.djep.vectorJep.*;

/**
 * Interface defining methods needed to work with vectors and matricies.
 * @author Rich Morris
 * Created on 02-Nov-2003
 */
public interface MatrixValueI {
	/** Returns the dimension of this object. */
	public Dimensions getDim();
	/** The total number of elements. */
	public int getNumEles();
	/** sets the i-th element, treats data a a linear array. */
	public void setEle(int i,Object value);
	/** gets the i-th element, treats data a a linear array. */
	public Object getEle(int i);
	/** sets the elements to those of the arguments. */
	public void setEles(MatrixValueI val);
}
