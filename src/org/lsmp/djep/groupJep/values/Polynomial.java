/* @author rich
 * Created on 09-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.values;
import org.lsmp.djep.groupJep.interfaces.*;
import org.lsmp.djep.groupJep.*;
import org.nfunk.jep.type.*;
/**
 * The ring of polynomials over a ring R.
 * 
 * @author Rich Morris
 * Created on 09-Mar-2004
 */
public class Polynomial extends Number {
	private RingI baseRing;
	private String symbol;
	private Number coeffs[];
	private int degree;
	/**
	 * 
	 */
	public Polynomial(RingI baseRing,String symbol,Number coeffs[]) {
		this.baseRing = baseRing;
		this.symbol = symbol;
		int deg=0;
		for(int i=coeffs.length-1;i>0;--i)
			if(!baseRing.equals(coeffs[i],baseRing.getZERO()))
			{
				deg=i;
				break;
			}
		if(deg == coeffs.length-1)
			this.coeffs = coeffs;
		else
		{
			this.coeffs = new Number[deg+1];
			System.arraycopy(coeffs,0,this.coeffs,0,deg+1);
		}
		this.degree = deg;
	}

	/** Sub classes can change the coefficients. Other methods
	 * should treat polynomials as imutable. */
	protected void setCoeffs(Number coeffs[])
	{
		this.coeffs = coeffs;
		this.degree = coeffs.length-1;
	}
	/** sub classes should overright this to make the correct type. */
	protected Polynomial valueOf(Number lcoeffs[])
	{
		Polynomial p = new Polynomial(baseRing,symbol,lcoeffs);
		return p;
	}
	public Polynomial add(Polynomial poly)
	{
		int deg = degree > poly.degree ? degree : poly.degree;
		Number lcoeffs[] = new Number[deg+1];
		for(int i=0;i<=deg;++i)
		{
			if(i<=degree && i <= poly.degree)
				lcoeffs[i] = baseRing.add(coeffs[i],poly.coeffs[i]);
			else if(i<=degree)
				lcoeffs[i] = coeffs[i];
			else
				lcoeffs[i] = poly.coeffs[i];
		}
		return valueOf(lcoeffs);
	}

	public Polynomial sub(Polynomial poly)
	{
		int deg = degree > poly.degree ? degree : poly.degree;
		Number lcoeffs[] = new Number[deg+1];
		for(int i=0;i<=deg;++i)
		{
			if(i<=degree && i <= poly.degree)
				lcoeffs[i] = baseRing.sub(coeffs[i],poly.coeffs[i]);
			else if(i<=degree)
				lcoeffs[i] = coeffs[i];
			else
				lcoeffs[i] = baseRing.getInverse(poly.coeffs[i]);
		}
		return valueOf(lcoeffs);
	}
	
	public Polynomial mul(Polynomial poly)
	{
		int deg = degree + poly.degree;
		Number lcoeffs[] = new Number[deg+1];
		for(int i=0;i<=deg;++i)
			lcoeffs[i] = baseRing.getZERO();

		for(int i=0;i<=degree;++i)
			for(int j=0;j<=poly.degree;++j)
			{
				lcoeffs[i+j] = baseRing.add(lcoeffs[i+j],
					baseRing.mul(coeffs[i],poly.coeffs[j]));			
			}
		return valueOf(lcoeffs);
	}

	private String stripBrackets(Number num)
	{
		String s = num.toString();
		if(s.startsWith("<") && s.endsWith(">"))
		{
			if(s.indexOf('+')!=-1 || s.indexOf('-')>1) // is it <x+y> or <x-y>. <-x> is OK
				return s;
			else
				return s.substring(1,s.length()-1);
		}
		else return s;
	}
	public String toString()
	{
		if(degree==0) return "<"+stripBrackets(coeffs[0])+">";
		StringBuffer sb = new StringBuffer("<");
		for(int i=degree;i>=0;--i)
		{
			String s = stripBrackets(coeffs[i]);

			// don't bother if a zero coeff
			if(s.equals("0") ||
			  this.baseRing.equals(coeffs[i],baseRing.getZERO()))
				continue;

			// apart from first add a + sign if positive
			if(i!=degree && !s.startsWith("-")) sb.append("+");
			
			// always print the final coeff (if non zero)
			if( i==0 ) {
				String s1 = coeffs[i].toString();
				if(s1.startsWith("<") && s1.endsWith(">"))
				{
						sb.append(s1.substring(1,s1.length()-1));
				}
				else 	sb.append(s1);
				break;
			}
			// if its -1 t^i just print -
			if(s.equals("-1")) 
				sb.append("-");
			else if(s.equals("1")  ||
				this.baseRing.equals(
					coeffs[i],
					baseRing.getONE()))
				{} // don't print 1
			else {
				sb.append(stripBrackets(coeffs[i]));
				sb.append(" ");
			}
			if(i>=2) sb.append(symbol+"^"+i);
			else if(i==1) sb.append(symbol);
		}
		sb.append(">");
		return sb.toString();
	}
	
	public int getDegree() { return degree; }
	public String getSymbol() { return symbol; }
	public Number[] getCoeffs() { return coeffs; }
	public Number getCoeff(int i) { return coeffs[i]; }
	public RingI getBaseRing() { return baseRing; }

	/** value of constant coeff. */	
	public int intValue() {return coeffs[0].intValue();	}
	/** value of constant coeff. */	
	public long longValue() {return coeffs[0].longValue();	}
	/** value of constant coeff. */	
	public float floatValue() {	return coeffs[0].floatValue();	}
	/** value of constant coeff. */	
	public double doubleValue() {return coeffs[0].doubleValue();	}

	public boolean equals(Polynomial n)
	{
		if(this.getDegree()!=n.getDegree()) return false;
		for(int i=0;i<=this.getDegree();++i)
			if(!baseRing.equals(this.getCoeff(i),n.getCoeff(i)))
				return false;
		return true;
	}

	/** returns the complex value of this polynomial. 
	 * Where the value of the symbol is replaced by rootVal. 
	 */
	public Complex calculateComplexValue(Complex rootVal) {
		Number val = coeffs[this.getDegree()];
		Complex cval = GroupJep.getComplexValue(val);
		
		for(int i=this.getDegree()-1;i>=0;--i)
		{
			Number val2 = coeffs[i];
			Complex cval2 = GroupJep.getComplexValue(val2);
			Complex prod = cval.mul(rootVal);
			cval = prod.add(cval2);
		}
		return cval;
	}

}
