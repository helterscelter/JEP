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
 * @version 1.3.0.2 now extends number
 */
public class Scaler extends Number implements MatrixValueI {

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
	
	/** value of constant coeff. */	
	public int intValue() {return ((Number) value).intValue();	}
	/** value of constant coeff. */	
	public long longValue() {return ((Number) value).longValue();	}
	/** value of constant coeff. */	
	public float floatValue() {	return ((Number) value).floatValue();	}
	/** value of constant coeff. */	
	public double doubleValue() {return ((Number) value).doubleValue();	}

	public boolean equals(Object obj) {
		if(!(obj instanceof Scaler)) return false;
		Scaler s = (Scaler) obj;
		if(!s.getDim().equals(getDim())) return false;
		if(!value.equals(s.value)) return false;
		return true;
	}
	
	/**
	 * Always override hashCode when you override equals.
	 * Efective Java, Joshua Bloch, Sun Press
	 */
	public int hashCode() {
		int result = 17;
			result = 37*result+ value.hashCode();
		return result;
	}

}
