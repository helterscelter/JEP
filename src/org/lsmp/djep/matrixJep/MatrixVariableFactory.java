/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;

import org.lsmp.djep.djep.*;
import org.nfunk.jep.Node;
import org.nfunk.jep.Variable;

/**
 * Allows creation of matrix aware variables.
 * 
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public class MatrixVariableFactory extends DVariableFactory {

	/** create a derivative */
	public PartialDerivative createDerivative(DVariable var,String[] dnames,Node eqn) {
		return null;
	}

	/** Create a variable with a given value. */
	public Variable createVariable(String name, Object value) {
		return new MatrixVariable(name,value);
	}

	/** Create a variable with a given value. */
	public Variable createVariable(String name) {
		return new MatrixVariable(name);
	}

}
