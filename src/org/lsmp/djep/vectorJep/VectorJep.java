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
		
		opSet = new VOperatorSet();
		this.ev = new VectorEvaluator();
		this.parser.setInitialTokenManagerState(Parser.NO_DOT_IN_IDENTIFIERS);
	}

	public void addStandardFunctions()
	{
		super.addStandardFunctions();
		super.addFunction("ele",new Ele());
		super.addFunction("len",new Length());
		super.addFunction("size",new Size());
		super.addFunction("id",new Id());
		super.addFunction("diag",new Diagonal());
		super.addFunction("getdiag",new GetDiagonal());
		super.addFunction("trans",new Transpose());
		super.addFunction("det",new Determinant());
		super.addFunction("trace",new Trace());
		super.addFunction("vsum",new VSum());
		super.addFunction("Map",new VMap());
		super.addFunction("GenMat",new GenMat());
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

	/**	When set the multiplication of vectors and matricies will be element by element.
	 * Otherwise multiplication will be matrix multiplication (the default).
	 */
	public void setElementMultiply(boolean value) {
		((VOperatorSet) opSet).setElementMultiply(value);
	}

}
