/* @author rich
 * Created on 05-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.interfaces;

/**
 * Represents a field.
 * Abelian group for + with inverse 0.
 * Elements excluding 0 form a abelian group.
 * 
 * @author Rich Morris
 * Created on 05-Mar-2004
 */
public interface FieldI extends IntegralDomainI,HasDivI {
	/** get mul inverse */
	public Number getMulInverse(Number num);
}
