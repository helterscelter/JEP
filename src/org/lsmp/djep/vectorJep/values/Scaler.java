/* @author rich
 * Created on 04-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.values;

import org.lsmp.djep.vectorJep.*;

/**
 * Degenerate i.e. rank 0 Tensor. Just has a single element.
 * @author Rich Morris
 * Created on 04-Nov-2003
 */
public class Scaler implements MatrixValueI {

	Object value = new Double(0.0);
	public Dimensions getDim() {return Dimensions.ONE; }
	public int getNumEles() { return 1;	}
	public void setEle(int i, Object value) {if(value!=null) this.value = value;}
	public Object getEle(int i) {return value; }
//	public void setValue(Object value) { this.value = value;}
//	public Object getValue() {return value; }
	public String toString() { return value.toString(); }
	/** sets the elements to those of the arguments. */
	public void setEles(MatrixValueI val)
	{
		if(!(val.getDim().equals(Dimensions.ONE))) return;
		value = val.getEle(0);
	}
}
