/*
Created 15-Jul-2006 - Richard Morris
*/
package org.lsmp.djepExamples;

import org.lsmp.djep.mrpe.*;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.lsmp.djep.matrixJep.*;

/** A Console application illustrating the use of the RPE evaluator.
 * The org.lsmp.djep.rpe package is intended to speed up multiple evaluation of the same equation
 * with different values for the variable. 
 * As each equation is only evaluated once this will not show a speed improvement. 
 * @author Richard Morris
 */
public class MRpConsole extends MatrixConsole {
	private static final long serialVersionUID = 2604208990249603097L;
	MRpEval rpe;

	public static void main(String[] args) {
		Console c = new MRpConsole();
		c.run(args);
	}

	public void initialise() {
		super.initialise();
		rpe = new MRpEval((MatrixJep)j);
	}

	public void processEquation(Node node) throws ParseException {
		MatrixJep mj = (MatrixJep) j;

		if(verbose) {
			print("Parsed:\t\t"); 
			println(mj.toString(node));
		}
		Node processed = mj.preprocess(node);
		if(verbose) {
			print("Processed:\t"); 
			println(mj.toString(processed));
		}
					
		Node simp = mj.simplify(processed);
		if(verbose) {
			print("Simplified:\t"); 
			println(mj.toString(simp));
		}	

		MRpCommandList list = rpe.compile(simp);
		MRpRes res = rpe.evaluate(list);
		println(res.toString());
	}

	public String getPrompt() {
		return "RPE > ";
	}

	public void printIntroText() {
		println("RPE Console.");
		printStdHelp();
	}

}
