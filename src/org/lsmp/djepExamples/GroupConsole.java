/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

/**
 * GroupConsole - JEP Example Applet
 * Copyright (c) 2000 Richard Morris
 *
 * @author Richard Morris
 */

package org.lsmp.djepExamples;

import org.lsmp.djep.groupJep.*;
import org.lsmp.djep.groupJep.groups.*;
import org.lsmp.djep.groupJep.interfaces.*;
import org.lsmp.djep.groupJep.values.*;
import org.nfunk.jep.*;
import java.io.*;
import java.math.*;
import java.util.*;

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
class GroupConsole {
	
	/** The prompt string */
	private String prompt;
	
	/** The input reader */
	private BufferedReader br;
	/** The Jep instance */
	private GroupJep j;
	/** Name of the current group */
	String groupName="Q";	
	/** where to give a dump of tree after parsing. **/
	boolean dumpTree = false;
	/** whether to print symbol table after each line. **/
	boolean dumpSymbols=false;
	
	/** Constructor */
	public GroupConsole() {
		prompt = "JEP > ";
		br = new BufferedReader(new InputStreamReader(System.in));

	}

	/** Creates a new Console object and calls run() */
	public static void main(String args[]) throws IOException {
		GroupConsole c = new GroupConsole();
		c.run(args);
	}
	
	/** The input loop */
	public void run(String args[]) throws IOException {
		String command="";
		j = new GroupJep(new Rationals()); 
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
			{
				Object val = j.getValueAsObject();
				if(j.hasError())
					System.out.println(j.getErrorInfo());
				else
					System.out.println(val);
			}
		}
		else
		{
			System.out.println("Using rationals. To change group type 'group g' where g is one of: ");
			System.out.println("I - integers, Q - Rationals, Rn - Reals(n dp),"+
					" or Zn for Integers mod n");
			System.out.println("A n, Algebraic Intergers Z(sqrt(n))");
			System.out.println("JEP - Enter q to quit");	
			// no arguments - interactive mode

			System.out.println("Current Group: "+j.getGroup().toString());			
							
			while ((command = getCommand()) != null) 
			{
				try
				{
					Node node = j.parse(command);
					Object value = j.evaluate(node);
					if(value instanceof HasComplexValueI)
						System.out.println(value.toString()+"="
							+((HasComplexValueI) value).getComplexValue());
					else
						System.out.println(value.toString());
				}
				catch(Exception e)
				{
					System.out.println(e.getClass().getName());
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
	}
	// split 
	String[] split(String s)
	{
		StringTokenizer st = new StringTokenizer(s);
		int tokCount = st.countTokens();
		String res[] = new String[tokCount];
		int pos=0;
		while (st.hasMoreTokens()) {
			res[pos++]=st.nextToken();
		}
		return res;	
	}
	/**
	 * Get a command from the input.
	 * @return null if an error occures, or if the user enters a terminating
	 *  command
	 */
	private String getCommand() throws IOException {
		String s;
		
		System.out.print(prompt);

		if (br == null)
			return null;

		if ( (s = br.readLine()) == null)
			return null;

		if (s.equals("q")
			|| s.equals("quit")
			|| s.equals("exit"))
			return null;
		
		if(s.equals("group"))
		{
			System.out.println("Current Group: "+j.getGroup().toString());			
			return getCommand();
		}
		else if(s.startsWith("group"))
		{
		  try
		  {
			groupName = s.substring(6);
			// split into string separated by spaces
			String splits[] = split(groupName);
			System.out.println("Changing group to '"+groupName+"'");
			if(splits[0].equals("Z"))
			{
					j = new GroupJep(new Integers());
			}
			else if(splits[0].equals("Q"))
			{
					j = new GroupJep(new Rationals());
			}
			else if(splits[0].equals("R"))
			{
				j = new GroupJep(new BigReals(
							Integer.parseInt(splits[1]),
							BigDecimal.ROUND_HALF_EVEN ));
			}
			else if(splits[0].equals("P"))
			{
				j = new GroupJep(new PermutationGroup(
							Integer.parseInt(splits[1]))
							);
			}
			else if(splits[0].equals("Zn"))
			{
				j = new GroupJep(new Zn(new BigInteger(splits[1]))); 
			} 
			else if(splits[0].equals("extend") && splits.length == 2)
			{
				RingI ring = (RingI) j.getGroup();
				j = new GroupJep(new FreeGroup(ring, splits[1]));
				j.addStandardConstants();
			}
			else if(splits[0].equals("extend"))
			{
				RingI ring = (RingI) j.getGroup();

				int deg = splits.length-3;
				Number coeffs[] = new Number[deg+1];
				for(int i=0;i<=deg;++i)
					coeffs[i] = ring.valueOf(splits[splits.length-i-1]);
				Polynomial p1 = new Polynomial(ring,splits[1],coeffs);

				j = new GroupJep(new AlgebraicExtension(ring, p1));
				j.addStandardConstants();
			}
			else
				System.out.println("Invalid group: "+groupName);			

			System.out.println("Current Group: "+j.getGroup().toString());			
			return getCommand();
		  }
		  catch(Exception e)
		  {
		  	e.printStackTrace();
			return getCommand();
		  }
		}
		return s;
	}
}
