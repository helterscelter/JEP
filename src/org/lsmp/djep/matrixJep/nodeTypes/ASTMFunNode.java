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
public class ASTMFunNode extends ASTFunNode implements MatrixNodeI 
{
	private MatrixValueI mvar=null;

	public ASTMFunNode(int i) {	super(i);}

	public Dimensions getDim()	{return mvar.getDim();	}

	public void setDim(Dimensions dim) {
		mvar = Tensor.getInstance(dim);
	}

	public MatrixValueI getMValue() {return mvar;}
	
}
