/* @author rich
 * Created on 18-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.xjep.function;

import java.util.*;
import org.nfunk.jep.*;
import org.nfunk.jep.function.*;

/**
 * A product function product(x^2,x,1,10) finds the product of x^2 with x running from 1 to 10.
 *
 * @author Rich Morris
 * Created on 10-Sept-2004
 */
public class Max extends SumType {

	static Comparative comp = new Comparative(Comparative.LE);

	public Max()
	{
		super("Max");
	}

		
	public Object evaluate(Object elements[]) throws ParseException
	{
		Object ret;
		ret = elements[0];
		for(int i=1;i<elements.length;++i)
		{
			if(comp.gt(elements[i],ret))
			ret = elements[i];
		}
		return ret;
	}
}
