/* @author rich
 * Created on 19-Dec-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.nfunk.jep;

/**
 * @author Rich Morris
 * Created on 19-Dec-2003
 */
public class VariableFactory implements VariableFactoryI 
{
	public Variable createVariable(String name,Object value)
	{
		return new Variable(name,value);
	}

	public Variable createVariable(String name)
	{
		return new Variable(name);
	}
}
