/* @author rich
 * Created on 28-Feb-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;
import org.nfunk.jep.*;

/**
 * A VariableFactory which creates XVariables (which have equations).
 * 
 * @author Rich Morris
 * Created on 28-Feb-2004
 */
public class XVariableFactory extends VariableFactory {

	public Variable createVariable(String name, Object value) {
		return new XVariable(name,value);
	}

	public Variable createVariable(String name) {
		return new XVariable(name);
	}
}
