/* @author rich
 * Created on 09-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.values;

import org.lsmp.djep.groupJep.groups.FreeGroup;
import org.nfunk.jep.type.*;

/**
 * An element of a free group with one generator.
 *
 * @see org.lsmp.djep.groupJep.groups.FreeGroup
 * @author Rich Morris
 * Created on 09-Mar-2004
 */
public class FreeGroupElement extends Polynomial implements HasComplexValueI {

	FreeGroup group;

	/**
	 * An element of a free group with one generator.
	 * @param the free group.
	 * @param coeffs array of coeficients for this element c0 + c1 t + ... + cn t^n 
	 */
	public FreeGroupElement(FreeGroup K, Number coeffs[]) {
		super(K.getBaseRing(),K.getSymbol(),coeffs);
		this.group = K;
	}

//	public AlgebraicNumber(RingI baseRing,Polynomial poly,Number coeffs[]) {
//		this(new AlgebraicExtension(baseRing,poly),coeffs);
//	}

	/** sub classes should overright this to make the correct type. */
	protected Polynomial valueOf(Number lcoeffs[])
	{
		FreeGroupElement g = new FreeGroupElement(group,lcoeffs);
		return g;
	}

	/** Returns an aproximation to the complex number representing this algebraic number. 
	 * This only gives meaningful results if setRootValue has been called
	 * or if it is a quadratic extension (t^2+b t+c) or if it is a simple n-th root (t^n+a).
	 * In the last two cases the root value is calculated automatically. 
	 * @return Complex(Nan) if I dont know how to calculate it. */

	public Complex getComplexValue() {
			return calculateComplexValue((Complex) group.getRootVal());
	}
}
