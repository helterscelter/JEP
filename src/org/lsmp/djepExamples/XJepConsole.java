/* @author rich
 * Created on 21-Mar-2005
 *
 * See LICENSE.txt for license information.
 */
package org.lsmp.djepExamples;
import org.lsmp.djep.xjep.*;
import org.nfunk.jep.*;
import java.text.NumberFormat;
import java.util.Enumeration;
/**
 * @author Rich Morris
 * Created on 21-Mar-2005
 */
public class XJepConsole extends Console
{
	protected NumberFormat format=null;
	
	public static void main(String[] args)
	{
		Console c = new XJepConsole();
		c.run(args);
	}
	
	public String getPrompt()
	{
		return "XJep > ";
	}

	public void initialise()
	{
		j = new XJep();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		j.setAllowUndeclared(true);
		j.setAllowAssignment(true);
		j.setImplicitMul(true);
	}

	public void printHelp()
	{
		super.printHelp();
		println("'setMaxLen 80'\tensures all lines and < 80 chars");
		println("'setDP 3'\tonly prints 3 decimal places");
		println("'invalidate'\tmarks all variables as invalid, forcing reevaluation");
		println("eg 'x=5','y=2*x' gives value 10, 'invalidate', 'x=6', 'y' gives value 12");
	}

	public void printIntroText()
	{
		println("XJep Console");
		super.printStdHelp();
	}

	public void printOps()
	{
		println("Known operators");
		Operator ops[] = j.getOperatorSet().getOperators();
		int maxPrec = -1;
		for(int i=0;i<ops.length;++i)
			if(((XOperator) ops[i]).getPrecedence()>maxPrec) maxPrec=((XOperator) ops[i]).getPrecedence();
		for(int j=-1;j<=maxPrec;++j)
			for(int i=0;i<ops.length;++i)
				if(((XOperator) ops[i]).getPrecedence()==j)
					println(((XOperator) ops[i]).toFullString());
	}

	public boolean testSpecialCommands(String command)
	{
		if(!super.testSpecialCommands(command)) return false;
		XJep xj = (XJep) this.j;

		if( command.equals("invalidate"))
		{
			resetVars();
			return false;
		}

		if(command.startsWith("setMaxLen"))
		{
			String words[] = split(command);
			int len = Integer.parseInt(words[1]);
			xj.getPrintVisitor().setMaxLen(len);
			return false;
		}
		if(command.startsWith("setDp"))
		{
			String words[] = split(command);
			int dp = Integer.parseInt(words[1]);
			
			format = NumberFormat.getInstance();
			xj.getPrintVisitor().setNumberFormat(format);
			format.setMaximumFractionDigits(dp);
			format.setMinimumFractionDigits(dp);

			return false;
		}
		return true;
	}

	public void processEquation(Node node) throws Exception
	{
		XJep xj = (XJep) j;
		Node processed = xj.preprocess(node);
		Node simp = xj.simplify(processed);
		print("Simplified:\t"); 
		println(xj.toString(simp));
		Object val = xj.evaluate(simp);
		String s = xj.getPrintVisitor().formatValue(val);
		println("Value:\t\t"+s);
	}

	public void printVars() {
		PrintVisitor pv = ((XJep) j).getPrintVisitor();
		SymbolTable st = j.getSymbolTable();
		println("Variables:");
		for(Enumeration  loop = st.keys();loop.hasMoreElements();)
		{
			String s = (String) loop.nextElement();
			XVariable var = (XVariable) st.getVar(s);
			println("\t"+var.toString(pv));
		}
	}

	public void resetVars()
	{
		this.j.getSymbolTable().clearValues();
	}

}
