/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.nfunk.jep;

import org.nfunk.jep.function.*;

/**
 * This class organizes all operator names and associated functions for the 
 * purpose of simplified creation and identification of nodes in the expression
 * tree.
 * <p>
 * <b>Note that changing the contents of the "name" array will not affect
 * the parsing of expressions, since the operator tokens for parsing are
 * defined separately in Parser.jjt.</b>
 */
public class Operators {
	public static final int LEFT =1;
	public static final int RIGHT =1;
	private static Operator operatorDefs[] = new Operator[20];
	private static int operatorDefsCurPos = 0;
	
	public static class Operator {

		/** is opperator associative? */
		boolean associative = false;
		/** is opperator commutative? */
		boolean commutative = false;
		/** precedence of operator, 0 is most tightly bound, so prec("*") < prec("+"). */
		int precedence = -1;
		/** When parsing is it LEFT, RIGHT. */  
		int binding = LEFT;
		/** The name of the operator, found in parser. */
		String name;
		/** Is it a binary operator. */
		boolean binary=true;
		/** Postfix mathcommand */
		PostfixMathCommandI pfmc;
		
		// TODO might want to add symetric, reflective and transative properties for comparision operators.

		private Operator()
		{
			addOperator(this);
		}
		/**
		 * Standard constructor ignores commutitivity etc.
		 */
		public Operator(String name,PostfixMathCommandI pfmc)
		{
			this();
			this.name = name; this.pfmc = pfmc; 
		}
		
		/**
		 * Allows the commutativity, associativity and binging to be set.
		 * @param name name used when printing.
		 * @param pfmc, associated postfix math command.
		 * @param associative, is operator associative 
		 * @param commutative, is operator commutative
		 * @param binding either LEFT or RIGHT
		 */
		public Operator(String name,PostfixMathCommandI pfmc,
			boolean commutative,boolean associative,int binding)
		{
			this();
			this.name = name; this.pfmc = pfmc; 
			this.associative = associative;
			this.commutative = commutative;
			this.binding = binding;
		}

		/**
		 * Allows binary operator field to be set.
		 * By default all operators are binary, so this method is most
		 * usful for unary operators ! and - .
		 * @param binary - set to false for unary operators.
		 */
		public Operator(String name,PostfixMathCommandI pfmc,
			boolean commutative,boolean associative,int binding,boolean binary)
		{
			this();
			this.name = name; this.pfmc = pfmc; 
			this.associative = associative;
			this.commutative = commutative;
			this.binding = binding;
			this.binary = binary;
		}
		public String getName() {return name;}
		public void setName(String string) {name = string;}

		public int getPrecedence() {return precedence;}
		public void setPrecedence(int i) {precedence = i;}

		public boolean isAssociative() {return associative;}
		public void setAssociative(boolean b) {associative = b;}

		public boolean isCommutative() { return commutative;}
		public void setCommutative(boolean b) {commutative = b;}

		public PostfixMathCommandI getPfmc() { return pfmc;}
		public void setPfmc(PostfixMathCommandI pfmc) {this.pfmc = pfmc;}

		public boolean isBinary() {	return binary;	}
		public void setBinary(boolean b) {binary = b; }

		/** When parsing how is x+y+z interpreted.
		 * Can be Operators.LEFT x+y+z -> (x+y)+z or
		 * Operator.RIGHT x=y=z -> x=(y=z). 
		 */  
		public int getBinding() { return binding; }
		public void setBinding(int i) {	binding = i; }
	}

	public static final Operator OP_GT     =  new Operator(">",new Comparative(1),false,false,LEFT);
	public static final Operator OP_LT     =  new Operator("<",new Comparative(0),false,false,LEFT);
	public static final Operator OP_EQ     =  new Operator("==",new Comparative(5),false,false,LEFT);
	public static final Operator OP_LE     =  new Operator("<=",new Comparative(2),false,false,LEFT);
	public static final Operator OP_GE     =  new Operator(">=",new Comparative(3),false,false,LEFT);
	public static final Operator OP_NE     =  new Operator("!=",new Comparative(4),false,false,LEFT);

	public static final Operator OP_AND    =  new Operator("&&",new Logical(0),true,true,LEFT);
	public static final Operator OP_OR     =  new Operator("||",new Logical(1),true,true,LEFT);
	public static final Operator OP_NOT    = new Operator("!",new Not(),false,false,RIGHT,false);

	public static final Operator OP_PLUS   =  new Operator("+",new Add(),true,true,LEFT);
	public static final Operator OP_MINUS  =  new Operator("-",new Subtract(),false,false,LEFT);
	public static final Operator OP_UMINUS =  new Operator("-",new UMinus(),false,false,RIGHT,false);

	public static final Operator OP_MUL    =  new Operator("*",new Multiply(),true,true,LEFT);
	public static final Operator OP_DIV    = new Operator("/",new Divide(),false,false,LEFT);
	public static final Operator OP_MOD    = new Operator("%",new Modulus(),false,false,LEFT);

	public static final Operator OP_POWER  = new Operator("^",new Power(),false,false,LEFT);
	
	/** 
	 * Sets the precedences of the operators according to order in the supplied array.
	 * For example
	 * <pre>
	 * 		setPrecedences(new Operator[][] 
	 *		{	{OP_UMINUS},
	 *			{OP_NOT},
	 *			{OP_MUL,OP_DIV,OP_MOD},
	 *			{OP_PLUS,OP_MINUS},
	 *			{OP_LT,OP_LE},
	 *			{OP_GT,OP_GE},
	 *			{OP_EQ},
	 *			{OP_NE},
	 *			{OP_AND},
	 *			{OP_OR},
	 *			});
	 * </pre>
	 */

	public static void setPrecedences(Operator[][] precArray)
	{
		for(int i=0;i<precArray.length;++i)
			for(int j=0;j<precArray[i].length;++j)
				precArray[i][j].setPrecedence(i);
	}
	
	/* sets the standard precedences. */
	{
		setPrecedences(new Operator[][] 
			{	{OP_UMINUS},
				{OP_NOT},
				{OP_MUL,OP_DIV,OP_MOD},
				{OP_PLUS,OP_MINUS},
				{OP_LT,OP_LE},
				{OP_GT,OP_GE},
				{OP_EQ},
				{OP_NE},
				{OP_AND},
				{OP_OR},
				});
	}
	
	/**
	 * Adds a new opperator. 
	 */
	protected static void addOperator(Operator op)
	{
		if(operatorDefsCurPos >= operatorDefs.length)
		{
			Operator[] tmp = new Operator[operatorDefs.length*2];
			for(int i=0;i<operatorDefs.length;++i)
				System.arraycopy(operatorDefs,0,tmp,0,operatorDefs.length);
				operatorDefs = tmp;
		}
		operatorDefs[operatorDefsCurPos++] = op;
	}

	/** Gets the list of operators. */	
	public static Operator[] getOperators() { return operatorDefs; }
}