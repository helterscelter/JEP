/*****************************************************************************

JEP - Java Math Expression Parser 2.24
	  December 30 2002
	  (c) Copyright 2002, Nathan Funk
	  See LICENSE.txt for license information.

*****************************************************************************/

/**
 * Console - JEP Example Applet
 * Copyright (c) 2000 Nathan Funk
 *
 * @author Richard Morris 
 */

package org.lsmp.djepExamples;
import org.nfunk.jep.*;
import org.lsmp.djep.djep.*;
import org.lsmp.djep.matrixJep.*;
import java.io.*;

/**
* This class implements a simple command line utility for evaluating
* mathematical expressions.
*
*   Usage: java org.lsmp.djep.matrixParser.MatrixParserConsole [expression]
*
* If an argument is passed, it is interpreted as an expression
* and evaluated. Otherwise, a prompt is printed, and the user can enter
* expressions to be evaluated. To exit from the command prompt a 'q' must
* be entered.
* typing 
* <pre>diff(x^2,x)</pre>
* will differentiate x^2 wrt 2. And
* <pre>eval(x^2,x,3)</pre> 
* will calculate x^2 at x=3.
* Expresions like
* <pre>eval(diff(diff(x^2+y^3,x),y),x,3,y,4)</pre>
* are also allowed.
*/
public class MatrixConsole {
	/** Main JEP object */
	MatrixJep j;	
	
	/** The prompt string */
	private String prompt;
	
	/** The input reader */
	private BufferedReader br;

	/** Constructor */
	public MatrixConsole() {
		prompt = "dJEPdx > ";
		br = new BufferedReader(new InputStreamReader(System.in));

	}

	/** Creates a new Console object and calls run() */
	public static void main(String args[]) throws IOException {
		MatrixConsole c = new MatrixConsole();
		c.run(args);
	}
	
	/** sets up all the needed objects. */
	void initialise()
	{
		j = new MatrixJep();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		j.setAllowUndeclared(true);
		j.setImplicitMul(true);
		j.setAllowAssignment(true);
		j.addStandardDiffRules();
		//		dv.addDiffRule(new MProductDiffRule(dv,MatrixOperatorSet.M_HAT));
	}
	
	/** performs the operations specified by the node tree and prints results. */
	void processEquation(Node node)
	{
		if(node==null) return;
		try
		{
			System.out.print("fun:\t\t"); 
			j.println(node);
			Node matEqn = j.preprocess(node);
			j.println(matEqn);
			Object res = j.evaluate(matEqn);
			System.out.println("Res: "+res);
			System.out.println("Variables");
			((DSymbolTable)j.getSymbolTable()).print(j.getPrintVisitor());	
		}
		catch(ParseException e1) { System.out.println("Parse Error: "+e1.getMessage()); }
		catch(IllegalArgumentException e2) { System.out.println(e2.getMessage()); }
		catch(Exception e3) { System.out.println("Exception "+e3.getMessage()); e3.printStackTrace(); }
	}

	/** The input loop */
	public void run(String args[]) throws IOException {
		String command="";
		initialise();
		 
		if (args.length>0) {
			// evaluate the expression passed as arguments
			String temp = args[0];
			for (int i=1; i<args.length; i++) temp += " " + args[i];
			j.parseExpression(temp);
			if (j.hasError())
				System.out.println(j.getErrorInfo());
			else
			{
				processEquation(j.getTopNode());
			}

		} else {
			// no arguments - interactive mode
				
			System.out.println("dJEPdx - Enter q to quit");
			System.out.println("\tdiff(x^2,x) to differentiate");
			System.out.println("\teval(x^y,x,2,y,3) to evaluate");
			System.out.println("\trules to print operators and differentation rules.");	
			System.out.println("\tinvalidate to make all variables as having invalid values, used to force evaluation.");	
			System.out.println("\t[1,2].[3,4] for dot product.");
			System.out.println("\t[1,2,3]^[3,4,5] for vector product (2^3 still works).");
			System.out.println("\t[[1,2],[3,4]] for matricies.");
						
			System.out.print(prompt);

			while ((command = getCommand()) != null) {
				//j.parseExpression(command);
				Node topNode=null;
				try
				{
					topNode = j.parse(command);
		
				} catch (ParseException e) { System.out.println(e.getMessage()); }
				
//				if (j.hasError()) {
//					System.out.println(j.getErrorInfo());
//				} else {
					// expression is OK, get the value
					//processEquation(Node j.getTopNode());
					processEquation(topNode);
					// did error occur during evaluation?
//					if (j.hasError()) {
//						System.out.println(j.getErrorInfo());
//					}
//				}
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

		if( s.equals("rules"))
		{
			j.getDifferentationVisitor().printDiffRules();

			System.out.println("Operators:");
			j.getOperatorSet().printOperators();

//			System.out.println("dJEPdx - Enter q to quit, rules to print the differentation rules,\ndiff(x^2,x) to differentiate,\neval(x^y,x,2,y,3) to evaluate");	
			System.out.print(prompt);
			return getCommand();
		}
		if( s.equals("invalidate"))
		{
			this.j.getSymbolTable().clearValues();
			((DSymbolTable)j.getSymbolTable()).print(j.getPrintVisitor());	
			System.out.print(prompt);
			return getCommand();
		}

		if (s.equals("q")
			|| s.equals("quit")
			|| s.equals("exit"))
			return null;
		
		return s;
	}
}
