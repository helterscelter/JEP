/* @author rich
 * Created on 23-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.nfunk.jep;

/**
 * @author Rich Morris
 * Created on 23-Nov-2003
 */
public interface ParserI {
	public Node parseStream(java.io.Reader stream, JEP jep_in)
													throws ParseException;

}
