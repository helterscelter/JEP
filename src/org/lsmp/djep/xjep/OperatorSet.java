/* @author rich
 * Created on 26-Jul-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

/**
 * Holds information about a set of operators.
 * By default objects of this particular class
 * contain the standard operators defined over the real or complex numbers.
 * 
 * <p>
 * This class can be extended to provide a different set of
 * operators. For example a diffent form of * is required for matricies
 * where the operator is not commutative.
 *  
 * @author Rich Morris
 * Created on 26-Jul-2003
 */
public class OperatorSet {
	protected Operator ADD;
	protected Operator SUBTRACT;
	protected Operator UMINUS;
	protected Operator MULTIPLY;
	protected Operator DIVIDE;
	protected Operator MOD;
	protected Operator POWER;
	
	protected Operator EQ;
	protected Operator NE;
	protected Operator GT;
	protected Operator GE;
	protected Operator LT;
	protected Operator LE;

	protected Operator AND;
	protected Operator OR;
	protected Operator NOT;

	protected Operator ASSIGN;
	
	protected NaturalLogarithm LN;
	protected Sum SUM;
	
	public OperatorSet(JEP jep)
	{
		this(jep.getFunctionTable());		
	}

	public OperatorSet(FunctionTable funTab)
	{
		ADD = Operator.OP_ADD;
		SUBTRACT = Operator.OP_SUBTRACT;
		UMINUS = Operator.OP_UMINUS;
		MULTIPLY = Operator.OP_MULTIPLY;
		DIVIDE = Operator.OP_DIVIDE;
		MOD = Operator.OP_MOD;
		POWER = Operator.OP_POWER;
		
		LT = Operator.OP_LT;
		GT = Operator.OP_GT;
		LE = Operator.OP_LE;
		GE = Operator.OP_GE;
		NE = Operator.OP_NE;
		EQ = Operator.OP_EQ;

		AND = Operator.OP_AND;
		OR = Operator.OP_OR;
		NOT = Operator.OP_NOT;
		ASSIGN = Operator.OP_ASSIGN;
		LN = (NaturalLogarithm) funTab.get("ln");
		SUM = (Sum) funTab.get("sum");
	}
	
	public Operator getAdd() {return ADD;	}
	public Operator getSubtract() {return SUBTRACT;	}
	public Operator getUMinus() {return UMINUS;	}
	public Operator getMultiply() {return MULTIPLY;	}
	public Operator getDivide() {return DIVIDE;	}
	public Operator getMod() {return MOD;	}
	public Operator getPower() {return POWER;	}

	public Operator getEQ() {return EQ;	}
	public Operator getNE() {return NE;	}
	public Operator getGE() {return GE;	}
	public Operator getGT() {return GT;	}
	public Operator getLE() {return LE;	}
	public Operator getLT() {return LT;	}

	public Operator getAnd() {	return AND;	}
	public Operator getOr() {return OR;	}
	public Operator getNot() {return NOT;	}
	public Operator getAssign() {return ASSIGN;	}

	public NaturalLogarithm getLn() { return LN; }
	public Sum getSum() { return SUM; }
	
	public boolean isAdd(Operator op) { return op == ADD;	}
	public boolean isSubtract(Operator op) { return op == SUBTRACT;	}
	public boolean isUMinus(Operator op) { return op == UMINUS;	}
	public boolean isMultiply(Operator op) { return op == MULTIPLY;	}
	public boolean isDivide(Operator op) { return op == DIVIDE;	}
	public boolean isMod(Operator op) { return op == MOD;	}
	public boolean isPower(Operator op) { return op == POWER;	}

	public boolean isEQ(Operator op) { return op == EQ;	}
	public boolean isNE(Operator op) { return op == NE;	}
	public boolean isGE(Operator op) { return op == GE;	}
	public boolean isGT(Operator op) { return op == GT;	}
	public boolean isLE(Operator op) { return op == LE;	}
	public boolean isLT(Operator op) { return op == LT;	}

	public boolean isAnd(Operator op) { return op == AND;	}
	public boolean isOr(Operator op) { return op == OR;	}
	public boolean isNot(Operator op) { return op == NOT;	}
}
