/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.vectorJep;

import org.nfunk.jep.*;
import org.lsmp.djep.vectorJep.function.*;
import org.lsmp.djep.vectorJep.values.*;
import java.util.*;

/**
 * An extension of JEP with support for basic vectors and matricies.
 * Use this class instead of JEP if you wish to use vectors and matricies.
 *  
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public class VectorJep extends JEP {

	
	public VectorJep() {
		super();
		
/*		Operator.OP_ADD.setPFMC(new MAdd());
		Operator.OP_SUBTRACT.setPFMC(new MSubtract());
		Operator.OP_MULTIPLY.setPFMC(new MMultiply());
		Operator.OP_POWER.setPFMC(new MPower());
		Operator.OP_UMINUS.setPFMC(new MUMinus());
		Operator.OP_DOT.setPFMC(new MDot());
		Operator.OP_CROSS.setPFMC(new ExteriorProduct());
		Operator.OP_LIST.setPFMC(new VList());
*/
		opSet = new VOperatorSet();
		this.parser.setInitialTokenManagerState(Parser.NO_DOT_IN_IDENTIFIERS);
	}

	public void addStandardFunctions()
	{
		super.addStandardFunctions();
		super.addFunction("ele",new Ele());
	}


	public VectorJep(JEP j) {
		super(j);
	}

	/** Evaluate a node. If the result is a scaler it
	 * will be unwrapped, i.e. it will return a Double and not a Scaler.
	 */
	public Object evaluate(Node node) throws Exception
	{
		Object res = ev.getValue(node,new Vector(),this.getSymbolTable());
		if(res instanceof Scaler)
			return ((Scaler) res).getEle(0);
		else 
			return res;
	}

	/** Evaluate a node. Does not unwrap scalers. */
	public Object evaluateRaw(Node node) throws Exception
	{
		Object res = ev.getValue(node,new Vector(),this.getSymbolTable());
		return res;
	}

}
