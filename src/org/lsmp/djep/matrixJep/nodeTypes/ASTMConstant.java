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
 * Holds a single constant number.
 * @author Rich Morris
 * Created on 01-Feb-2004
 */
public class ASTMConstant extends ASTConstant implements MatrixNodeI 
{
	private Scaler scalerval;

	public ASTMConstant(int i)
	{
		super(i);
		scalerval = new Scaler();
	}
	public Dimensions getDim() {
		return Dimensions.ONE;
	}

	public MatrixValueI getMValue() {
		return scalerval;
	}

	public Object getValue() {
		return scalerval.getEle(0);
	}

	public void setValue(Object val) {
		scalerval.setEle(0,val);
	}
	
}
