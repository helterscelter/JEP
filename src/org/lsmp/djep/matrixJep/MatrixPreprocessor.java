/* @author rich
 * Created on 30-Oct-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import org.lsmp.djep.djep.*;
import org.lsmp.djep.xjep.*;
import org.lsmp.djep.matrixJep.nodeTypes.*;
import org.lsmp.djep.vectorJep.*;
import org.lsmp.djep.vectorJep.function.*;
/**
 * This visitor does the majority of preprocessing work.
 * Specifically it
 * <ul>
 * <li>Sets the dimension of each node.
 * <li>For assignment equations it adds an entry in the VariableTable
 * <li>For diff opperator it calculates the derivative.
 * <li>For the List opperator it finds the dimensions and
 * returns a ASTTensor.
 * <li>For the Hat opperator it finds the dimension and returns
 * a Power or Wedge opperator.
 * </ul>
 * The visitor will return a new Tree.
 * 
 * @author Rich Morris
 * Created on 30-Oct-2003
 */
public class MatrixPreprocessor implements ParserVisitor
{
	private MatrixJep mjep;
	private MatrixNodeFactory nf;
	private MatrixOperatorSet opSet;
	private DSymbolTable vt;

	public MatrixPreprocessor() {}

	/**
	 * Main entry point: preprocess a node. 
	 * @param node	Top node of tree. 
	 * @param mdjep	Reference to MatrixJep instance
	 * @return	A new tree with all preprocessing carried out.
	 * @throws ParseException
	 */	
	public MatrixNodeI preprocess(Node node,MatrixJep mdjep) throws ParseException
	{
		this.mjep=mdjep;
		this.nf=(MatrixNodeFactory) mdjep.getNodeFactory();
		this.vt=(DSymbolTable) mdjep.getSymbolTable();
		this.opSet=(MatrixOperatorSet) mdjep.getOperatorSet();
		return (MatrixNodeI) node.jjtAccept(this,null);
	}
	
	/**
	 * Returns an array of matrix nodes which are the results of visiting each child.
	 */
	public MatrixNodeI[] visitChildrenAsArray(Node node,Object data) throws ParseException 
	{
		int nchild = node.jjtGetNumChildren();
		MatrixNodeI children[] = new MatrixNodeI[nchild];
		for(int i=0;i<nchild;++i)
		{
//		  System.out.println("vcaa "+i+" "+node.jjtGetChild(i));
		  MatrixNodeI no = (MatrixNodeI) node.jjtGetChild(i).jjtAccept(this,data);
//		  System.out.println("vcaa "+i+" "+node.jjtGetChild(i)+" done "+no);
		  children[i] = (MatrixNodeI) no;
		}
		return children;
	}
	
	////////////////////////////////////////////////////////////////////
	
	public Object visit(SimpleNode node, Object data)	{ return null;	}
	public Object visit(ASTStart node, Object data)	{ return null;	}

	/** constants **/
	public Object visit(ASTConstant node, Object data) throws ParseException
	{
		return nf.buildConstantNode(node.getValue());
	}
	/** multidimensions differentiable variables */
	public Object visit(ASTVarNode node, Object data) throws ParseException
	{
		return nf.buildVariableNode(vt.getVar(node.getName()));
	}

	/** visit functions and operators **/
	public Object visit(ASTFunNode node, Object data) throws ParseException
	{
		if(node.isOperator()) return visitOp(node,data);
		if(node.getPFMC() instanceof Diff) return visitDiff(node,data);
		if(node.getPFMC() instanceof CommandVisitorI)
				throw new IllegalArgumentException("MatrixPreprocessor: encounterd and instance of CommandVisitorI  for function "+node.getName());
		MatrixNodeI children[] = visitChildrenAsArray(node,data);
		ASTMFunNode res = (ASTMFunNode) nf.buildFunctionNode(node,children);
		return res;
	}

	/** operators +,-,*,/ **/
	public Object visitOp(ASTFunNode node, Object data) throws ParseException
	{
		PostfixMathCommandI pfmc=node.getPFMC();
//		if(pfmc instanceof SpecialPreProcessI)
//		{
//			SpecialPreProcessI spp = (SpecialPreProcessI) node.getPFMC();
//			return spp.preprocess(node,this,mjep);
//		}
		MatrixNodeI children[] = visitChildrenAsArray(node,data);

		if(pfmc instanceof Assign)
		{
			if(node.jjtGetNumChildren()!=2) throw new ParseException("Operator "+node.getOperator().getName()+" must have two elements, it has "+children.length);
			Dimensions rhsDim = children[1].getDim();
			MatrixVariable var = (MatrixVariable) ((ASTVarNode) children[0]).getVar();
			var.setDimensions(rhsDim);
			Node copy =mjep.deepCopy(children[1]);
			Node simp = mjep.simplify(copy);
			//Node preproc = (Node) simp.jjtAccept(this,data);
			var.setEquation(simp);
			return (ASTMFunNode) nf.buildOperatorNode(node.getOperator(),children,rhsDim);
		}
		else if(pfmc instanceof Power || pfmc instanceof MPower)
		{
			if(node.jjtGetNumChildren()!=2) throw new ParseException("Operator "+node.getOperator().getName()+" must have two elements, it has "+children.length);
			Dimensions lhsDim = children[0].getDim();
			Dimensions rhsDim = children[1].getDim();
			if(rhsDim.equals(Dimensions.ONE))
			{
				Dimensions dim = lhsDim; 
				return (ASTMFunNode) nf.buildOperatorNode(
						node.getOperator(),children,dim);
			}
			else
			{
				Operator op = opSet.getCross();
				PostfixMathCommandI pfmc2 = op.getPFMC();
				BinaryOperatorI bin = (BinaryOperatorI) pfmc2;
				Dimensions dim = bin.calcDim(lhsDim,rhsDim);
				return (ASTMFunNode) nf.buildOperatorNode(op,children,dim);
			}
		}
		else if(pfmc instanceof List)
		{
			// What if we have x=[1,2]; y = [x,x]; or z=[[1,2],x];
			// first check if all arguments are TENSORS
			boolean flag=true;
			for(int i=0;i<children.length;++i)
			{
				if(children[i] instanceof ASTMFunNode)
				{
					if(((ASTMFunNode) children[i]).getOperator() != opSet.getMList())
					{
						flag=false; break;
					}
				}
				else
					flag=false; break;
			}

			if(flag)
			{
				ASTMFunNode opNode1 = (ASTMFunNode) children[0];
				Dimensions dim = Dimensions.valueOf(children.length,opNode1.getDim());
				ASTMFunNode res = (ASTMFunNode) nf.buildUnfinishedOperatorNode(opSet.getMList());
				int k=0;
				res.setDim(dim);
				res.jjtOpen();
				for(int i=0;i<children.length;++i)
				{
					ASTMFunNode opNode = (ASTMFunNode) children[i];
					for(int j=0;j<opNode.jjtGetNumChildren();++j)
					{
						Node child = opNode.jjtGetChild(j);
						res.jjtAddChild(child,k++);
						child.jjtSetParent(res);
					}
				}
				res.jjtClose();
				return res;
			}
			else
			{
				MatrixNodeI node1 = (MatrixNodeI) children[0];
				Dimensions dim = Dimensions.valueOf(children.length,node1.getDim());
				ASTMFunNode res = (ASTMFunNode) nf.buildOperatorNode(opSet.getMList(),children,dim);
				return res;
			}
		}
		else if(pfmc instanceof BinaryOperatorI)
		{
			if(node.jjtGetNumChildren()!=2) throw new ParseException("Operator "+node.getOperator().getName()+" must have two elements, it has "+children.length);
			BinaryOperatorI bin = (BinaryOperatorI) pfmc;
			Dimensions dim = bin.calcDim(children[0].getDim(),children[1].getDim());
			return (ASTMFunNode) nf.buildOperatorNode(node.getOperator(),children,dim);
		}
		else if(pfmc instanceof UnaryOperatorI)
		{
			if(children.length!=1) throw new ParseException("Operator "+node.getOperator().getName()+" must have one elements, it has "+children.length);
			UnaryOperatorI uni = (UnaryOperatorI) pfmc;
			Dimensions dim = uni.calcDim(children[0].getDim());
			return (ASTMFunNode) nf.buildOperatorNode(node.getOperator(),children,dim);
		}
		else if(pfmc instanceof NaryOperatorI)
		{
			Dimensions dims[] = new Dimensions[children.length];
			for(int i=0;i<children.length;++i)
				dims[i]=((MatrixNodeI) children[i]).getDim();
			NaryOperatorI uni = (NaryOperatorI) pfmc;
			Dimensions dim = uni.calcDim(dims);
			return (ASTMFunNode) nf.buildOperatorNode(node.getOperator(),children,dim);
		}
		else
		{
			//throw new ParseException("Operator must be unary or binary. It is "+op);
			Dimensions dim = Dimensions.ONE;
			return (ASTMFunNode) nf.buildOperatorNode(node.getOperator(),children,dim);
		}
	}

	/** the differential opperator 
	 * TODO move into an MDiff function
	 * **/

 	public Object visitDiff(ASTFunNode node, Object data) throws ParseException
	{
		MatrixNodeI children[] = visitChildrenAsArray(node,data);
		if(children.length != 2)
			throw new ParseException("Diff opperator should have two children, it has "+children.length);
		// TODO need to handle diff(x,[x,y])
		if(!(children[1] instanceof ASTMVarNode))
			throw new ParseException("rhs of diff opperator should be a variable.");
		ASTMVarNode varNode = (ASTMVarNode) children[1];
		Node diff = mjep.differentiate(children[0],varNode.getName());
		return diff;
	}
}
