/* @author rich
 * Created on 05-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.interfaces;

/**
 * An IntergralDomainI which also has a notion of division,
 * which is not necessarily closed i.e. the integers.
 * 
 * @author Rich Morris
 * Created on 05-Mar-2004
 */
public interface HasDivI {
	/** get division of two numbers. i.e. a * ( b^-1).
	 * Strictly speeking  */
	public Number div(Number a,Number b);
}
