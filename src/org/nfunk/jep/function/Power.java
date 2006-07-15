/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/
package org.nfunk.jep.function;

import java.lang.Math;
import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

/**
 * Computes the power of an number.
 * Implements a fast algorithm for integer powers.
 * @author N Funk, R Morris incorporating code by Patricia Shanahan pats@acm.org
 */
public class Power extends PostfixMathCommand
{
	public Power()
	{
		numberOfParameters = 2;
	}
	
	public void run(Stack inStack)
		throws ParseException 
	{
		checkStack(inStack); // check the stack
		
		Object param2 = inStack.pop();
		Object param1 = inStack.pop();
		
		inStack.push(power(param1, param2));
	}
	
	public Object power(Object param1, Object param2)
		throws ParseException
	{
		if (param1 instanceof Complex) {
			if (param2 instanceof Complex)
				return power((Complex)param1, (Complex)param2);
			else if (param2 instanceof Number) 
				return power((Complex)param1, (Number)param2);
		}
		else if (param1 instanceof Number) {
			if (param2 instanceof Complex)
				return power((Number)param1, (Complex)param2);
			else if (param2 instanceof Number) 
				return power((Number)param1, (Number)param2);
		}

		throw new ParseException("Invalid parameter type");
	}
	

	public Object power(Number d1, Number d2)
	{
		int ival = d2.shortValue();
		double dval = d2.doubleValue();
		if (d1.doubleValue()<0 && dval != ival)
		{
			Complex c = new Complex(d1.doubleValue(), 0.0);
			return c.power(d2.doubleValue());
		}
		else
		{
			if(dval == ival)
			{
				if(dval>=0)
					return new Double(power(d1.doubleValue(),ival));
				else
					return new Double(1.0/power(d1.doubleValue(),-ival));
			}

			return new Double(Math.pow(d1.doubleValue(),d2.doubleValue()));
		}
	}
	
	public Object power(Complex c1, Complex c2)
	{
		Complex temp = c1.power(c2);

		if (temp.im()==0)
			return new Double(temp.re());
		else
			return temp;
	}
	
	public Object power(Complex c, Number d)
	{
		Complex temp = c.power(d.doubleValue());
		
		if (temp.im()==0)
			return new Double(temp.re());
		else
			return temp;
	}

	public Object power(Number d, Complex c)
	{
		Complex base = new Complex(d.doubleValue(), 0.0);
		Complex temp = base.power(c);
		
		if (temp.im()==0)
			return new Double(temp.re());
		else
			return temp;
	}
	
	/**
	 * A fast routine for computing integer powers.
	 * Code adapted from http://mindprod.com/jgloss/power.html
	 * Almost identical to the method Knuth gives on page 462 of The Art of Computer Programming Volume 2 Seminumerical Algorithms.
	 * @param x number to be taken to a power.
	 * @param n power to take x to. 0 <= n <= Integer.MAX_VALUE
	 * Negative numbers will be treated as unsigned positives.
 	 * @return x to the power n
	 * @author Patricia Shanahan pats@acm.org
	 */
	public double power(double x,int n)
	{
		switch(n){
		case 0: x = 1.0; break;
		case 1: break;
		case 2: x *= x; break;
		case 3: x *= x*x; break;
		case 4: x *= x*x*x; break;
		case 5: x *= x*x*x*x; break;
		case 6: x *= x*x*x*x*x; break;
		case 7: x *= x*x*x*x*x*x; break;
		case 8: x *= x*x*x*x*x*x*x; break;
		default:
			{
			   int bitMask = n;
			   double evenPower = x;
			   double result;
			   if ( (bitMask & 1) != 0 )
			      result = x;
			   else
			      result = 1;
			   bitMask >>>= 1;
			   while ( bitMask != 0 ) {
			      evenPower *= evenPower;
			      if ( (bitMask & 1) != 0 )
			         result *= evenPower;
			      bitMask >>>= 1;
			   } // end while
			   x = result;
			}
		}
		return x;
	} 
}
