/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/
package org.nfunk.jep.function;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

/** The complex conjugate of a number conj(c)
 * 
 * @author Rich Morris
 * Created on 13-Feb-2005
 */
public class Conjugate extends PostfixMathCommand
{
	public Conjugate() {
		numberOfParameters = 1;
	}
	
	public void run(Stack inStack) throws ParseException {
		
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(conj(param));//push the result on the inStack
		return;
	}
	
	public Object conj(Object param) throws ParseException {
		
		if (param instanceof Complex)
			return ((Complex)param).conj();
		else if (param instanceof Number)
			return param;
		

		throw new ParseException("Invalid parameter type");
	}

}
