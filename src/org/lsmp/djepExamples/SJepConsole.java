/* @author rich
 * Created on 22-Mar-2005
 *
 * See LICENSE.txt for license information.
 */
package org.lsmp.djepExamples;

import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.lsmp.djep.xjep.XJep;
import org.lsmp.djep.sjep.Monomial;
import org.lsmp.djep.sjep.PConstant;
import org.lsmp.djep.sjep.PFunction;
import org.lsmp.djep.sjep.PNodeI;
import org.lsmp.djep.sjep.POperator;
import org.lsmp.djep.sjep.PVariable;
import org.lsmp.djep.sjep.Polynomial;
import org.lsmp.djep.sjep.PolynomialCreator;

/**
 * @author Rich Morris
 * Created on 22-Mar-2005
 */
public class SJepConsole extends DJepConsole
{
	private static final long serialVersionUID = -2796652887843007314L;
	PolynomialCreator pc = null;
	
	public static void main(String[] args)
	{
		Console c = new SJepConsole();
		c.run(args);
	}

	public String getPrompt()
	{
		return "SJep > ";
	}

	public void initialise()
	{
		super.initialise();
		pc = new PolynomialCreator((XJep) j);
	}

	public void printIntroText()
	{
		println("SJep: advanced simplification/expansion");
	}

	public void processEquation(Node node) throws ParseException
	{
		XJep xj = (XJep) j;

		Node pre = xj.preprocess(node);
		Node proc = xj.simplify(pre);
		print("Old simp:\t"); 
		println(xj.toString(proc));
		Node simp = pc.simplify(proc);
		print("New simp:\t"); 
		println(xj.toString(simp));

		PNodeI poly = pc.createPoly(proc);
		explain(poly,0);
		
		Node expand = pc.expand(proc);
		print("Expanded:\t"); 
		println(xj.toString(expand));

		Object val = xj.evaluate(simp);
		String s = xj.getPrintVisitor().formatValue(val);
		println("Value:\t\t"+s);
	}

	private void explain(PNodeI pnode,int depth) {
		// TODO Auto-generated method stub
		for(int i=0;i<depth;++i) print(" ");
		if(pnode instanceof Polynomial)
		{
			Polynomial poly = (Polynomial) pnode;
			println("Polynomial with "+poly.getNTerms()+" terms:");
			for(int i=0;i<poly.getNTerms();++i)
				explain(poly.getTerm(i),depth+1);
		}
		else if(pnode instanceof Monomial)
		{
			Monomial mon = (Monomial) pnode;
			print("Monomial with coefficient: ");
			print(mon.getCoeff().getValue().toString());
			println(" and "+mon.getNVars()+" variables:");
			for(int i=0;i<mon.getNVars();++i)
			{
				explain(mon.getVar(i),depth+1);
				for(int j=0;j<depth;++j) print(" ");
				println(" power:");
				explain(mon.getPower(i),depth+1);
			}
		}
		else if(pnode instanceof PConstant)
		{
			println("Constant: "+((PConstant) pnode).getValue().toString());
		}
		else if(pnode instanceof PVariable)
		{
			println("Variable: "+((PVariable) pnode).getVariable().toString());
		}
		else if(pnode instanceof PFunction)
		{
			PFunction fun = (PFunction) pnode;
			println("Function: "+fun.getName()+" with "+fun.getNArgs()+"arguments:");
			for(int i=0;i<fun.getNArgs();++i)
				explain(fun.getArg(i),depth+1);
		}
		else if(pnode instanceof POperator)
		{
			POperator fun = (POperator) pnode;
			println("Operator: "+fun.getName()+" with "+fun.getNArgs()+"arguments:");
			for(int i=0;i<fun.getNArgs();++i)
				explain(fun.getArg(i),depth+1);
		}
	}
}
