/*
Created 15-Jul-2006 - Richard Morris
*/
package org.lsmp.djepExamples;

import org.lsmp.djep.rpe.*;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

/** A Console application illustrating the use of the RPE evaluator.
 * 
 * @author Richard Morris
 */
public class RpeConsole extends Console {
	private static final long serialVersionUID = 2604208990249603097L;
	RpEval rpe;

	public static void main(String[] args) {
		Console c = new RpeConsole();
		c.run(args);
	}

	public void initialise() {
		super.initialise();
		rpe = new RpEval(j);
	}

	public void processEquation(Node node) throws ParseException {
		RpCommandList list = rpe.compile(node);
		double val = rpe.evaluate(list);
		println(new Double(val));
	}

	public String getPrompt() {
		return "RPE > ";
	}

	public void printIntroText() {
		println("RPE Console.");
		printStdHelp();
	}

}
