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
 * A sum function Sum(x^2,x,1,10) finds the sum of x^2 with x running from 1 to 10.
 * Sum(x^2,x,1,10,2) calculates the 1^2+3^2+5^2+7^2+9^2 i.e. in steps of 2.
 * @author Rich Morris
 * Created on 10-Sept-2004
 */
public class Sum extends SumType {

	static Add add = new Add();

	public Sum()
	{
		super("Sum");
	}

		
	public Object evaluate(Object elements[]) throws ParseException
	{
		Object ret;
		ret = elements[0];
		for(int i=1;i<elements.length;++i)
		{
			ret = add.add(ret,elements[i]);
		}
		return ret;
	}
}
