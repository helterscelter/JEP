/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

/**
 * Console - JEP Example Applet
 * Copyright (c) 2000 Nathan Funk
 *
 * @author Nathan Funk 
 */

package org.lsmp.djepExamples;

import org.lsmp.djep.vectorJep.VectorJep;
import org.nfunk.jep.*;
import java.io.*;
//import org.nfunk.sovler.*;

/**
* This class implements a simple command line utility for evaluating
* mathematical expressions.
*
*   Usage: java org.nfunk.jepexamples.Console [expression]
*
* If an argument is passed, it is interpreted as an expression
* and evaluated. Otherwise, a prompt is printed, and the user can enter
* expressions to be evaluated. To exit from the command prompt a 'q' must
* be entered.
*/
class VectorConsole {
	
	/** The prompt string */
	private String prompt;
	
	/** The input reader */
	private BufferedReader br;
	
	/** where to give a dump of tree after parsing. **/
	boolean dumpTree = false;
	/** whether to print symbol table after each line. **/
	boolean dumpSymbols=false;
	
	/** Constructor */
	public VectorConsole() {
		prompt = "JEP > ";
		br = new BufferedReader(new InputStreamReader(System.in));

	}

	/** Creates a new Console object and calls run() */
	public static void main(String args[]) throws IOException {
		VectorConsole c = new VectorConsole();
		c.run(args);
	}
	
	/** The input loop */
	public void run(String args[]) throws IOException {
		String command="";
		JEP j = new VectorJep();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		//j.setTraverse(true);
		j.setAllowAssignment(true);
		j.setAllowUndeclared(true);
		String temp="";
		for(int i=0;i<args.length;++i)
		{
			if(args[i].equals("--dumpTree"))
				dumpTree = true;
			else if(args[i].equals("--dumpSymbols"))
				dumpSymbols = true;
			else
				temp += " " + args[i];
		} 
		if(temp.length()!=0)
		{
			j.parseExpression(temp);
			if (j.hasError())
				System.out.println(j.getErrorInfo());
			else
				System.out.println(j.getValueAsObject());
		}
		else
		{
			// no arguments - interactive mode
				
			System.out.println("JEP - Enter q to quit");	
			System.out.print(prompt);

			while ((command = getCommand()) != null) 
			{
				j.parseExpression(command);
				
				if (j.hasError()) {
					System.out.println(j.getErrorInfo());
				} 
				else 
				{
					if(dumpTree)
						((SimpleNode) j.getTopNode()).dump("");

					// expression is OK, get the value
					Object value = j.getValueAsObject();
					
					// did error occur during evaluation?
					if (j.hasError()) {
						System.out.println(j.getErrorInfo());
					}
					else
					{
						if(dumpSymbols)
							System.out.print(j.getSymbolTable().toString());
						System.out.println(value);
					}

/*
					System.out.println(
						(LinearVisitor.isLinear(j.getTopNode())) ?
						"Linear" : "Not Linear");
					System.out.println(
						(ConstantVisitor.isConstant(j.getTopNode())) ?
						"Constant" : "Not Constant");
*/
				}
					
				System.out.print(prompt);
			}
		}
		
	}
	
	/**
	 * Get a command from the input.
	 * @return null if an error occures, or if the user enters a terminating
	 *  command
	 */
	private String getCommand() throws IOException {
		String s;
		
		if (br == null)
			return null;

		if ( (s = br.readLine()) == null)
			return null;

		if (s.equals("q")
			|| s.equals("quit")
			|| s.equals("exit"))
			return null;
		
		return s;
	}
}
