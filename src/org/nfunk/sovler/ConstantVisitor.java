package org.nfunk.sovler;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

public class ConstantVisitor implements ParserVisitor
{
	public ConstantVisitor()
	{
	}
	
	public static boolean isConstant(Node node)
	{
		ConstantVisitor cv = new ConstantVisitor();
		MyBool returnVal = (MyBool)node.jjtAccept(cv, new MyBool(true));
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
		data = node.childrenAccept(this, data);
		return data;
	}
	
	public Object visit(ASTVarNode node, Object data) {
		if (data == null)
		{
			System.out.println("data is null");
			return null;
		}
		((MyBool)data).setValue(false);
		return data;
	}
	
	public Object visit(ASTConstant node, Object data) {
		return data;
	}
}
