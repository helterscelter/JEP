/*
 * Created on 16-Jun-2003 by Rich webmaster@pfaf.org
 * www.comp.leeds.ac.uk/pfaf/lsmp
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 *
 * Adapted from :
 */

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
 * @author Nathan Funk 
 */

package org.lsmp.djepExamples;
import org.lsmp.djep.djep.*;
import org.nfunk.jep.*;
import java.io.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
* This class implements a simple command line utility for evaluating
* mathematical expressions.
*
*   Usage: java org.lsmp.djep.DJepConsole [expression]
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
public class DJepConsole extends Applet implements ActionListener {
	/** Main JEP object */
	DJep j;	
	/** Input equation. */
	TextField inputTF;
	/** variable to differentiate wrt respect to. */
	TextField varTF;
	/** Output equation. */
	TextField outputTF;
	/** Button to perform differentiation. */
	Button but;
	
	
	/** The prompt string */
	private String prompt;
	
	/** The input reader */
	private BufferedReader br;
	
	
	/** Constructor */
	public DJepConsole() {
		prompt = "DJep > ";
		br = new BufferedReader(new InputStreamReader(System.in));

	}

	/** Applet initialisation */
		
	public void init() 
	{
		initialise();
		setLayout(new GridLayout(3,2));
		inputTF = new TextField("sin(x^2)",50);
		outputTF = new TextField(50);
		outputTF.setEditable(false);
		varTF = new TextField("x",5);
		but = new Button("Calculate");
		but.addActionListener(this);
		inputTF.addActionListener(this);

		Panel p1 = new Panel();
		p1.add(new Label("Differentiate:"));
		p1.add(inputTF);
		add(p1);
		
		Panel p2 = new Panel();
		p2.add(new Label("with respect to:"));
		p2.add(varTF);
		p2.add(but);
		add(p2);
		
		Panel p3 = new Panel();
		p3.add(new Label("Result:"));
		p3.add(outputTF);
		add(p3);
	}
	
	/** Called when the Calculate button is pressed.
	 * Firsts differentiates the expresion in inputTF wrt variable in
	 * varTF, then simplifies it and puts results into outputTF.
	 */ 
	public void actionPerformed(ActionEvent e)
	{
			String command = inputTF.getText();
			j.parseExpression(command);
			if (j.hasError())
			{
				outputTF.setText(j.getErrorInfo());
			}
			else
			{
				// expression is OK, get the value
				try
				{
					Node diff = j.differentiate(j.getTopNode(),varTF.getText());
					Node simp = j.simplify(diff);
					if (j.hasError()) 
					{
						outputTF.setText(j.getErrorInfo());
					}
					else
						outputTF.setText(j.toString(simp));
				}
				catch(ParseException e1) { outputTF.setText("Parse Error: "+e1.getMessage()); }
				catch(IllegalArgumentException e2) { outputTF.setText(e2.getMessage()); }
				catch(Exception e3) { outputTF.setText(e3.getMessage()); }

				// did error occur during evaluation?
			}
		
	}
	
	/** Creates a new Console object and calls run() */
	public static void main(String args[]) throws IOException {
		DJepConsole c = new DJepConsole();
		c.run(args);
	}
	
	/** sets up all the needed objects. */
	public void initialise()
	{
		j = new DJep();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		j.setAllowUndeclared(true);
		j.setAllowAssignment(true);
		j.setImplicitMul(true);
		j.addStandardDiffRules();
		//j.setTraverse(true);
	}
	
	/** performs the operations specified by the node tree and prints results. */
	public void processEquation(Node node)
	{
		try
		{
			System.out.print("Parsed:\t\t"); 
			j.println(node);
			Node processed = j.preprocess(node);
			System.out.print("Processed:\t"); 
			j.println(processed);
			
			Node simp = j.simplify(processed);
			System.out.print("Simplified:\t"); 
			j.println(simp);
			
			System.out.print("Full Brackets, no variable expansion:\t");
			j.getPrintVisitor().setMode(DPrintVisitor.FULL_BRACKET,true);
			j.getPrintVisitor().setMode(DPrintVisitor.PRINT_PARTIAL_EQNS,false);
			j.println(simp);
			j.getPrintVisitor().setMode(DPrintVisitor.PRINT_PARTIAL_EQNS,true);
			j.getPrintVisitor().setMode(DPrintVisitor.FULL_BRACKET,false);

			try
			{
				Object res = j.evaluate(simp);
				System.out.println("Value:\t"+res.toString());
			}
			catch(Exception e2)
			{
				System.out.println("Value:\tnull "+e2.getMessage());
			}
			
		//	Node diff = dv.differentiate(simp,"x");
		//	System.out.print("dfun/dx:\t"); 
		//	bpv.println(diff);
		//	simp = sv.simplify(diff);
		//	System.out.print("Simplified:\t"); 
		//	bpv.println(simp);
			
			System.out.println("Variables");
			((DSymbolTable) j.getSymbolTable()).print(j.getPrintVisitor());
		}
		catch(ParseException e1) { System.out.println("Parse Error: "+e1.getMessage()); }
		catch(IllegalArgumentException e2) { System.out.println(e2.getMessage()); }
		catch(Exception e3) { System.out.println(e3.getMessage()); }
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
				
			System.out.println("dJEPdx - Enter q to quit, rules to print the differentation rules,\ndiff(x^2,x) to differentiate,\neval(x^y,x,2,y,3) to evaluate");	
			System.out.print(prompt);

			while ((command = getCommand()) != null) {
				j.parseExpression(command);
				
				if (j.hasError()) {
					System.out.println(j.getErrorInfo());
				} else {
						// expression is OK, get the value
					processEquation(j.getTopNode());
					// did error occur during evaluation?
					if (j.hasError()) {
						System.out.println(j.getErrorInfo());
					}
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

		if( s.equals("rules"))
		{
			j.getDifferentationVisitor().printDiffRules();
			System.out.println("dJEPdx - Enter q to quit, rules to print the differentation rules,\ndiff(x^2,x) to differentiate,\neval(x^y,x,2,y,3) to evaluate");	
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
