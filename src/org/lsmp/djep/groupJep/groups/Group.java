/* @author rich
 * Created on 06-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.groups;
import org.lsmp.djep.groupJep.*;
import org.nfunk.jep.type.*;
import org.nfunk.jep.*;
/**
 * Base abstract class for all groups.
 * 
 * @author Rich Morris
 * Created on 06-Mar-2004
 */
public abstract class Group implements GroupI {
	/**
	* Creates a default NumberFactory which calls
	* the {@link org.lsmp.djep.groupJep.GroupI#valueOf} method of the subclass
	* to create strings from numbers.
	*/
	private NumberFactory NumFac = new NumberFactory()	{
		public Object createNumber(String s) {
			return valueOf(s);
		}};
		
	/** returns a number factory for creating group elements from strings */
	public NumberFactory getNumberFactory() { return NumFac; }

	/** adds the standard constants for this group.
	 * By default does nothing. */
	public void addStandardConstants(JEP j) {}
	
	/** adds the standard function for this group 
	* By default does nothing. */
	public void addStandardFunctions(JEP j) {}
	
	public String toString()
	{
		return "general group";
	}
}
