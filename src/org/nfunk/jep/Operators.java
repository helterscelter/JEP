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
	
	public static final int OP_GT     =  0;
	public static final int OP_LT     =  1;
	public static final int OP_EQ     =  2;
	public static final int OP_LE     =  3;
	public static final int OP_GE     =  4;
	public static final int OP_NE     =  5;
	public static final int OP_AND    =  6;
	public static final int OP_OR     =  7;
	public static final int OP_PLUS   =  8;
	public static final int OP_MINUS  =  9;
	public static final int OP_UMINUS = 10;
	public static final int OP_MUL    = 11;
	public static final int OP_DIV    = 12;
	public static final int OP_MOD    = 13;
	public static final int OP_NOT    = 14;
	public static final int OP_POWER  = 15;
	
	/**
	 * Names for each operator (useful when printing expressions)
	 */
	public static final String[] name = {
		">",
		"<",
		"==",
		"<=",
		">=",
		"!=",
		"&&",
		"||",
		"+",
		"-",
		"-",
		"*",
		"/",
		"%",
		"!",
		"^"
	};
	
	
	/**
	 * Postfix math commands for each operator
	 */
	public static PostfixMathCommandI[] pfmc = {
		new Comparative(1),
		new Comparative(0),
		new Comparative(5),
		new Comparative(2),
		new Comparative(3),
		new Comparative(4),
		new Logical(0),
		new Logical(1),
		new Add(),
		new Subtract(),
		new UMinus(),
		new Multiply(),
		new Divide(),
		new Modulus(),
		new Not(),
		new Power()
	};
	
	/**
	 * Returns true if the id is within the boundaries of valid id numbers.
	 * <code>id>=0 && id<name.length</code>
	 */
	public static boolean isValidID(int id) {
		return (id>=0 && id<name.length);
	}
}