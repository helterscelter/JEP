/* @author rich
 * Created on 27-Jul-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.matrixJep;
import org.lsmp.djep.vectorJep.function.*;
import org.lsmp.djep.xjep.*;
//import org.nfunk.jep.*;	// Want to make sure we never use standard JEP operators
import org.nfunk.jep.Operator;

/**
 * @author Rich Morris
 * Created on 27-Jul-2003
 */
public class MatrixOperatorSet extends OperatorSet {
	public static final Operator M_ADD   =  new Operator("{Matrix+}","+",new MAdd(),Operator.BINARY+Operator.LEFT+Operator.COMMUTATIVE+Operator.ASSOCIATIVE);
	public static final Operator M_SUBTRACT  =  new Operator("{Matrix-}","-",new MSubtract(),Operator.BINARY+Operator.LEFT);
	public static final Operator M_UMINUS =  new Operator("{Matrix Uminus}","-",new MUMinus(),Operator.UNARY+Operator.RIGHT+Operator.PREFIX);
	public static final Operator M_MULTIPLY    =  new Operator("{Matrix*}","*",new MMultiply(),Operator.BINARY+Operator.LEFT+Operator.ASSOCIATIVE);
	/** An overloaded ^ operator, can either be power or vector product. */
	public static final Operator M_CROSS  = new Operator("{Matrix^}","^",new ExteriorProduct(),Operator.BINARY+Operator.LEFT);
	public static final Operator OP_ASSIGN = new Operator("=",new Assignment(),Operator.BINARY+Operator.LEFT);
	public static final Operator M_DOT = new Operator(".",new Dot(),Operator.BINARY+Operator.LEFT);

	//public static final Operator LIST = new Operator("LIST",new MList(),Operator.NARY);
	public static final Operator TENSOR = new Operator("TENSOR",new TensorFun(),Operator.NARY);
	
	/** place holder for DOT operation, can be overwritten. */
	protected Operator DOT;
	/** place holder for Assignment operation, can be overwritten. */
	protected Operator ASSIGN;
	/** place holder for Hat operation, can be overwritten. */
	protected Operator CROSS;

	//private matDJep jep;
	public MatrixOperatorSet(MatrixDJep jep)
	{
		super(jep);
		//this.jep=jep;
		ADD = M_ADD;
		SUBTRACT = M_SUBTRACT;
		UMINUS = M_UMINUS;
		MULTIPLY = M_MULTIPLY;
		CROSS = M_CROSS;
		DOT = M_DOT;
		ASSIGN = OP_ASSIGN;
		Operator.setPrecedenceTable(new Operator[][]
			{
			{Operator.OP_UMINUS, M_UMINUS},
			{Operator.OP_NOT},
			{Operator.OP_POWER, M_CROSS},
			{Operator.OP_MULTIPLY,Operator.OP_DIVIDE,Operator.OP_MOD, M_MULTIPLY, M_DOT},
			{Operator.OP_ADD,Operator.OP_SUBTRACT, M_ADD, M_SUBTRACT},
			{Operator.OP_LT,Operator.OP_LE},
			{Operator.OP_GT,Operator.OP_GE},
			{Operator.OP_EQ},
			{Operator.OP_NE},
			{Operator.OP_AND},
			{Operator.OP_OR},
			{OP_ASSIGN},
			});
	}
	public Operator getAssign() {return ASSIGN;}
	public Operator getDot() {return DOT;	}
	public Operator getCross() {return CROSS;	}
	//public Operator getList() {return LIST;	}



}
