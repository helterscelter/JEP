/**
 * Evaluator
 * JEP Example Applet
 *
 * @author Nathan Funk
 */
package org.nfunk.jepexamples;

import java.io.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;

/**
* This class is designed for testing the validity of JEP evaluations.
* Expressions from a text file are evaluated with JEP in pairs of two, and
* the result are compared. If they do not match the two expressions are 
* printed to standard output.<p>
* Take for example an input text file containing the two lines
* <pre>1+2
* 3</pre>.
* The expressions '1+2' and '3' are evaluated with JEP and the results compared.
*/
public class JEPTester {

	/** The parser */
	JEP myParser;
	
	/** Current line position */
	int lineCount;

	/**
	 * Constructor
	 */
	public JEPTester() {
		// Set up the parser
		myParser = new JEP();
		myParser.setImplicitMul(true);
		myParser.addStandardFunctions();
		myParser.addStandardConstants();
		myParser.addComplex();
		myParser.setTraverse(false);
		lineCount = 0;
	}
	
	/**
	 * The main method checks the arguments and creates an instance
	 * and calls it's run method.
	 */
	public static void main(String args[]) {
		String fileName;
		
		// get filename from argument, or use default
		if (args!=null && args.length>0) {
			fileName = args[0];
		} else {
			fileName = "JEPTesterExpressions.txt";
			println("Using default input file: " + fileName);
			println("Start with \"java org.nfunk.jepexamples."+
			"JEPTester <filename>\" to load a different input file.");
		}
		
		// Create an instance of this class and analyse the file
		JEPTester jt = new JEPTester();
		jt.run(fileName);
	}

	/**
	 * Loads the file specified in fileName. Evaluates the expressions listed
	 * in it and compares the expressions with the results.
	 */
	public void run(String fileName) {
		BufferedReader reader;
		Complex c1, c2;

		// Load the input file
		try {
			reader = new BufferedReader(new FileReader(fileName));
		} catch (Exception e) {
			println("File \""+fileName+"\" not found");
			return;
		}
		
		// reset the line count
		lineCount = 0;
		
		// cycle through the expressions in pairs of two
		println("Evaluating and comparing expressions...");
		while (true) {
			c1 = parseNextLine(reader);
			c2 = parseNextLine(reader);

			if (c1==null || c2==null) break;
			
			// TODO: add comparison method that handles all types (Strings...)
			if (!c1.equals(c2,1e-15)) {
				print("Line: " + lineCount + ": ");
				if (c1.im() == 0)
					print("" + c1.re() + " != ");
				else
					print("" + c1 + " != ");

				if (c2.im() == 0)
					println("" + c2.re());
				else
					println("" + c2);
			}
		}
	}
	
	/**
	 * Parses a single line from the reader, and returns the
	 * evaluation of that line.
	 */
	private Complex parseNextLine(BufferedReader reader) {
		Complex value;
		String line, errorStr;
		
		// cycle till a valid line is found
		do {
			try {
				line = reader.readLine();
				lineCount++;
			} catch (Exception e) {
				return null;
			}

			if (line==null) return null;

		} while (line.length()==0 || line.trim().charAt(0)=='#');
			
		// parse the expression
		myParser.parseExpression(line);
		// did an error occur while parsing?
		errorStr = myParser.getErrorInfo();
		if (errorStr != null) {
			println("Error while parsing line " + lineCount + ": " + errorStr);
			return null;
		}
		
		// evaluate the expression
		value = myParser.getComplexValue();
		// did an error occur while evaluating?
		errorStr = myParser.getErrorInfo();
		if ((value == null) || (errorStr != null)) {
			println("Error while evaluating line " + lineCount + ": " + errorStr);
			return null;
		}
			
		return value;
	}
	
	/**
	 * Helper function for printing.
	 */
	private static void print(String str) {
		System.out.print(str);
	}

	/**
	 * Helper function for printing lines.
	 */
	private static void println(String str) {
		System.out.println(str);
	}
}
