/* @author rich
 * Created on 07-Dec-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.groups;

import org.lsmp.djep.groupJep.interfaces.*;
import org.lsmp.djep.groupJep.values.*;
import org.nfunk.jep.type.*;
/**
 * @author Rich Morris
 * Created on 07-Dec-2004
 */
public class ExtendedFreeGroup
	extends FreeGroup
	implements HasPowerI, HasDivI, HasModI {

	public ExtendedFreeGroup(RingI K, String symbol) {
		super(K, symbol);
	}

	/** Limited implementation of power, only works with integer powers.
	 * Second argument should be an Integer.
	 */
	public Number pow(Number a, Number b) {
		FreeGroupElement exp = (FreeGroupElement) b; 
		if(!isConstantPoly(exp))
			throw new IllegalArgumentException("Powers only supported for integer exponant. Current exponant is "+exp.toString());

		Complex c = exp.getComplexValue();
		if(c.im() != 0.0)
			throw new IllegalArgumentException("Powers only supported for integer exponant. Current exponant is "+exp.toString());
		double re = c.re();
		if(Math.floor(re) != re)
			throw new IllegalArgumentException("Powers only supported for integer exponant. Current exponant is "+exp.toString());

		return ((FreeGroupElement) a).pow((int) re);
	}

	/** Division of Polynomials, discards remainder.
	 * 
	 */
	public Number div(Number a, Number b) {
		return ((FreeGroupElement) a).div((FreeGroupElement) b);
	}

	/** Division of Polynomials, discards remainder.
	 */
	public Number mod(Number a, Number b) {
		return null;
		//return ((FreeGroupElement) a).mod((FreeGroupElement) b);
	}

}
