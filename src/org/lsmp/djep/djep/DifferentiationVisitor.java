/* @author rich
 * Created on 18-Jun-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
   
package org.lsmp.djep.djep;
import org.lsmp.djep.djep.diffRules.*;
import org.lsmp.djep.xjep.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.PrintStream;

/**
 * A class for perfoming differentation of an expression.
 * To use do
 * <pre>
 * JEP j = ...; Node in = ...;
 * DifferentiationVisitor dv = new DifferentiationVisitor(jep);
 * dv.addStandardDiffRules();
 * Node out = dv.differentiate(in,"x");
 * </pre>
 * The class follows the visitor pattern described in
 * {@link org.nfunk.jep.ParserVisitor ParserVisitor}.
 * The rules for differentiating specific functions are contained in
 * object which implement
 * {@link DiffRulesI DiffRulesI}
 * A number of inner classes which use this interface are defined for specific
 * function types.
 * In particular
 * {@link MacroDiffRules MacroDiffRules}
 * allow the rule for differentiation to be specified by strings.
 * New rules can be added using
 * {@link DJep#addDiffRule} method.
 * @author R Morris
 * Created on 19-Jun-2003
 */
public class DifferentiationVisitor extends DeepCopyVisitor
{
	private static final boolean DEBUG = false; 
	private DJep localDJep;
	private DJep globalDJep;
	private NodeFactory nf;
	private TreeUtils tu;
//	private OperatorSet opSet;
  /**
   * Construction with a given set of tree utilities 
   * @param tu
   */
  public DifferentiationVisitor(DJep jep)
  {
	this.globalDJep = jep;
	
	addDiffRule(new AdditionDiffRule("+"));
	addDiffRule(new SubtractDiffRule("-"));
	addDiffRule(new MultiplyDiffRule("*"));
	addDiffRule(new DivideDiffRule("/"));
	addDiffRule(new PowerDiffRule("^"));
	addDiffRule(new PassThroughDiffRule("UMinus",globalDJep.getOperatorSet().getUMinus().getPFMC()));

  }
  
  
   /** 
   * Adds the standard set of differentation rules. 
   * Corresponds to all standard functions in the JEP plus a few more.
   * <pre>
   * sin,cos,tan,asin,acos,atan,sinh,cosh,tanh,asinh,acosh,atanh
   * sqrt,log,ln,abs,angle
   * sum,im,re are handled seperatly.
   * rand and mod currently unhandled
   * 
   * Also adds rules for functions not in JEP function list:
   * 	sec,cosec,cot,exp,pow,sgn 
   * 
   * TODO include if, min, max, sgn
   * </pre>
   * @return false on error
   */
  boolean addStandardDiffRules()
  {
  	try
  	{
  		addDiffRule(new MacroDiffRules(globalDJep,"sin","cos(x)"));
		addDiffRule(new MacroDiffRules(globalDJep,"cos","-sin(x)")); 	
		addDiffRule(new MacroDiffRules(globalDJep,"tan","1/((cos(x))^2)"));

		MacroFunction sec = new MacroFunction("sec",1,"1/cos(x)",globalDJep);
		globalDJep.addFunction("sec",sec);
		MacroFunction cosec = new MacroFunction("cosec",1,"1/sin(x)",globalDJep);
		globalDJep.addFunction("cosec",cosec);
		MacroFunction cot = new MacroFunction("cot",1,"1/tan(x)",globalDJep);
		globalDJep.addFunction("cot",cot);
		
		addDiffRule(new MacroDiffRules(globalDJep,"sec","sec(x) * tan(x)"));
		addDiffRule(new MacroDiffRules(globalDJep,"cosec","-cosec(x) * cot(x)"));
		addDiffRule(new MacroDiffRules(globalDJep,"cot","-(cosec(x))^2"));
			
		addDiffRule(new MacroDiffRules(globalDJep,"asin","1/(sqrt(1-x^2))"));
		addDiffRule(new MacroDiffRules(globalDJep,"acos","-1/(sqrt(1-x^2))"));
		addDiffRule(new MacroDiffRules(globalDJep,"atan","1/(1+x^2)"));

		addDiffRule(new MacroDiffRules(globalDJep,"sinh","cosh(x)"));
		addDiffRule(new MacroDiffRules(globalDJep,"cosh","sinh(x)"));
		addDiffRule(new MacroDiffRules(globalDJep,"tanh","1-(tanh(x))^2"));

		addDiffRule(new MacroDiffRules(globalDJep,"asinh","1/(sqrt(1+x^2))"));
		addDiffRule(new MacroDiffRules(globalDJep,"acosh","1/(sqrt(x^2-1))"));
		addDiffRule(new MacroDiffRules(globalDJep,"atanh","1/(1-x^2)"));

		addDiffRule(new MacroDiffRules(globalDJep,"sqrt","1/(2 (sqrt(x)))"));
		
		globalDJep.addFunction("exp",new Exp());
		addDiffRule(new MacroDiffRules(globalDJep,"exp","exp(x)"));
//		globalDJep.addFunction("pow",new Pow());
//		addDiffRule(new MacroDiffRules(globalDJep,"pow","y*(pow(x,y-1))","(ln(x)) (pow(x,y))"));
		addDiffRule(new MacroDiffRules(globalDJep,"ln","1/x"));
		addDiffRule(new MacroDiffRules(globalDJep,"log",	// -> (1/ln(10)) /x = log(e) / x but don't know if e exists
			globalDJep.getNodeFactory().buildOperatorNode(globalDJep.getOperatorSet().getDivide(),
				globalDJep.getNodeFactory().buildConstantNode(
					globalDJep.getTreeUtils().getNumber(1/Math.log(10.0))),
				globalDJep.getNodeFactory().buildVariableNode(globalDJep.getSymbolTable().makeVarIfNeeded("x")))));
		// TODO problems here with using a global variable (x) in an essentially local context
		addDiffRule(new MacroDiffRules(globalDJep,"abs","abs(x)/x"));
		addDiffRule(new MacroDiffRules(globalDJep,"angle","y/(x^2+y^2)","-x/(x^2+y^2)"));
		addDiffRule(new MacroDiffRules(globalDJep,"mod","1","0"));
		addDiffRule(new PassThroughDiffRule(globalDJep,"sum"));
		addDiffRule(new PassThroughDiffRule(globalDJep,"re"));
		addDiffRule(new PassThroughDiffRule(globalDJep,"im"));
		addDiffRule(new PassThroughDiffRule(globalDJep,"rand"));
		
		MacroFunction complex = new MacroFunction("macrocomplex",2,"x+i*y",globalDJep);
		globalDJep.addFunction("macrocomplex",complex);
		addDiffRule(new MacroFunctionDiffRules(globalDJep,complex));
		
/*		addDiffRule(new PassThroughDiffRule("\"<\"",globalDJep.getOperatorSet().getLT().getPFMC()));
		addDiffRule(new PassThroughDiffRule("\">\"",new Comparative(1)));
		addDiffRule(new PassThroughDiffRule("\"<=\"",new Comparative(2)));
		addDiffRule(new PassThroughDiffRule("\">=\"",new Comparative(3)));
		addDiffRule(new PassThroughDiffRule("\"!=\"",new Comparative(4)));
		addDiffRule(new PassThroughDiffRule("\"==\"",new Comparative(5)));
*/		
//		addDiffRule(new DiffDiffRule(this,"diff"));
		// TODO do we want to add eval here?
//		addDiffRule(new EvalDiffRule(this,"eval",eval));
		
		//addDiffRule(new PassThroughDiffRule("\"&&\""));
		//addDiffRule(new PassThroughDiffRule("\"||\""));
		//addDiffRule(new PassThroughDiffRule("\"!\""));
		
		// also consider if, min, max, sgn, dot, cross, 
		//addDiffRule(new MacroDiffRules(this,"sgn","0"));
		return true;
  	}
  	catch(ParseException e)
  	{
  		System.err.println(e.getMessage());
  		return false;
  	}
  }
  
  /** The set of all differentation rules indexed by name of function. */ 
  Hashtable diffRules = new Hashtable();
  /** Adds the rules for a given function. */
  void addDiffRule(DiffRulesI rule)
  {
	diffRules.put(rule.getName(),rule);
	if(DEBUG) System.out.println("Adding rule for "+rule.getName());
  }
  /** finds the rule for function with given name. */
  DiffRulesI getDiffRule(String name)
  {
	return (DiffRulesI) diffRules.get(name);
  }
  
  /**
   * Prints all the differentation rules for all functions on System.out.
   */
  public void printDiffRules() { printDiffRules(System.out); }
  
  /**
   * Prints all the differentation rules for all functions on specifed stream.
   */
  public void printDiffRules(PrintStream out)
  {
	out.println("Standard Functions and their derivatives");
	for(Enumeration enume = globalDJep.getFunctionTable().keys(); enume.hasMoreElements();)
	{
		String key = (String) enume.nextElement();
		PostfixMathCommandI value = (PostfixMathCommandI) globalDJep.getFunctionTable().get(key);
		DiffRulesI rule = (DiffRulesI) diffRules.get(key);
		if(rule==null)
			out.print(key+" No diff rules specified ("+value.getNumberOfParameters()+" arguments).");
		else
			out.print(rule.toString());
		out.println();
	}
	for(Enumeration enume = diffRules.keys(); enume.hasMoreElements();)
		{
			String key = (String) enume.nextElement();
			DiffRulesI rule = (DiffRulesI) diffRules.get(key);
			if(!globalDJep.getFunctionTable().containsKey(key))
			{
				out.print(rule.toString());
				out.println("\tnot in JEP function list");
			}
		}
	}

	/**
	 * Differentiates an expression tree wrt a variable var.
	 * @param node the top node of the expresion tree
	 * @param var the variable to differentiate wrt
	 * @return the top node of the differentiated expression 
	 * @throws ParseException if some error occured while trying to differentiate, for instance of no rule supplied for given function.
	 * @throws IllegalArgumentException
	 */
	public Node differentiate(Node node,String var,DJep djep) throws ParseException,IllegalArgumentException
	{
	  this.localDJep = djep;
	  this.nf=djep.getNodeFactory();
	  this.tu=djep.getTreeUtils();
	  //this.opSet=djep.getOperatorSet();
	  
	  if (node == null)
		  throw new IllegalArgumentException("node parameter is null");
	  if (var == null)
		  throw new IllegalArgumentException("var parameter is null");

	  Node res = (Node) node.jjtAccept(this,var);
	  return res;
	}

	/********** Now the recursive calls to differentiate the tree ************/

	/**
	 * Applies differentiation to a function.
	 * Used the rules specified by objects of type {@link DiffRulesI}.
	 * @param node The node of the function.
	 * @param data The variable to differentiate wrt.
	 **/

	public Object visit(ASTFunNode node, Object data) throws ParseException
	{
		String name = node.getName();

	   //System.out.println("FUN: "+ node + " nchild "+nchild);
		Node children[] = TreeUtils.getChildrenAsArray(node);
		Node dchildren[] = acceptChildrenAsArray(node,data);

		if(node.getPFMC() instanceof DiffRulesI)
		{
			 return ((DiffRulesI) node.getPFMC()).differentiate(node,(String) data,children,dchildren,localDJep);
		}
		DiffRulesI rules = (DiffRulesI) diffRules.get(name);
		if(rules != null)
		return rules.differentiate(node,(String) data,children,dchildren,localDJep);

		throw new ParseException("Sorry I don't know how to differentiate "+node+"\n");
	}

	 /**
	  * Differentiates a variable. 
	  * May want to alter behaviour when using multi equation as diff(f,x)
	  * might not be zero.
	  * @return 1 if the variable has the same name as data
	  * @return 0 if the variable has a different name.
	  */
	 public Object visit(ASTVarNode node, Object data) throws ParseException {
	   String varName = (String) data;
	   Variable var = node.getVar();
	   if(var instanceof DVariable)
	   {
		DVariable difvar = (DVariable) var;
		if(varName.equals(var.getName()))
			return nf.buildConstantNode(tu.getONE());
		else if(difvar.hasEquation())
		{
			PartialDerivative deriv = difvar.findDerivative((String) data,localDJep);
			return nf.buildVariableNode(deriv);
		}
		else
			return nf.buildConstantNode(tu.getZERO());
	   }
	   if(var instanceof PartialDerivative)
	   {
			PartialDerivative pvar = (PartialDerivative) var;
			DVariable dvar = pvar.getRoot();
			PartialDerivative deriv = dvar.findDerivative(pvar,varName,localDJep);
			return nf.buildVariableNode(deriv);
	   }
	   throw new ParseException("Encountered non differentiable variable");
	 }

	 /**
	  * Differentiates a constant.
	  * @return 0 direvatives of constants are always zero.
	  */
	 public Object visit(ASTConstant node, Object data) throws ParseException {
		return nf.buildConstantNode(tu.getZERO());
	 }
}

/*end*/
