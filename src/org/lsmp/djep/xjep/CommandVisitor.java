/* @author rich
 * Created on 18-Jun-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */

package org.lsmp.djep.xjep;
//import org.lsmp.djep.matrixParser.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.PostfixMathCommandI;

/**
 * Executes commands like diff and eval embeded in expression trees.
 * For example you could do 
 * <pre>eval(diff(x^3,x),x,2)</pre>
 * to differentiate x^3 and then substitute x=2 to get the value 12. 
 * To use do
 * <pre>
 * JEP j = ...; Node in = ...;
 * TreeUtils tu = new TreeUtils(j);
 * CommandVisitor cv = new CommandVisitor(tu);
 * Node out = cv.process(in);
 * </pre>
 * Commands to be executed must implement
 * {@link org.lsmp.djep.CommandVisitorI} and {@link  org.nfunk.jep.function.PostfixMathCommandI}.
 * See {@link org.lsmp.djep.functions.Eval} for an example of this. 
 * See {@link org.nfunk.jep.ParserVisitor} for details on the VisitorPattern.
 * @author R Morris
 * Created on 19-Jun-2003
 */
public class CommandVisitor extends DoNothingVisitor
{
  private XJepI xjep;
  /** private default constructor to prevent init without a tree utils
   */
    public CommandVisitor()
  {
  }
  
  /** 
   * Decends the tree processing all diff, eval and simplify options
   */

  public Node process(Node node,XJepI xjep) throws ParseException
  {
  	this.xjep=xjep;
	Node res = (Node) nodeAccept(node,null);
	return res;
  }

  public Object visit(ASTFunNode node, Object data) throws ParseException
  {
	Node children[] = acceptChildrenAsArray(node,data);

	PostfixMathCommandI pfmc = node.getPFMC();
	if(pfmc instanceof CommandVisitorI )
	{
		CommandVisitorI com = (CommandVisitorI) pfmc;
		return com.process(node,children,xjep);
	}
	TreeUtils.copyChildrenIfNeeded(node,children);
	return node;
  }
}
