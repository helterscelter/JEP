/* @author rich
 * Created on 14-Dec-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.sjep;
import org.lsmp.djep.xjep.*;
import org.nfunk.jep.*;
import java.io.*;

/**
 * @author Rich Morris
 * Created on 14-Dec-2004
 */
public class PolyTester {
	XJep j;
	SymbolTable st;
	BufferedReader br;
	PolynomialCreator pc;
	 
	public PolyTester() {
		j = new XJep();
		st = j.getSymbolTable();
		j.setAllowAssignment(true);
		j.setImplicitMul(true);
		j.setAllowUndeclared(true);
		j.addStandardFunctions();
		
		br = new BufferedReader(new InputStreamReader(System.in));
		pc = new PolynomialCreator(j);
	}

	public void inputLoop()
	{
		String line;
		while(true)
		{
			System.out.print("> ");
			try {
				if((line = br.readLine()) == null) break;
				Node n = j.parse(line);
				PNodeI p = pc.createPoly(n);
				System.out.println(p.toString());
				PNodeI exp = p.expand();
				System.out.println("expand; "+exp.toString());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		PolyTester pt = new PolyTester();
		pt.inputLoop();
	}
}
