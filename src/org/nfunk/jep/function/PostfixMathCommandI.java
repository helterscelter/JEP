/*****************************************************************************

JEP - Java Expression Parser
    JEP is a Java package for parsing and evaluating mathematical 
	expressions. It currently supports user defined variables, 
	constant, and functions. A number of common mathematical 
	functions and constants are included.

Author: Nathan Funk
Copyright (C) 2000 Nathan Funk

    JEP is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    JEP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JEP; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*****************************************************************************/

package org.nfunk.jep.function;

import java.util.*;
import org.nfunk.jep.*;


/**
 * All function classes must implement this interface to ensure that the run()
 * method is implemented.
 */
public interface PostfixMathCommandI
{
	/**
	 * Run the function on the stack. Pops the arguments from the stack, and
	 * pushes the result on the top of the stack.
	 */
	public void run(Stack aStack) throws ParseException;
	
	/**
	 * Returns the number of required parameters, or -1 if any number of
	 * parameters is allowed.
	 */
	public int getNumberOfParameters();

	/**
	 * Sets the number of current number of parameters used in the next call
	 * of run(). This method is only called when the reqNumberOfParameters is
	 * -1.
	 */
	public void setCurNumberOfParameters(int n);
}
