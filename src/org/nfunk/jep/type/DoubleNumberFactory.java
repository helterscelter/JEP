/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.nfunk.jep.type;

/**
 * Default class for creating number objects. This class can be replaced by
 * other NumberFactory implementations if other number types are required. This
 * can be done using the 
 */
public class DoubleNumberFactory implements NumberFactory {
	
	/**
	 * Creates a Double object initialized to the value of the parameter.
	 *
	 * @param value The initialization value for the returned object.
	 */
	public Object createNumber(double value) {
		return new Double(value);
	}
}
