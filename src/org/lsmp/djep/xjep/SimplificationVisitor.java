   
package org.lsmp.djep.xjep;
//import org.lsmp.djep.matrixParser.*;
import org.nfunk.jep.*;

/**
 * Simplifies an expression.
 * To use
 * <pre>
 * JEP j = ...; Node in = ...;
 * TreeUtils tu = new TreeUtils(j);
 * SimplificationVisitor sv = new SimplificationVisitor(tu);
 * Node out = sv.simplify(in);
 * </pre>
 * 
 * <p>
 * Its intended to completly rewrite this class to that simplification
 * rules can be specified by strings in a way similar to DiffRulesI.
 * It also would be nice to change the rules depending on the type of
 * arguments, for example matrix multiplication is not commutative.
 * But some of the in built rules exploit commutativity.
 * 
 * @author Rich Morris
 * Created on 20-Jun-2003
 */

public class SimplificationVisitor extends DoNothingVisitor
{
  private NodeFactory nf;
  private OperatorSet opSet;
  
  public SimplificationVisitor()
  {
  }

  /** must be implemented for subclasses. **/
  public Node simplify(Node node,XJepI xjep) throws ParseException,IllegalArgumentException
  {
	nf = xjep.getNodeFactory();
	opSet = xjep.getOperatorSet();
	if (node == null) 
		throw new IllegalArgumentException(
			"topNode parameter is null");
	Node res = (Node) nodeAccept(node,null);
	return res;
  }

	public Node simplifyBuiltOperatorNode(Operator op,Node lhs,Node rhs) throws ParseException
	{
		ASTFunNode res = nf.buildOperatorNode(op,lhs,rhs);
		Node res2 = simplifyOp(res,new Node[]{lhs,rhs});
		return res2;
	}
 	/**
 	 * Simplifies expresions like 2+(3+x) or (2+x)+3
 	 * 
 	 * @param node
 	 * @param dimKids
 	 * @return null if no rewrite happens or top node or top node of new tree.
 	 * @throws ParseException
 	 */
	public Node simplifyTripple(Operator op,Node lhs,Node rhs) throws ParseException
	{
		
		Operator rootOp;
		if(op.isComposite()) rootOp = op.getRootOp();
		else				 rootOp = op;

		if(op.isCommutative() && TreeUtils.isConstant(rhs))
		{
			return simplifyBuiltOperatorNode(op,rhs,lhs);
		}			
		if(TreeUtils.isConstant(lhs) && TreeUtils.isBinaryOperator(rhs))
		{
			Node rhsChild1 = rhs.jjtGetChild(0);
			Node rhsChild2 = rhs.jjtGetChild(1);
			Operator rhsOp = ((ASTFunNode) rhs).getOperator();
			Operator rhsRoot;
			if(rhsOp.isComposite())	rhsRoot = rhsOp.getRootOp();
			else					rhsRoot = rhsOp;
	
			if(TreeUtils.isConstant(rhsChild1))	
			{
				Operator op2 = rootOp;
				if(op == rhsOp) op2 = rootOp;
				else			op2 = rootOp.getBinaryInverseOp();

				//	2 + ~( 3 + ~x ) -> (2+~3) + ~~x
				if(rootOp == rhsRoot && rootOp.isAssociative()) 
				{
					Node newnode = simplifyBuiltOperatorNode(op2,
						nf.buildConstantNode(op,lhs,rhsChild1),rhsChild2);
					return newnode;
				}
			
				if(op.isDistributiveOver(rhsRoot))	// 2 * (3 + ~x) -> (2 * 3) + ~(2 @ x)
				{
					Node newnode = simplifyBuiltOperatorNode(rhsOp,
						nf.buildConstantNode(op,lhs,rhsChild1),
						simplifyBuiltOperatorNode(op,lhs,rhsChild2));
					return newnode;
				}
			}


			if(TreeUtils.isConstant(rhsChild2))	
			{
				// 2 + ~( x + ~3 ) -> (2 + ~~3) + ~x

				Operator op2 = rootOp;
				if(op == rhsOp) op2 = rootOp;
				else			op2 = rootOp.getBinaryInverseOp();

				if(rootOp == rhsRoot && rootOp.isCommutative() && rootOp.isAssociative())
				{
					Node newnode = simplifyBuiltOperatorNode(op,
						nf.buildConstantNode(op2,lhs,rhsChild2),rhsChild1);
					return newnode;
				}
			
				if(op.isDistributiveOver(rhsRoot))	// 2 * (x + ~3) -> (2 * x) + ~(2 * 3)
				{
					Node newnode = simplifyBuiltOperatorNode(rhsOp,
						simplifyBuiltOperatorNode(op,lhs,rhsChild1),
						nf.buildConstantNode(op,lhs,rhsChild2));
					return newnode;
				}
			}
		}

		if(TreeUtils.isBinaryOperator(lhs) && TreeUtils.isConstant(rhs))
		{
			Node lhsChild1 = lhs.jjtGetChild(0);
			Node lhsChild2 = lhs.jjtGetChild(1);
			Operator lhsOp = ((ASTFunNode) lhs).getOperator();
			Operator lhsRoot;
			if(lhsOp.isComposite())	lhsRoot = lhsOp.getRootOp();
			else					lhsRoot = lhsOp;
	
			if(TreeUtils.isConstant(lhsChild1))	
			{
				// (2 + ~x) + ~3    ->   (2 + ~3) + ~x
				if(rootOp == lhsRoot && rootOp.isAssociative() && rootOp.isCommutative())
				{
					Node newnode = simplifyBuiltOperatorNode(lhsOp,
						nf.buildConstantNode(op,lhsChild1,rhs),
						lhsChild2);
					return newnode;
				}
			
				// (2 + ~x) * 3    -->  (2*3) +~ (x*3)
				if(op.isDistributiveOver(lhsRoot)) 
				{
					Node newnode = simplifyBuiltOperatorNode(lhsOp,
						nf.buildConstantNode(op,lhsChild1,rhs),
						simplifyBuiltOperatorNode(op,lhsChild2,rhs));
					return newnode;
				}
			}


			if(TreeUtils.isConstant(lhsChild2))	
			{
				// (x + ~2) + !3 -> x + (~2 + !3) -> x + ~(2+~!3)
				// (x*2)*3 -> x*(2*3), (x/2)*3 -> x/(2/3)
				// (x*2)/3 -> x*(2/3), (x/2)/3 -> x/(2*3) 
				if(rootOp == lhsRoot && rootOp.isAssociative())
				{
					Operator op2 = rootOp;
					if(op == lhsOp) op2 = rootOp;
					else			op2 = rootOp.getBinaryInverseOp();
					
					Node newnode = simplifyBuiltOperatorNode(lhsOp,
						lhsChild1,
						nf.buildConstantNode(op2,lhsChild2,rhs));
					return newnode;
				}
			
				// (x + ~2) * 3 -> (x*3) + ~(2*3)
				if(op.isDistributiveOver(lhsRoot))
				{
					Node newnode = simplifyBuiltOperatorNode(lhsOp,
						simplifyBuiltOperatorNode(op,lhsChild1,rhs),
						nf.buildConstantNode(op,lhsChild2,rhs));
					return newnode;
				}
			}
		}
		return null;
	}

  /**
   * Simplifies an addition. Performs the following rules
   * <pre>
   * 0+x -> x
   * x+0 -> x
   * m+n -> (m+n) where m,n are numbers
   * x - (-2) -> x + 2 for any negative number -2
   * x + (-2) -> x - 2 for any negative number -2
   * 2 +/- ( 3 +/- x ) ->  (2 +/- 3 ) +/- x and similar
   * </pre>
   * @param dimKids an array of the simplified children of this node
   */
  
  public Node simplifyAdd(Node lhs,Node rhs) throws ParseException
  {
	if(TreeUtils.isInfinity(lhs))
	{	// Inf + Inf -> NaN TODO not correct for signed infinity 
		if(TreeUtils.isInfinity(rhs))
			return nf.buildConstantNode(Double.NaN);
		else	// Inf + x -> Inf
			return nf.buildConstantNode(Double.POSITIVE_INFINITY);
	}
	if(TreeUtils.isInfinity(rhs)) // x + Inf -> Inf
		return nf.buildConstantNode(Double.POSITIVE_INFINITY);
	  
	if(TreeUtils.isZero(lhs))	// 0+x -> x
		return rhs;
	if(TreeUtils.isZero(rhs))	// x + 0 -> x
		return lhs;

	if(TreeUtils.isNegative(lhs)) // -3 + x -> x - 3
	{
		Node newnode = nf.buildOperatorNode(opSet.getSubtract(),
			rhs,
			nf.buildConstantNode(-TreeUtils.doubleValue(lhs)));
		return newnode;
	}
	if(TreeUtils.isNegative(rhs)) // x + -3 -> x - 3
	{
		Node newnode = nf.buildOperatorNode(opSet.getSubtract(),
			lhs,
			nf.buildConstantNode(-TreeUtils.doubleValue(rhs)));
		return newnode;
	}
	return null;
//	return nf.buildOperatorNode(node.getOperator(),lhs,dimKids[1]);
//	return opSet.buildAddNode(lhs,dimKids[1]);
  }

  /**
   * Simplifies a subtraction. Performs the following rules
   * <pre>
   * 0-x -> 0-x
   * x-0 -> x
   * m-n -> (m-n) where m,n are numbers
   * x - (-2) -> x + 2 for any negative number -2
   * x + (-2) -> x - 2 for any negative number -2
   * 2 +/- ( 3 +/- x ) ->  (2 +/- 3 ) +/- x and similar
   * </pre>
   * @param dimKids an array of the simplified children of this node
   */
  
  public Node simplifySubtract(Node lhs,Node rhs) throws ParseException
  {
	if(TreeUtils.isInfinity(lhs))
	{	// Inf + Inf -> NaN TODO not correct for signed infinity 
		if(TreeUtils.isInfinity(rhs))
			return nf.buildConstantNode(Double.NaN);
		else	// Inf + x -> Inf
			return nf.buildConstantNode(Double.POSITIVE_INFINITY);
	}
	if(TreeUtils.isInfinity(rhs)) // x + Inf -> Inf
		return nf.buildConstantNode(Double.POSITIVE_INFINITY);

	if(TreeUtils.isZero(rhs))	// x - 0 -> x
		return lhs;
	// TODO implement 0 - x -> -(x)
	
	if(TreeUtils.isNegative(rhs)) // x - (-2) -> x + 2
	{
		Node newnode = simplifyBuiltOperatorNode(opSet.getAdd(),
			lhs,
			nf.buildConstantNode(-TreeUtils.doubleValue(rhs)));
		return newnode;
	}
	return null;
//	return nf.buildOperatorNode(((ASTOpNode) node).getOperator(),lhs,rhs);
//	return tu.buildSubtract(lhs,rhs);
  }
  
  /**
   * Simplifies a multiplication.
   * <pre>
   * 0 * Inf -> NaN
   * 0 * x -> 0
   * x * 0 -> 0
   * 1 * x -> x
   * x * 1 -> x
   * Inf * x -> Inf
   * x * Inf -> Inf
   * 2 * ( 3 * x) -> (2*3) * x
   * and similar.
   * </pre>
   */
  
  public Node simplifyMultiply(Node child1,Node child2) throws ParseException
  {
	if(TreeUtils.isZero(child1))
	{	// 0*Inf -> NaN 
		if(TreeUtils.isInfinity(child2))
			return nf.buildConstantNode(Double.NaN);
		else // 0*x -> 0
			return nf.buildConstantNode(TreeUtils.ZERO);
	}
	if(TreeUtils.isZero(child2))
	{ // Inf*0 -> NaN
		if(TreeUtils.isInfinity(child1))
			return nf.buildConstantNode(Double.NaN);
		else // 0 * x -> 0
			return nf.buildConstantNode(TreeUtils.ZERO);
	}
	if(TreeUtils.isInfinity(child1)) // Inf * x -> Inf
			return nf.buildConstantNode(Double.POSITIVE_INFINITY);
	if(TreeUtils.isInfinity(child2)) // x * Inf -> Inf
			return nf.buildConstantNode(Double.POSITIVE_INFINITY);
	  			  
	if(TreeUtils.isOne(child1))	// 1*x -> x
			  return child2;
	if(TreeUtils.isOne(child2))	// x*1 -> x
			  return child1;
	
	if(TreeUtils.isMinusOne(child1))	// -1*x -> -x
	{
		Node newnode = nf.buildOperatorNode(opSet.getUMinus(),child2);
		return newnode;
	}

	if(TreeUtils.isMinusOne(child2))	// x*-1 -> -x
	{
		Node newnode = nf.buildOperatorNode(opSet.getUMinus(),child1);
		return newnode;
	}
	return null;
//	return nf.buildOperatorNode(((ASTOpNode) node).getOperator(),child1,child2);
//  return tu.buildMultiply(child1,child2);
	}
	/**
	 * Simplifies a division.
	 * <pre>
	 * 0/0 -> NaN
	 * 0/Inf -> Inf
	 * 0/x -> Inf
	 * x/0 -> Inf
	 * x/1 -> x
	 * Inf / x -> Inf
	 * x / Inf -> 0
	 * 2 / ( 3 * x) -> (2/3) / x
	 * 2 / ( x * 3) -> (2/3) / x
	 * 2 / ( 3 / x) -> (2/3) * x
	 * 2 / ( x / 3) -> (2*3) / x
	 * (2 * x) / 3 -> (2/3) * x
	 * (x * 2) / 3 -> x * (2/3)
	 * (2 / x) / 3 -> (2/3) / x
	 * (x / 2) / 3 -> x / (2*3)
	 * </pre>
	 */
	public Node simplifyDivide(Node child1,Node child2) throws ParseException
	{
	  if(TreeUtils.isZero(child2))
	  {
		if(TreeUtils.isZero(child1))	// 0/0 -> NaN
			return nf.buildConstantNode(Double.NaN);
		else	// x/0 -> Inf
			return nf.buildConstantNode(Double.POSITIVE_INFINITY);
	  }
		  
	  if(TreeUtils.isZero(child1))
	  {		// 0/x -> 0
		return child1;
	  }
	  //if(TreeUtils.isOne(child1))	// 1/x -> 1/x
	  //		  return child2;
	  if(TreeUtils.isOne(child2))	// x/1 -> x
			  return child1;
			
	  if(TreeUtils.isInfinity(child1)) // Inf / x -> Inf
			  return nf.buildConstantNode(Double.POSITIVE_INFINITY);
	  if(TreeUtils.isInfinity(child2)) // x / Inf -> 0
			  return nf.buildConstantNode(TreeUtils.ZERO);
  	  return null;
//	  return nf.buildOperatorNode(((ASTOpNode) node).getOperator(),child1,child2);
//	  return opSet.buildDivideNode(child1,child2);
	}

	/** Simplify a power.
	 * <pre>
	 * x^0 -> 1
	 * x^1 -> x
	 * 0^0 -> NaN
	 * 0^x -> 0
	 * 1^x -> 1
	 * </pre>
	 */
	public Node simplifyPower(Node child1,Node child2) throws ParseException
	{
		if(TreeUtils.isZero(child1))
		{
			if(TreeUtils.isZero(child2))	// 0^0 -> NaN
				return nf.buildConstantNode(Double.NaN);
			else	// 0^x -> 0
				return nf.buildConstantNode(TreeUtils.ZERO);
		}
		if(TreeUtils.isZero(child2))	// x^0 -> 1
			return nf.buildConstantNode(TreeUtils.ONE);
		if(TreeUtils.isOne(child1))	// 1^x -> 1
			return nf.buildConstantNode(TreeUtils.ONE);
		if(TreeUtils.isOne(child2))	// x^1 -> x
			return child1;
		return null;	
//		return nf.buildOperatorNode(((ASTOpNode) node).getOperator(),child1,child2);
//		return tu.buildPower(child1,child2);
	}

	/** simplifies operators, does not decend into children */

	public Node simplifyOp(ASTFunNode node,Node children[]) throws ParseException
	{
		boolean allConst=true;
		Operator op=node.getOperator();
		int nchild=children.length;
		for(int i=0;i<nchild;++i)
		{
			if(!TreeUtils.isConstant(children[i]))
				allConst=false;
			if(TreeUtils.isNaN(children[i]))
				return nf.buildConstantNode(Double.NaN);
		}	
		if(allConst)
			return nf.buildConstantNode(op,children);
		
		if(nchild==1)
		{
			if(TreeUtils.isUnaryOperator(children[0]) && op == TreeUtils.getOperator(children[0]))
			{
				if(op.isSelfInverse()) return children[0].jjtGetChild(0);
			}
		}
		if(nchild==2)
		{
			Node res=null;
			if(opSet.isAdd(op)) res = simplifyAdd(children[0],children[1]);
			if(opSet.isSubtract(op)) res = simplifySubtract(children[0],children[1]);
			if(opSet.isMultiply(op)) res = simplifyMultiply(children[0],children[1]);
			if(opSet.isDivide(op)) res = simplifyDivide(children[0],children[1]);
			if(opSet.isPower(op)) res = simplifyPower(children[0],children[1]);
			if(res!=null)
			{
				if(TreeUtils.isConstant(res)) return res;
				if(TreeUtils.isOperator(res))
				{
					Node res2 = simplifyOp((ASTFunNode) res,TreeUtils.getChildrenAsArray(res));
					return res2;
				} 
				return res;
			}
			res = this.simplifyTripple(op,children[0],children[1]);
			if(res!=null)
			{
				if(TreeUtils.isConstant(res)) return res;
				if(TreeUtils.isOperator(res))
				{
					Node res2 = simplifyOp((ASTFunNode) res,TreeUtils.getChildrenAsArray(res));
					return res2;
				} 
				return res;
			}
		}
		return node;
	}
	
	public Object visit(ASTFunNode node, Object data) throws ParseException
	{
		int nchild = node.jjtGetNumChildren();

		if(node.isOperator())
		{
			Operator op=node.getOperator();
			if( (op.isBinary() && nchild !=2)
			 || (op.isUnary() && nchild !=1))
			 throw new ParseException("Wrong number of children for "+nchild+" for operator "+op.getName());
	
			Node children[] = acceptChildrenAsArray(node,data);
			TreeUtils.copyChildrenIfNeeded(node,children);
	
			Node res = simplifyOp(node,children);
			if(res == null)
				throw new ParseException("null res from simp op");
			return res;
		}		
		else
		{
			Node children[] = acceptChildrenAsArray(node,data);

			boolean allConst=true;
			for(int i=0;i<nchild;++i)
			{
				if(!TreeUtils.isConstant(children[i]))
					allConst=false;
				if(TreeUtils.isNaN(children[i]))
					return nf.buildConstantNode(Double.NaN);
			}	
			if(allConst)
				return nf.buildConstantNode(node,children);
		
			return TreeUtils.copyChildrenIfNeeded(node,children);
		}
	}
}
