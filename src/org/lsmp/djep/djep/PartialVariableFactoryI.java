/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;

import org.nfunk.jep.VariableFactoryI;
import org.nfunk.jep.*;
/**
 * An interface specifying the method needed to create a a PartialDerivative.
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public interface PartialVariableFactoryI extends VariableFactoryI {
	public PartialDerivative createDerivative(DVariable var,String dnames[],Node eqn);
}
