/* @author rich
 * Created on 16-Dec-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.sjep;
import org.lsmp.djep.xjep.*;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

/**
 * @author Rich Morris
 * Created on 16-Dec-2004
 */
public class POperator extends AbstractPNode {
	XOperator op;
	PNodeI args[];
	public POperator(PolynomialCreator pc,XOperator op,PNodeI[] args) {
		super(pc);
		this.op = op;
		this.args = args;
	}
	
	public boolean equals(PNodeI node)
	{
		if(!(node instanceof POperator)) return false;
		POperator op = (POperator) node;
		if(!this.op.equals(op)) return false;
		if(args.length != op.args.length)
			return false;
		for(int i=0;i<args.length;++i)
			if(!args[i].equals(op.args[i])) return false;
		return true;
	}

	/**
	this < arg ---> -1
	this > arg ---> 1
	*/
	public int compareTo(POperator fun)
	{
		int res = op.getName().compareTo(op.getName());
		if(res != 0) return res;
		
		if(args.length < fun.args.length) return -1;
		if(args.length > fun.args.length) return 1;
		
		for(int i=0;i<args.length;++i)
		{
			res = args[i].compareTo(fun.args[i]);
			if(res != 0) return res;
		}
		return 0;
	}


	public String toString()
	{
		if(args.length == 1)
		{
			if(op.isPrefix())
			return  "("+op.getSymbol() + args[0].toString()+")"; 
		}
		if(args.length == 2)
		{
			return "(("+args[0].toString()+")"+op.getSymbol()+"("+args[1].toString()+"))";
		}
		//TODO
		return super.toString();
	}
	
	public Node toNode() throws ParseException
	{
		Node funargs[] = new Node[args.length];
		for(int i=0;i<args.length;++i)
			funargs[i] = args[i].toNode();
		Node fun = pc.nf.buildOperatorNode(op,funargs);
		return fun;
	}

	public PNodeI expand() throws ParseException	{ 
		PNodeI newTerms[] = new PNodeI[args.length];
		for(int i=0;i<args.length;++i)
			newTerms[i] = args[i].expand();
		return new POperator(pc,op,newTerms);		
	}
}
