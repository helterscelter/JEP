/* @author rich
 * Created on 05-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.groups;
import org.lsmp.djep.groupJep.interfaces.*;

/**
 * @author Rich Morris
 * Created on 05-Mar-2004
 */
public class Reals extends Group implements FieldI,OrderedSetI,HasPowerI {

	private Double ZERO = new Double(0.0);
	private Double ONE = new Double(1.0);

	/**
	 * Operations on the reals (Implemented as Doubles  
	 */
	public Reals() {
	}

	public Number getZERO() {
		return ZERO;
	}

	public Number getONE() {
		return ONE;
	}

	public Number getInverse(Number num) {
		return new Double(1.0/num.doubleValue());
	}

	public Number getMulInverse(Number num) {
		return new Double(1.0 / num.doubleValue());
	}

	public Number add(Number a, Number b) {
		return new Double(a.doubleValue()+b.doubleValue());
	}

	public Number sub(Number a, Number b) {
		return new Double(a.doubleValue()-b.doubleValue());
	}

	public Number mul(Number a, Number b) {
		return new Double(a.doubleValue()*b.doubleValue());
	}

	public Number div(Number a, Number b) {
		return new Double(a.doubleValue()/b.doubleValue());
	}

	public Number pow(Number a, Number b) {
		return new Double(Math.pow(a.doubleValue(),b.doubleValue()));
	}

	public Number valueOf(String str) {
		return new Double(str);
	}

	public boolean equals(Number a,Number b)	{
		return ((Double) a).compareTo((Double) b) == 0;
	}
	
	public int compare(Number a,Number b)	{
		return ((Double) a).compareTo((Double) b);
	}
}
