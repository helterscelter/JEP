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
A collection of utility methods for function classes.
<p>
It includes a numberOfParameters member, that is
checked when parsing the expression. This member should be 
initialized to an appropriate value for all classes 
extending this class. If an arbitrary number of parameters should
be allowed, initialize this member to -1.
*/
public class PostfixMathCommand
{
	/** number of parameters a the function can take. Initialize
	    this value to -1 if any number of parameters should be
	    allowed */
	protected int numberOfParameters;

	public PostfixMathCommand() {
		numberOfParameters = 0;	
	}

	/** Check whether the stack is not null, throw a ParseException
	    if it is. (used in previous versions to check whether the
	    number of arguments on the stack was equal to the number of
	    parameters for the funciton) */
	protected void checkStack(Stack inStack) throws ParseException
	{
		/* Check if stack is null */
		if (null == inStack)
		{
			throw new ParseException("Stack argument null");
		}
	}

	public int getNumberOfParameters()
	{
		return numberOfParameters;
	}
}
