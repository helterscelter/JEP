/* @author rich
 * Created on 07-Jul-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.values;

import org.lsmp.djep.vectorJep.*;

/**
 * A Vector of elements.
 * @author Rich Morris
 * Created on 07-Jul-2003
 * @version 1.3.0.2 now extends number
 */

public class MVector extends Number implements MatrixValueI
{
	private Object data[] = null;
	private Dimensions dim;
	//DoubleMatrix jsciMat;
	
	private MVector() {}
	/** constructs a vector of a given size. **/
	public MVector(int size)
	{
		data = new Object[size];
		dim = Dimensions.valueOf(size);
	}
	
	/**
	 * Construct a Vector from a set of elements.
	 * @param vecs
	 */
/*
 	public MVector(Object[] eles) throws ParseException
	{
		if(eles==null) { throw new ParseException("Tried to create an MVector with null elements"); } 
		size = eles.length;
		if(size==0) {  throw new ParseException("Tried to create an Mvector with zero elements"); }		
		
		// now check that each vector has the same size.
		
		data = eles;
	}
*/
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		for(int i = 0;i<data.length;++i)
		{
				if(i>0) sb.append(',');
				sb.append(data[i]);
		}
		sb.append(']');
		return sb.toString();
	}

	public Dimensions getDim() { return dim; }	
	public int getNumEles() { return data.length; }
	public void setEle(int i,Object value) { data[i] = value; }
	public Object getEle(int i) { return data[i];	}
	/** sets the elements to those of the arguments. */
	public void setEles(MatrixValueI val)
	{
		if(!dim.equals(val.getDim())) return;
		System.arraycopy(((MVector) val).data,0,data,0,getNumEles());
	}

	/** value of constant ele(1). */	
	public int intValue() {return ((Number) data[0]).intValue();	}
	/** value of constant ele(1). */	
	public long longValue() {return ((Number) data[0]).longValue();	}
	/** value of constant ele(1). */	
	public float floatValue() {	return ((Number) data[0]).floatValue();	}
	/** value of constant ele(1). */	
	public double doubleValue() {return ((Number) data[0]).doubleValue();	}

	public boolean equals(Object obj) {
		if(!(obj instanceof MVector)) return false;
		MVector vec = (MVector) obj;
		if(!vec.getDim().equals(getDim())) return false;
		for(int i=0;i<data.length;++i)
				if(!data[i].equals(vec.data[i])) return false;
		return true;
	}

	/**
	 * Always override hashCode when you override equals.
	 * Efective Java, Joshua Bloch, Sun Press
	 */
	public int hashCode() {
		int result = 17;
		for(int i=0;i<data.length;++i)
			result = 37*result+ data[i].hashCode();
		return result;
	}
	
}
