/* @author rich
 * Created on 03-Aug-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.nfunk.jep;

import org.nfunk.jep.function.PostfixMathCommandI;
import org.nfunk.jep.function.*;

/**
 * This class servers two purposes. Firstly it 
 * defines all the methods and data needed to define operators like
 * ADD  "+" and GE ">=". Secondally it serves as a container
 * for a set of operators, this particular class contains all the
 * standard operators for the default functioning of JEP. Sub-classes
 * can overload the set of opperators to give them extended functionality.
 * 
 * <p>
 * Operators have a number of properties:
 * <ul>
 * <li>A symbol or name of the operator "+".
 * <li>The number of arguments NO_ARGS 0, UNARY 1 (eg UMINUS -x), 
 * BINARY 2 (eq x+y), and NARY either 3 ( a>b ? a : b) or
 * unspecified like a list [x,y,z,w].
 * <li>The binging of the operator, LEFT 1+2+3 -> (1+2)+3 or RIGHT 1=2=3 -> 1=(2=3).
 * <li>Whether the operator is ASSOCIATIVE or COMMUTATIVE.
 * <li>The precedence of the operators + has a higher precedence than *.
 * <li>For unary opperators they can either be PREFIX like -x or SUFIX like x%.
 * <li>Comparative operators can be REFLEXIVE, SYMMETRIC, TRANSITIVE or EQUIVILENCE which has all three properties.
 * <li>A reference to a PostfixtMathCommandI object which is used to evaluate an equation containing the operator.
 * </ul>
 * various is... and get... methods are provided to query the properties of the opperator.
 * 
 * <p>
 * Individual operators can be accessed in two way. Firstly
 * there are public static final fields specifict to this particular class.
 * These are OP_ADD, OP_SUBTRACT, OP_GE, etc. The can be access using
 * <pre>
 * Operator myOp = Operator.OP_ADD;
 * if(myOp == Operator.OP_ADD) System.out.println("Addition: "+myOp.toString());
 * </pre>
 * Secondly they can be accessed through the 
 * {@link org.lsmp.djep.OperatorSet org.lsmp.djep.OperatorSet}.
 * This provides a way to overide operators to provide generic properties.
 * For example the normal multiplication over the reals is commutative
 * but it is not commutative for matricies. Generic methods are provided
 * which allow the particular operator to be accessed.
 * <pre>
 * OperatorSet opSet = new OperatorSet();
 * Operator myOp = opSet.getAdd();
 * if(opSet.isAdd(myOp)) ... // is an instance of the Add operator for this set.
 * </pre> 
 * <p>   
 * @author Rich Morris
 * Created on 19-Oct-2003
 */
public class Operator {
	/** No arguments to operator */
	public static final int NO_ARGS=0;
	/** Unary operators, such as -x !x ~x */
	public static final int UNARY=1;
	/** Binary operators, such as x+y, x>y */
	public static final int BINARY=2;
	/** Trinary ops such as ?: and or higher like [x,y,z,w] */
	public static final int NARY=3;
	/** Left binding like +: 1+2+3 -> (1+2)+3 */
	public static final int LEFT=4;
	/** Right binding like =: 1=2=3 -> 1=(2=3) */
	public static final int RIGHT=8;
	/** Associative operators x*(y*z) == (x*y)*z . */
	public static final int ASSOCIATIVE=16;
	/** Commutative operators x*y = y*x. */
	public static final int COMMUTATIVE=32;
	/** Reflecive relations x=x for all x. */
	public static final int REFLEXIVE=64;
	/** Symmetric relation x=y implies y=x. */
	public static final int SYMMETRIC=128;
	/** Transative relations x=y and y=z implies x=z */
	public static final int TRANSITIVE=256;
	/** Equivilence relations = reflexive, transative and symetric. */
	public static final int EQUIVILENCE=TRANSITIVE+REFLEXIVE+SYMMETRIC;
	/** prefix operators -x **/
	public static final int PREFIX=512;
	/** postfix operators  x%, if neiter prefix and postif then infix, if both trifix like x?y:z **/
	public static final int SUFIX=1024;
	/** self inverse operators like -(-x) !(!x) **/
	public static final int SELF_INVERSE=2048;
	/** composite operators, like a-b which is a+(-b) **/
	public static final int COMPOSITE=4096;
		
	/** And array to hold all the know operators. */
	private static Operator operatorDefs[] = new Operator[20];
	/** The current position in the array of the next unfilled element. */
	private static int operatorDefsCurPos = 0;
	
	/** everyone can read but not write these operators **/
	public static final Operator OP_GT     =  new Operator(">",new Comparative(1),BINARY+LEFT+TRANSITIVE);
	public static final Operator OP_LT     =  new Operator("<",new Comparative(0),BINARY+LEFT+TRANSITIVE);
	public static final Operator OP_EQ     =  new Operator("==",new Comparative(5),BINARY+LEFT+EQUIVILENCE);
	public static final Operator OP_LE     =  new Operator("<=",new Comparative(2),BINARY+LEFT+REFLEXIVE+TRANSITIVE);
	public static final Operator OP_GE     =  new Operator(">=",new Comparative(3),BINARY+LEFT+REFLEXIVE+TRANSITIVE);
	public static final Operator OP_NE     =  new Operator("!=",new Comparative(4),BINARY+LEFT+SYMMETRIC);

	public static final Operator OP_AND    =  new Operator("&&",new Logical(0),BINARY+LEFT+COMMUTATIVE+ASSOCIATIVE);
	public static final Operator OP_OR     =  new Operator("||",new Logical(1),BINARY+LEFT+COMMUTATIVE+ASSOCIATIVE);
	public static final Operator OP_NOT    = new Operator("!",new Not(),UNARY+RIGHT+PREFIX+SELF_INVERSE);

	public static final Operator OP_ADD   =  new Operator("+",new Add(),BINARY+LEFT+COMMUTATIVE+ASSOCIATIVE);
	public static final Operator OP_SUBTRACT  =  new Operator("-",new Subtract(),BINARY+LEFT+COMPOSITE);
	public static final Operator OP_UMINUS =  new Operator("UMinus","-",new UMinus(),UNARY+RIGHT+PREFIX+SELF_INVERSE);

	public static final Operator OP_MULTIPLY    =  new Operator("*",new Multiply(),BINARY+LEFT+COMMUTATIVE+ASSOCIATIVE);
	public static final Operator OP_DIVIDE = new Operator("/",new Divide(),BINARY+LEFT+COMPOSITE);
	public static final Operator OP_MOD    = new Operator("%",new Modulus(),BINARY+LEFT);
	/** unary division i.e. 1/x or x^(-1) **/ 
	public static final Operator OP_UDIVIDE =  new Operator("UDivide","^-1",null,UNARY+RIGHT+PREFIX+SELF_INVERSE);

	public static final Operator OP_POWER  = new Operator("^",new Power(),BINARY+LEFT);

	public static final Operator OP_ASSIGN = new Operator("=",new Assign(),BINARY+RIGHT); // 
	public static final Operator OP_DOT = new Operator(".",new Dot(),BINARY+RIGHT); // 
	public static final Operator OP_CROSS = new Operator("^^",new Cross(),BINARY+RIGHT); // 
	public static final Operator OP_LIST = new Operator("LIST",new List(),NARY+RIGHT); // 
	
	/** subclasses can write these operators **/
	protected static Operator ADD = OP_ADD;
	protected static Operator SUBTRACT = OP_SUBTRACT;
	protected static Operator UMINUS = OP_UMINUS;
	protected static Operator MULTIPLY = OP_MULTIPLY;
	protected static Operator DIVIDE = OP_DIVIDE;
	protected static Operator MOD = OP_MOD;
	protected static Operator POWER = OP_POWER;
	
	protected static Operator EQ = OP_EQ;
	protected static Operator NE = OP_NE;
	protected static Operator GT = OP_GT;
	protected static Operator GE = OP_GE;
	protected static Operator LT = OP_LT;
	protected static Operator LE = OP_LE;
	protected static Operator AND = OP_AND;
	protected static Operator OR = OP_OR;
	protected static Operator NOT = OP_NOT;
	
	/** 
	 * Sets the precedences of the operators according to order in the supplied array.
	 * For example
	 * <pre>
	 * 		setPrecedenceTable(new Operator[][] 
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

	public static final void setPrecedenceTable(Operator[][] precArray)
	{
		for(int i=0;i<precArray.length;++i)
			for(int j=0;j<precArray[i].length;++j)
				precArray[i][j].setPrecedence(i);
	}
	
	/* sets the standard precedences. */
	static {
		setPrecedenceTable(new Operator[][] 
			{	{OP_UMINUS},
				{OP_NOT},
				{OP_POWER},
				{OP_MULTIPLY,OP_DIVIDE,OP_MOD,OP_DOT,OP_CROSS},
				{OP_ADD,OP_SUBTRACT},
				{OP_LT,OP_LE},
				{OP_GT,OP_GE},
				{OP_EQ},
				{OP_NE},
				{OP_AND},
				{OP_OR},
				{OP_ASSIGN},
				});
		//printOperators();

		// 		
		OP_ADD.setInverseOp(OP_UMINUS);
		OP_ADD.setBinaryInverseOp(OP_SUBTRACT);
		OP_SUBTRACT.setRootOp(OP_ADD);
		OP_SUBTRACT.setInverseOp(OP_UMINUS);
		OP_UMINUS.setRootOp(OP_ADD);
		OP_UMINUS.setBinaryInverseOp(OP_SUBTRACT);
		
		OP_MULTIPLY.setInverseOp(OP_UDIVIDE);
		OP_MULTIPLY.setBinaryInverseOp(OP_DIVIDE);
		OP_DIVIDE.setRootOp(OP_MULTIPLY);
		OP_DIVIDE.setInverseOp(OP_UDIVIDE);
		OP_UDIVIDE.setRootOp(OP_MULTIPLY);
		OP_UDIVIDE.setBinaryInverseOp(OP_DIVIDE);
		
		// Set distribuative over
		OP_UMINUS.setDistributiveOver(OP_ADD); // -(a+b) -> (-a) + (-b)
		OP_UMINUS.setDistributiveOver(OP_SUBTRACT); // -(a-b) -> (-a) - (-b)

		OP_MULTIPLY.setDistributiveOver(OP_ADD); // a*(b+c) -> a*b + a*c
		OP_MULTIPLY.setDistributiveOver(OP_SUBTRACT); // a*(b-c) -> a*b - a*c
		OP_MULTIPLY.setDistributiveOver(OP_UMINUS); // a*(-b) -> -(a*b)

		//OP_AND.setDistributiveOver(OP_OR);	// a&&(b||c) -> (a&&b)||(a&&c)
		//OP_OR.setDistributiveOver(OP_AND);	// a||(b&&c) -> (a||b)&&(a||c)
		// TODO cant use above as causes circulatity!
		// TODO set distributive of relations a>b -> a+c > b+c, 
	}
	
	public static void printOperators()
	{
		int maxPrec = -1;
		for(int i=0;i<operatorDefsCurPos;++i)
			if(operatorDefs[i].getPrecedence()>maxPrec) maxPrec=operatorDefs[i].getPrecedence();
		for(int j=-1;j<=maxPrec;++j)
			for(int i=0;i<operatorDefsCurPos;++i)
				if(operatorDefs[i].getPrecedence()==j)
					System.out.println(operatorDefs[i].toFullString());
	}
	/**
	 * Adds a new opperator. 
	 */
	private static final void addOperator(Operator op)
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
	public static final Operator[] getOperators() { return operatorDefs; }

	/** The name of the operator, found in parser. */
	private String name;
	/** The symbol name of the operator, found in parser. */
	private String symbol;
	/** Postfix mathcommand */
	private PostfixMathCommandI pfmc;
	
	/** flags for type of operator */
	private int flags;

	/** private default constructor, prevents calling with no arguments. */
	private Operator()
	{
		addOperator(this);
	}

	/** construct a new operator.
	 * 
	 * @param name	printable name of operator
	 * @param pfmc  postfix math command for opperator
	 * @param flags set of flags defining the porperties of the operator.
	 */
	public Operator(String name,PostfixMathCommandI pfmc,int flags)
	{
		this();
		this.name = name; this.pfmc = pfmc;
		this.flags = flags; 
		this.symbol = name;
	}
	/**
	 * Allows a given precedent to be set.
	 * @param name
	 * @param pfmc
	 * @param flags
	 * @param precedence
	 */
	public Operator(String name,PostfixMathCommandI pfmc,int flags,int precedence)
	{
		this(name,pfmc,flags);
		this.precedence=precedence;
		this.symbol = name;
	}
	/** construct a new operator, with a different name and symbol
	 * 
	 * @param name	name of operator, must be unique, used when describing operator
	 * @param symbol printable name of operator, used for printing equations
	 * @param pfmc  postfix math command for opperator
	 * @param flags set of flags defining the porperties of the operator.
	 */
	public Operator(String name,String symbol,PostfixMathCommandI pfmc,int flags)
	{
		this();
		this.name = name; this.pfmc = pfmc;
		this.flags = flags; 
		this.symbol = symbol;
	}
	/**
	 * Allows a given precedent to be set.
	 * @param name
	 * @param pfmc
	 * @param flags
	 * @param precedence
	 */
	public Operator(String name,String symbol,PostfixMathCommandI pfmc,int flags,int precedence)
	{
		this(name,pfmc,flags);
		this.precedence=precedence;
		this.symbol = symbol;
	}

	/** precedence of operator, 0 is most tightly bound, so prec("*") < prec("+"). */
	private int precedence = -1;
	public final int getPrecedence() {return precedence;}
	protected final void setPrecedence(int i) {precedence = i;}

	/** Operators this is distributative over **/
	private Operator distribOver[] = new Operator[0];

	protected final void setDistributiveOver(Operator op)
	{
		int len = distribOver.length;
		Operator temp[] = new Operator[len+1];
		for(int i=0;i<len;++i)	temp[i] = distribOver[i];
		temp[len]=op;
		distribOver=temp; 
	}
	public boolean isDistributiveOver(Operator op)
	{
		for(int i=0;i<distribOver.length;++i)
			if(op == distribOver[i])
				return true;
		return false;	
	}
	
	/** For composite operators there is a root operator and an inverse operator **/
	private Operator rootOperator=null;
	private Operator inverseOperator=null;
	private Operator binaryInverseOperator=null;
	protected void setRootOp(Operator root)	{rootOperator=root;}
	protected void setInverseOp(Operator inv){inverseOperator = inv;}
	protected void setBinaryInverseOp(Operator inv){binaryInverseOperator = inv;}
	public Operator getRootOp() { return rootOperator; }
	public Operator getInverseOp() { return inverseOperator; }
	public Operator getBinaryInverseOp() { return binaryInverseOperator; }

	/** 
	 * When parsing how is x+y+z interpreted.
	 * Can be Operator.LEFT x+y+z -> (x+y)+z or
	 * Operator.RIGHT x=y=z -> x=(y=z). 
	 */  
	public final int getBinding() { return (flags & (LEFT | RIGHT)); }
	public final boolean isAssociative() {return ((flags & ASSOCIATIVE) == ASSOCIATIVE);}
	public final boolean isCommutative() { return ((flags & COMMUTATIVE) == COMMUTATIVE);}
	public final boolean isBinary() {	return ((flags & 3) == BINARY);	}
	public final boolean isUnary() {	return ((flags & 3) == UNARY);	}
	public final boolean isNary() {	return ((flags & 3) == NARY);	}
	public final int numArgs() { return (flags & 3);	}
	public final boolean isTransitive() {	return ((flags & TRANSITIVE) == TRANSITIVE);	}
	public final boolean isSymmetric() {	return ((flags & SYMMETRIC) == SYMMETRIC);	}
	public final boolean isReflexive() {	return ((flags & REFLEXIVE) == REFLEXIVE);	}
	public final boolean isEquivilence() {return ((flags & EQUIVILENCE) == EQUIVILENCE);	}
	public final boolean isPrefix() {return ((flags & PREFIX) == PREFIX);	}
	public final boolean isSufix() {return ((flags & SUFIX) == SUFIX);	}
	public final boolean isComposite() {return ((flags & COMPOSITE) == COMPOSITE);	}
	public final boolean isSelfInverse() {return ((flags & SELF_INVERSE) == SELF_INVERSE);	}

	public final String getSymbol() {return symbol;}
	public final String getName() {return name;}
	public final PostfixMathCommandI getPFMC() { return pfmc;}
	public final void setPFMC(PostfixMathCommandI pfmc) { this.pfmc = pfmc;}
	/** returns a verbose representation of the operator and all its properties. **/
	public String toString() { return "Operator: \""+name+"\""; }
	
	public String toFullString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Operator: \""+symbol+"\"");
		if(!name.equals(symbol)) sb.append(" "+name);
		switch(numArgs()){
		case 0: sb.append(" no arguments,"); break;
		case 1: sb.append(" unary,"); break;
		case 2: sb.append(" binary,"); break;
		case 3: sb.append(" variable number of arguments,"); break;
		}
		if(isPrefix() && isSufix()) sb.append(" trifix,");
		else if(isPrefix()) sb.append(" prefix,");
		else if(isSufix()) sb.append(" sufix,");
		else sb.append(" infix,");
		if(getBinding()==LEFT) sb.append(" left binding,");
		else if(getBinding()==RIGHT) sb.append(" right binding,");
		if(isAssociative()) sb.append(" associative,");
		if(isCommutative()) sb.append(" commutative,");
		sb.append(" precedence "+getPrecedence()+",");
		if(isEquivilence())
			sb.append(" equivilence relation,");
		else
		{
			if(isReflexive()) sb.append(" reflexive,");
			if(isSymmetric()) sb.append(" symmetric,");
			if(isTransitive()) sb.append(" transitive,");
		}
		sb.setCharAt(sb.length()-1,'.');
		return sb.toString();
	}
}
