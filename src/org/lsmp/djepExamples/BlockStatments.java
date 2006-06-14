package org.lsmp.djepExamples;
import java.util.Stack;

import org.nfunk.jep.*;

/**
* Example code illustrating how block control structures could be implemented.
* <p>
* Sample session
* <pre>
JEP > a=3
3.0
JEP > IF a>2
JEP  > IF a>3
JEP   > b=4
skipped
JEP   > ELSE
JEP   > b=3
3.0
JEP   > ENDIF
JEP  > ELSE
JEP  > b=2
skipped
JEP  > ENDIF
JEP > b
3.0
JEP >
*</pre>
*This code does currently allow looping statements.
**/

public class BlockStatments extends Console {
	
	private static final long serialVersionUID = 9035584745289937584L;

	/** Indicates current state where in */
	protected Stack states = new Stack();
	protected Stack conds= new Stack();
	
	/** Creates a new Console object and calls run() */
	public static void main(String args[]) {
		Console c = new BlockStatments();
		c.run(args);
	}

	/** 
	 * Catches macros which are not handled by JEP
	 * 
	 * @return false - stops further processing of the line
	 */
	public boolean testSpecialCommands(String command) 
	{	
		String trim = command.trim();
		if(trim.startsWith("IF "))
		{
			String tail = trim.substring(3);
			try
			{
				Node n = j.parse(tail);
				Object res = j.evaluate(n);
				if( (res instanceof Boolean && ((Boolean) res).booleanValue() )
						|| (res instanceof Number  && ((Number) res).intValue() != 0) )
					conds.push(Boolean.TRUE);
				else
					conds.push(Boolean.FALSE);
				states.push(Boolean.TRUE);
				return false;
			}
			catch(ParseException e) {
				handleError(e);
			}
		}
		if(command.equals("ELSE"))
		{
			states.pop();
			states.push(Boolean.FALSE);
			return false;
		}
		if(command.equals("ENDIF")) {
			if(states.isEmpty())
				println("ERROR: Too many ENDIF statements");
			states.pop();
			conds.pop();
			return false;
		}
		if(command.equals("states"))
		{
			print("STATES: ");
			for(int i=0;i<states.size();++i)
				if(((Boolean)states.elementAt(i)).booleanValue()) 
					 print("T");
				else print("F");
			println("");
			print("CONDS:  ");
			for(int i=0;i<conds.size();++i)
				if(((Boolean)conds.elementAt(i)).booleanValue()) 
					 print("T");
				else print("F");
			println("");
			return false;
		}
		return true;
	}

	/** Evaluates a node, but only if the state corresponds to the conditionValue.
	 * Also saves the result of evaluation in conditionValue for use in subsequent calls
	 *  
	 * @param node Node representing expression
	 * @throws ParseException if a Parse or evaluation error
	 */ 
	public void processEquation(Node node) throws ParseException
	{
		for(int i=0;i<states.size();++i)
			if(!states.elementAt(i).equals(conds.elementAt(i)))
			{
				println("skipped");
				return;
			}
		Object res = j.evaluate(node);
		println(res);
	}
	
	/** Prints introductory text. */
	public void printIntroText() {
		println("JEP Console with basic IF ELSE ENDIF nested block structure.");
		printStdHelp();
	}

	public String getPrompt() { 
		StringBuffer sb = new StringBuffer("JEP ");
		for(int i=0;i<states.size();++i)
			sb.append(' ');
		sb.append("> ");
		return sb.toString();
	}

	public void printHelp() {
		super.printHelp();
		println("'IF condition' begins a (true) block");
		println("'ELSE' begins a false block");
		println("'ENDIF' ends a block");
		println("'states' shows the current states and conditions");
	}

}