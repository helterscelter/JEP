/* @author rich
 * Created on 26-Feb-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;
import org.nfunk.jep.*;
import org.lsmp.djep.xjep.PrintVisitor;

/**
 * An extension of PrintVisitor which will print the equations of a variable if required.
 * The behavious of this class is determined by two flags
 * printPartialEquations and printVariableEquations.
 * When a variable or partial derivative is encountered then
 * its equation may be printed.
 * By default equations for PartialDerivatives are printed
 * but equations for normal derivatives are not.
 * TODO might want to print eqn for y=sin(x) but not x=3
 *  
 * @author Rich Morris
 * Created on 26-Feb-2004
 */
public class DPrintVisitor extends PrintVisitor {
	private boolean printPartialEquations = true;
	private boolean printVariableEquations = false;
	
	/** Prints the variable or its equation.
	 * Depends on the statr of the flags and whether the variable has an equation.
	 */
	public Object visit(ASTVarNode node, Object data) throws ParseException
	{
		Variable var = node.getVar();
		if(var instanceof PartialDerivative)
		{
			PartialDerivative deriv = (PartialDerivative) var;
			if(printPartialEquations && deriv.hasEquation())
				deriv.getEquation().jjtAccept(this,null);
			else
				sb.append(node.getName());
		}
		else if(var instanceof DVariable)
		{
			DVariable dvar = (DVariable) var;
			if(printVariableEquations && dvar.hasEquation())
				dvar.getEquation().jjtAccept(this,null);
			else
				sb.append(node.getName());
		}
		else
			sb.append(node.getName());

	  return data;
	}

	/** Whether to print equations for partial derivatives? */
	public boolean isPrintPartialEquations() {
		return printPartialEquations;
	}

	/** Whether to print equations for normal variables? */
	public boolean isPrintVariableEquations() {
		return printVariableEquations;
	}

	/** Switches printing of equations for partial derivatives on or off. */
	public void setPrintPartialEquations(boolean b) {
		printPartialEquations = b;
	}

	/** Switches printing of equations for normal variables on or off. */
	public void setPrintVariableEquations(boolean b) {
		printVariableEquations = b;
	}

}
