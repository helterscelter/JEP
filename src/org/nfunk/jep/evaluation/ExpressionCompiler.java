/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/


package org.nfunk.jep.evaluation;

import org.nfunk.jep.*;
import java.util.*;


public class ExpressionCompiler implements ParserVisitor {
	/** Commands */
	private Vector commands;
		
	public ExpressionCompiler() {
		commands = new Vector();
	}
	
	public CommandElement[] compile(Node node) {
		commands.removeAllElements();
		node.jjtAccept(this, null);
		CommandElement[] temp = new CommandElement[commands.size()];
		Enumeration enum = commands.elements();
		int i = 0;
		while (enum.hasMoreElements()) {
			 temp[i++] = (CommandElement)enum.nextElement();
		}
		return temp;
	}

	public Object visit(ASTFunNode node, Object data) {
		node.childrenAccept(this,data);
		
		CommandElement c = new CommandElement();
		c.setType(CommandElement.FUNC);
		c.setPFMC(node.getPFMC());
		c.setNumParam(node.jjtGetNumChildren());
		commands.addElement(c);
		
		return data;
	}

	public Object visit(ASTVarNode node, Object data) {
		CommandElement c = new CommandElement();
		c.setType(CommandElement.VAR);
		c.setVarName(node.getName());
		commands.addElement(c);

		return data;
	}

	public Object visit(ASTConstant node, Object data) {
		CommandElement c = new CommandElement();
		c.setType(CommandElement.CONST);
		c.setValue(node.getValue());
		commands.addElement(c);

		return data;
	}
	
	public Object visit(SimpleNode node, Object data) {
		return data;
	}
	
	public Object visit(ASTStart node, Object data) {
		return data;
	}
}
