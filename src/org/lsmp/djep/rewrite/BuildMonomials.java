/* @author rich
 * Created on 09-Nov-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.rewrite;

import org.lsmp.djep.xjep.XJep;
import org.nfunk.jep.ASTFunNode;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

/**
 * Converts terms like 
 * @author Rich Morris
 * Created on 09-Nov-2004
 */
public class BuildMonomials extends AbstractRewrite {

	/**
	 * @param xj
	 */
	public BuildMonomials(XJep xj) {
		super(xj);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.xjep.RewriteRuleI#test(org.nfunk.jep.ASTFunNode, org.nfunk.jep.Node[])
	 */
	public boolean test(ASTFunNode node, Node[] children) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.xjep.RewriteRuleI#apply(org.nfunk.jep.ASTFunNode, org.nfunk.jep.Node[])
	 */
	public Node apply(ASTFunNode node, Node[] children) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
