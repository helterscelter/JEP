/* @author rich
 * Created on 27-Jul-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep.function;
import org.lsmp.djep.vectorJep.*;
import org.lsmp.djep.vectorJep.values.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import java.util.*;

/**
 * An extension of the Add command to allow it to add MVector's and Matrix's.
 * @author Rich Morris
 * Created on 27-Jul-2003
 * TODO add handeling of tensors
 * TODO clean it all up
 */
public class MMultiply extends Multiply implements BinaryOperatorI {
	
	protected Add add = new Add();
	protected Subtract sub = new Subtract();
	
	public MMultiply()
	{
		//add = (Add) Operator.OP_ADD.getPFMC();
		//sub = (Subtract) Operator.OP_SUBTRACT.getPFMC();
		numberOfParameters = 2;
	}

	public Dimensions calcDim(Dimensions l,Dimensions r) {
		if(l.equals(Dimensions.ONE) && r.equals(Dimensions.ONE)) return Dimensions.ONE;
		if(l.is0D() ) return r; // scalar mult on left
		if(r.is0D() ) return l; // scalar mult on right
		if(l.is1D() && r.is1D() ) return null; // error for vector * vector
		if(l.is2D() && r.is1D() ) // matrix times vector
		{
			if(l.getLastDim() == r.getFirstDim()) return Dimensions.valueOf(l.getFirstDim());
			else return null;
		}
		if(l.is1D() && r.is2D() ) // vector times matrix
		{
			if(l.getLastDim() == r.getFirstDim()) return Dimensions.valueOf(r.getLastDim());
			else return null;
		}
		if(l.is2D() && r.is2D() ) // matrix times matrix
		{
			if(l.getLastDim() == r.getFirstDim()) return Dimensions.valueOf(l.getFirstDim(),r.getLastDim());
			else return null;
		}
		if(l.getLastDim() == r.getFirstDim()) // Something else
		{
			int dims[] = new int[l.rank()+r.rank()-2];
			int j = 0;
			for(int i=0;i<l.rank()-1;++i)	dims[j++]=l.getIthDim(i);
			for(int i=1;i<r.rank();++i)	dims[j++]=r.getIthDim(i);
			return Dimensions.valueOf(dims);
		}
		else return null;
	}


	/**
	 *  need to redo this as the standard jep version assumes commutivity.
	 */	
	public void run(Stack stack) throws ParseException 
	{
		checkStack(stack); // check the stack
		//if(this.curNumberOfParameters!=2) throw new ParseException("Multiply: should have two children its got "+stack.size());
		Object param2 = stack.pop();
		Object param1 = stack.pop();
		Object product = mul(param1, param2);
		stack.push(product);
		return;
	}

	/**
	 * Multiply two objects.
	 */
	
	public Object mul(Object param1, Object param2) throws ParseException 
	{
		// TODO tensor mult?
		if(param1 instanceof Matrix && param2 instanceof MVector)
			return mul((Matrix) param1,(MVector) param2);

		else if(param1 instanceof MVector && param2 instanceof Matrix)
			return mul((MVector) param1,(Matrix) param2);
			
		else if(param1 instanceof Matrix && param2 instanceof Matrix)
			return mul((Matrix) param1,(Matrix) param2);
			
		else if(param1 instanceof MVector && param2 instanceof MVector)
			return mul((MVector) param1,(MVector) param2);
			
		else if(param1 instanceof MVector)
			return mul((MVector) param1,param2);

		else if(param2 instanceof MVector)
			return mul(param1,(MVector) param2);

		else if(param1 instanceof Matrix)
			return mul((Matrix) param1,param2);

		else if(param2 instanceof Matrix)
			return mul(param1,(Matrix) param2);

		else
			return super.mul(param1,param2);
	}

	public MatrixValueI calcValue(MatrixValueI res,MatrixValueI param1,MatrixValueI param2) throws ParseException
	{
		if(res instanceof MVector && param1 instanceof Matrix && param2 instanceof MVector)
			return calcValue((MVector) res,(Matrix) param1,(MVector) param2);

		else if(res instanceof MVector && param1 instanceof MVector && param2 instanceof Matrix)
			return calcValue((MVector) res,(MVector) param1,(Matrix) param2);
			
		else if(res instanceof MVector && param1 instanceof MVector && param2 instanceof MVector)
			return calcValue((MVector) res,(MVector) param1,(MVector) param2);
			
		else if(res instanceof Matrix && param1 instanceof Matrix && param2 instanceof Matrix)
			return calcValue((Matrix) res,(Matrix) param1,(Matrix) param2);

		else if(res instanceof Scaler && param1 instanceof Scaler && param2 instanceof Scaler)
			return calcValue((Scaler) res,(Scaler) param1,(Scaler) param2);

		else if(res instanceof MVector && param1 instanceof MVector && param2 instanceof Scaler)
			return calcValue((MVector) res,(MVector) param1,((Scaler) param2).getEle(0));

		else if(res instanceof MVector && param1 instanceof Scaler && param2 instanceof MVector)
			return calcValue((MVector) res,((Scaler) param1).getEle(0),(MVector) param2);

		else if(res instanceof Matrix && param1 instanceof Matrix && param2 instanceof Scaler)
			return calcValue((Matrix) res,(Matrix) param1,((Scaler) param2).getEle(0));

		else if(res instanceof Matrix && param1 instanceof Scaler && param2 instanceof Matrix)
			return calcValue((Matrix) res,((Scaler) param1).getEle(0),(Matrix) param2);
		else
		{
			Object val = super.mul(param1,param2);
			res.setEle(0,val);
			return res;
		}
	}
	
	/** Multiply two 2D vectors, treat multiplocation as complex. */

	public MVector calcValue(MVector res,MVector lhs,MVector rhs) throws ParseException
	{
		if(!lhs.getDim().equals(rhs.getDim())) throw new ParseException("Vec * Vec: Miss match in sizes ("+lhs.getDim()+","+rhs.getDim()+") when trying to multiply two vectors!");
		if(!lhs.getDim().equals(Dimensions.TWO)) throw new ParseException("Vec * Vec: Sorry can only multiply 2D vectors which are treated as complex numbers.");
		res.setEle(0,sub.sub(
			super.mul(lhs.getEle(0),rhs.getEle(0)),
			super.mul(lhs.getEle(1),rhs.getEle(1))));
		res.setEle(1,add.add(
			super.mul(lhs.getEle(0),rhs.getEle(1)),
			super.mul(lhs.getEle(1),rhs.getEle(0))));
		return res;			
	}

	/** Multiply two 2D vectors, treat multiplocation as complex. */
	public MVector mul(MVector lhs,MVector rhs) throws ParseException
	{
		MVector res = new MVector(2); 
		return(calcValue(res,lhs,rhs));
	}

	/** Multiply matrix and vectors. */
	public MVector calcValue(MVector res,Matrix lhs,MVector rhs) throws ParseException
	{
		if(lhs.getNumCols() != rhs.getNumEles()) throw new ParseException("Mat * Vec: Miss match in sizes ("+lhs.getNumCols()+","+rhs.getNumEles()+") when trying to add vectors!");
		for(int i=0;i<lhs.getNumRows();++i)
		{
			Object val = super.mul(lhs.getEle(i,0),rhs.getEle(0));
			for(int j=1;j<lhs.getNumCols();++j)
				val = add.add(val,super.mul(lhs.getEle(i,j),rhs.getEle(j)));
			res.setEle(i,val);
		}
		return res;			
	}

	/** Multiply matrix and vectors. */
	public MVector calcValue(MVector res,MVector lhs,Matrix rhs) throws ParseException
	{
		if(lhs.getNumEles() != rhs.getNumRows()) throw new ParseException("Multiply Matrix , Vector: Miss match in sizes ("+lhs.getNumEles()+","+rhs.getNumRows()+")!");
		for(int i=0;i<rhs.getNumCols();++i)
		{
			Object val = super.mul(lhs.getEle(0),rhs.getEle(0,i));
			for(int j=1;j<rhs.getNumRows();++j)
				val = add.add(val,
						super.mul(lhs.getEle(j),rhs.getEle(j,i)));
			res.setEle(i,val);
		}
		return res;			
	}

	/** multiply two matricies. */
	public Matrix calcValue(Matrix res,Matrix lhs,Matrix rhs) throws ParseException
	{
		if(lhs.getNumCols() != rhs.getNumRows()) throw new ParseException("Multiply matrix,matrix: Miss match in number of dims ("+lhs.getNumCols()+","+rhs.getNumRows()+")!");
		for(int i=0;i<lhs.getNumRows();++i)
			for(int j=0;j<rhs.getNumCols();++j)
			{
				Object val = mul(lhs.getEle(i,0),rhs.getEle(0,j));
				for(int k=1;k<lhs.getNumCols();++k)
					val = add.add(val,
						mul(lhs.getEle(i,k),rhs.getEle(k,j)));
				res.setEle(i,j,val);
			}
		return res;			
	}

	/** multiply two scalers **/
	public Scaler calcValue(Scaler res,Scaler lhs,Scaler rhs) throws ParseException
	{
		res.setEle(0,super.mul(lhs.getEle(0),rhs.getEle(0)));
		return res;			
	}

	/** multiply vector and scaler **/
	public MVector calcValue(MVector res,MVector lhs,Object rhs) throws ParseException
	{
		for(int i=0;i<res.getNumEles();++i)
			res.setEle(i,super.mul(lhs.getEle(i),rhs));
		return res;			
	}

	/** multiply scaler and vector **/
	public MVector calcValue(MVector res,Object lhs,MVector rhs) throws ParseException
	{
		for(int i=0;i<res.getNumEles();++i)
			res.setEle(i,super.mul(lhs,rhs.getEle(i)));
		return res;			
	}

	/** multiply matrix and scaler **/
	public Matrix calcValue(Matrix res,Matrix lhs,Object rhs) throws ParseException
	{
		for(int i=0;i<res.getNumEles();++i)
			res.setEle(i,super.mul(lhs.getEle(i),rhs));
		return res;			
	}

	/** multiply scaler and matrix **/
	public Matrix calcValue(Matrix res,Object lhs,Matrix rhs) throws ParseException
	{
		for(int i=0;i<res.getNumEles();++i)
			res.setEle(i,super.mul(lhs,rhs.getEle(i)));
		return res;			
	}

	/** Multiply scaler and vectors. */
	public MVector mul(Object lhs,MVector rhs) throws ParseException
	{
		MVector res = new MVector(rhs.getNumEles());
		return calcValue(res,lhs,rhs);			
	}

	/** Multiply vector and scaler. */
	public MVector mul(MVector lhs,Object rhs) throws ParseException
	{
		MVector res = new MVector(lhs.getNumEles());
		return calcValue(res,lhs,rhs);			
	}

	/** Multiply scaler and matrix. */
	public Matrix mul(Object lhs,Matrix rhs) throws ParseException
	{
		Matrix res = new Matrix(rhs.getNumRows(),rhs.getNumCols());
		return calcValue(res,lhs,rhs);			
	}

	/** Multiply matrix and scaler. */
	public Matrix mul(Matrix lhs,Object rhs) throws ParseException
	{
		Matrix res = new Matrix(lhs.getNumRows(),lhs.getNumCols());
		return calcValue(res,lhs,rhs);			
	}
	
	/** Multiply matrix and vectors. */
	public MVector mul(Matrix lhs,MVector rhs) throws ParseException
	{
		MVector res = new MVector(lhs.getNumRows());
		return calcValue(res,lhs,rhs);			
	}

	/** Multiply vector and matrix. */
	public MVector mul(MVector lhs,Matrix rhs) throws ParseException
	{
		MVector res = new MVector(rhs.getNumCols());
		return calcValue(res,lhs,rhs);			
	}

	/** multiply two matricies. */
	public Matrix mul(Matrix lhs,Matrix rhs) throws ParseException
	{
		Matrix res = new Matrix(lhs.getNumRows(),rhs.getNumCols());
		return calcValue(res,lhs,rhs);			
	}
}
