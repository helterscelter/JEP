/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;

import org.nfunk.jep.Variable;
import org.nfunk.jep.*;
/**
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public class PartialVariableFactory implements PartialVariableFactoryI {

	public PartialDerivative createDerivative(DVariable var, String[] dnames,Node eqn) {
		return new PartialDerivative(var,dnames,eqn);
	}

	public Variable createVariable(String name, Object value) {
		return new DVariable(name,value);
	}

	public Variable createVariable(String name) {
		return new DVariable(name);
	}

}
