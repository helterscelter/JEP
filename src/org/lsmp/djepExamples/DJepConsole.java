/* @author rich
 * Created on 21-Mar-2005
 *
 * See LICENSE.txt for license information.
 */
package org.lsmp.djepExamples;

import org.nfunk.jep.Node;
import org.lsmp.djep.djep.*;
import java.io.*;

/**
 * @author Rich Morris
 * Created on 21-Mar-2005
 */
public class DJepConsole extends XJepConsole
{
	boolean verbose = false;
	
	public void initialise()
	{
		j = new DJep();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		j.setAllowUndeclared(true);
		j.setAllowAssignment(true);
		j.setImplicitMul(true);
		((DJep) j).addStandardDiffRules();
	}

	public void printHelp()
	{
		super.printHelp();
		println("'diff(x^2,x)' to differentiate x^2 with respect to x");
		println("'verbose on', 'verbose off' switch verbose mode on or off");
	}

	public void printIntroText()
	{
		println("DJep: differention in JEP. eg. diff(x^2,x)");
		printStdHelp();
	}

	public String getPrompt()
	{
		return "DJep > ";
	}

	public void processEquation(Node node) throws Exception
	{
		DJep j = (DJep) this.j;
		if(verbose) {
			print("Parsed:\t\t"); 
			println(j.toString(node));
		}
		Node processed = j.preprocess(node);
		if(verbose) {
			print("Processed:\t"); 
			println(j.toString(processed));
		}
					
		Node simp = j.simplify(processed);
		if(verbose) 
			print("Simplified:\t"); 
		println(j.toString(simp));
			
		if(verbose) {
			print("Full Brackets, no variable expansion:\n\t\t");
			j.getPrintVisitor().setMode(DPrintVisitor.FULL_BRACKET,true);
			j.getPrintVisitor().setMode(DPrintVisitor.PRINT_PARTIAL_EQNS,false);
			println(j.toString(simp));
			j.getPrintVisitor().setMode(DPrintVisitor.PRINT_PARTIAL_EQNS,true);
			j.getPrintVisitor().setMode(DPrintVisitor.FULL_BRACKET,false);
		}

		Object val = j.evaluate(simp);
		String s = j.getPrintVisitor().formatValue(val);
		println("Value:\t\t"+s);
	}

	
	/** Creates a new Console object and calls run() */
	public static void main(String args[]) {
		Console c = new DJepConsole();
		c.run(args);
	}

	public boolean testSpecialCommands(String command)
	{
		if(!super.testSpecialCommands(command)) return false;
		if(command.startsWith("verbose"))
		{
			String words[] = split(command);
			if(words.length<2)
				println("verbose should be on or off");
			else if(words[1].equals("on"))
				verbose = true;
			else if(words[1].equals("off"))
				verbose = true;
			else
				println("verbose should be on or off");
			return false;
		}
		return true;
	}

}
