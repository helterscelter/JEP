/* @author rich
 * Created on 11-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.values;
import org.nfunk.jep.type.*;

/**
 * Groups which have a natural conversion to complex numbers.
 * 
 * @author Rich Morris
 * Created on 11-Mar-2004
 */
public interface HasComplexValueI {
	public Complex getComplexValue();
}
