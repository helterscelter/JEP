/* @author rich
 * Created on 07-Jul-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.values;
import org.lsmp.djep.vectorJep.*;

//import JSci.maths.DoubleMatrix;
//import JSci.physics.relativity.Rank1Tensor;

/**
 * Represents a matrix.
 * @author Rich Morris
 * Created on 07-Jul-2003
 */
public class Matrix implements MatrixValueI 
{
	// want package access to simplify addition of matricies
	int rows=0;
	int cols=0;
	Object data[][] = null;
	Dimensions dims;
	
	private Matrix() {}
	/** Construct a matrix with given rows and cols. */
	public Matrix(int rows,int cols)
	{
		this.rows = rows;
		this.cols = cols;
		data = new Object[rows][cols];
		dims = Dimensions.valueOf(rows,cols);
	}
	
	/**
	 * Construct a Matrix from a set of row vectors.
	 * @param vecs
	 */
/*
	public Matrix(MVector[] vecs) throws ParseException
	{
		if(vecs==null) { throw new ParseException("Tried to create a matrix with null row vectors"); } 
		rows = vecs.length;
		if(rows==0) {  throw new ParseException("Tried to create a matrix with zero row vectors"); }		
		
		// now check that each vector has the same size.
		
		cols = vecs[0].size();
		for(int i = 1;i<rows;++i)
			if(cols != vecs[i].size())
				throw new ParseException("Each vector must be of the same size");
	
		data = new Object[rows][cols];
		for(int i = 0;i<rows;++i)
			for(int j=0;j<cols;++j)
			{
				data[i][j]= vecs[i].elementAt(j);
				if(data[i][j] == null)
					throw new ParseException("Null element in vector");
			}
	}
*/
	/**
	 * Returns a string rep of matrix. Uses [[a,b],[c,d]] syntax.  
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		for(int i = 0;i<rows;++i)
		{
			if(i>0) sb.append(',');
			sb.append('[');
			for(int j=0;j<cols;++j)
			{
				if(j>0)sb.append(',');
				sb.append(data[i][j]);
			}
			sb.append(']');
		}
		sb.append(']');
		return sb.toString();
	}
	public Dimensions getDim() { return dims; }
	public int getNumEles() { return rows*cols; }
	public int getNumRows() { return rows; }
	public int getNumCols() { return cols; }

	public void setEle(int n,Object value) 
	{
		int i = n / rows;
		int j = n % rows;
		data[i][j] = value;
	}
	public void setEle(int i,int j,Object value) 
	{
		data[i][j] = value;
	}
	public Object getEle(int n)
	{
		int i = n / rows;
		int j = n % rows;
		return data[i][j];
	}
	public Object getEle(int i,int j) 
	{
		return data[i][j];
	}
	/** sets the elements to those of the arguments. */
	public void setEles(MatrixValueI val)
	{
		if(!dims.equals(val.getDim())) return;
		for(int i=0;i<rows;++i)
			System.arraycopy(((Matrix) val).data[i],0,data[i],0,cols);
	}
}
