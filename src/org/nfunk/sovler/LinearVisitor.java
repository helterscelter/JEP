package org.nfunk.sovler;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

public class LinearVisitor implements ParserVisitor
{
	public LinearVisitor()
	{
	}
	
	public static boolean isLinear(Node node)
	{
		LinearVisitor lv = new LinearVisitor();
		MyBool returnVal = ((MyBool)node.jjtAccept(lv, new MyBool(true)));
		return returnVal.getValue();		
	}
	
	public Object visit(SimpleNode node, Object data) {
		System.out.println("SimpleNode visit method");
		data = node.childrenAccept(this, data);
		return data;
	}
	
	public Object visit(ASTStart node, Object data) {
		System.out.println("ASTStart visit method");
		data = node.childrenAccept(this, data);
		return data;
	}
	
	public Object visit(ASTFunNode node, Object data) {
		
		if (node.getName() == "*" &&
			(node.jjtGetChild(0) instanceof ASTVarNode ||
			 node.jjtGetChild(1) instanceof ASTVarNode))
		{

			if (node.jjtGetChild(0) instanceof ASTVarNode)
			{
				if (!(ConstantVisitor.isConstant(node.jjtGetChild(1))))
					((MyBool)data).setValue(false);
			}
			else
			{
				if (!(ConstantVisitor.isConstant(node.jjtGetChild(0))))
					((MyBool)data).setValue(false);
			}
			
		}

		// non +, -, * operators must only involve constants
		if ((node.getName() != "+")
				&& (node.getName() != "-")
				&& (node.getName() != "*")) {
					
			int i=0;
			while (i < node.jjtGetNumChildren()) {
				if (!ConstantVisitor.isConstant(node.jjtGetChild(i))) {
					((MyBool)data).setValue(false);
					break;
				}
				i++;
			}
		}


		if (((MyBool)data).getValue())
			data = node.childrenAccept(this, data);

		return data;
	}
	
	public Object visit(ASTVarNode node, Object data) {
		return data;
	}
	
	public Object visit(ASTConstant node, Object data) {
		return data;
	}
}
