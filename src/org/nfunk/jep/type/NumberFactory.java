/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.nfunk.jep.type;

/**
 * This interface can be implemented to create numbers of any object type.
 * By implementing this interface and calling the setNumberFactory() method of
 * the JEP class, the constants in an expression will be created with that
 * class.
 */
public interface NumberFactory {
	
	/**
	 * Creates a number object and initializes its value.
	 * @param value The initial value of the number as a string.
	 */
	public Object createNumber(String value);
}
