/* @author rich
 * Created on 16-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;

import org.lsmp.djep.xjep.XJepI;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

/**
 * This interface specifies method needed to include differentation
 * for the main JEP class. This interface allow multiple inheritance.
 * @author Rich Morris
 * Created on 16-Nov-2003
 */
public interface DJepI extends XJepI {
	public DifferentationVisitor getDV();
	public DSymbolTable getVarTab(); 

	/** Visitor of diffeentiation equations. */
	public abstract Node differentiate(Node node, String name)
		throws ParseException;
}