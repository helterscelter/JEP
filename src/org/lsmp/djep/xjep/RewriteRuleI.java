/* @author rich
 * Created on 01-Oct-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;
import org.nfunk.jep.*;
/**
 * @author Rich Morris
 * Created on 01-Oct-2004
 */
public interface RewriteRuleI {
	public boolean test(ASTFunNode node,Node children[]);
	public Node apply(ASTFunNode node,Node children[]) throws ParseException;
}
