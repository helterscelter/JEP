/* @author rich
 * Created on 26-Feb-2004
 */

package org.lsmp.djepExamples;
import java.util.Random;

import org.nfunk.jep.*;
import org.lsmp.djep.rpe.*;
/**
 * Compares the speed of evaluation between normal jep, rpe, and occasionally java.
 * <p>
 * If you have some nice complicated examples, I'd love to
 * hear about them to see if we can tune things up. - rich
 */
public class RpSpeed {
	static JEP j;
	static int num_itts = 1000000; // for normal use
//	static int num_itts = 1000;	  // for use with profiler
	static long seed; // seed for random number generator
	static int num_vals = 1000; // number of random numbers selected
	public static void main(String args[])	{
		long t1 = System.currentTimeMillis();
		initJep();
		long t2 = System.currentTimeMillis();
		System.out.println("Jep initialise "+(t2-t1));

		doAll("1*2*3+4*5*6+7*8*9",new String[]{});
		doAll("x1*x2*x3+x4*x5*x6+x7*x8*x9",new String[]{"x1","x2","x3","x4","x5","x6","x7","x8","x9"});
		doAll("cos(x)^2+sin(x)^2",new String[]{"x"});
		doCos();
		doAll("5",new String[]{});
		doAll("x",new String[]{"x"});
		doAll("1+x",new String[]{"x"});
		doAll("x^2",new String[]{"x"});
		doAll("x*x",new String[]{"x"});
		doAll("5*x",new String[]{"x"});
		doAll("cos(x)",new String[]{"x"});
		doAll("1+x+x^2",new String[]{"x"});
		doAll("1+x+x^2+x^3",new String[]{"x"});
		doAll("1+x+x^2+x^3+x^4",new String[]{"x"});
		doAll("1+x+x^2+x^3+x^4+x^5",new String[]{"x"});
		doAll("1+x(1+x(1+x(1+x(1+x))))",new String[]{"x"});
		doHorner();
	}
	
	/** Run speed comparison  between jep and rpe.
	 * 
	 * @param eqn The equation to test
	 * @param varNames an array of variable names which will be set to random values.
	 */
	public static void doAll(String eqn,String varNames[])
	{
		System.out.println("\nTesting speed for \""+eqn+"\"");
		seed = System.currentTimeMillis();
		try {
			j.parse(eqn);
		} catch(Exception e) {};
		Variable vars[] = new Variable[varNames.length];
		Double varVals[][] = new Double[varNames.length][num_vals];
		Random generator = new Random(seed);
		for(int i=0;i<vars.length;++i)
		{
			vars[i] = j.getVar(varNames[i]);
			for(int j=0;j<num_vals;++j)
				varVals[i][j] = new Double(generator.nextDouble());
		}
	
		doJep(eqn,vars,varVals);
		doRpe(eqn,vars,varVals);
		//System.out.println();
	}

	static void initJep()
	{
		j = new JEP();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		j.setAllowUndeclared(true);
		j.setImplicitMul(true);
		j.setAllowAssignment(true);
	}
	
	static void doJep(String eqn2,Variable vars[],Double vals[][])
	{
	//	System.out.println("vec init"+(t4-t3));
		try
		{
			Node node = j.parse(eqn2);
	//		System.out.println("vec parse"+(t1-t4));
			
			long t1 = System.currentTimeMillis();
			for(int i=0;i<num_itts;++i)
			{
				for(int j=0;j<vars.length;++j)
					vars[j].setValue(vals[j][i%num_vals]);
				j.evaluate(node);
			}
			long t2 = System.currentTimeMillis();
			System.out.println("Using Jep:\t"+(t2-t1));
		}
		catch(Exception e) {System.out.println("Error"+e.getMessage());}
	}

	static void doRpe(String eqn2,Variable vars[],Double vals[][])
	{
		try
		{
			Node node3 = j.parse(eqn2);
			RpEval rpe = new RpEval(j);
			RpCommandList list = rpe.compile(node3);
			int refs[]=new int[vars.length];
			for(int i=0;i<vars.length;++i)
				refs[i]=rpe.getVarRef(vars[i]);
			long t1 = System.currentTimeMillis();
	//		System.out.println("mat parse"+(t1-t4));
			for(int i=0;i<num_itts;++i)
			{
				for(int j=0;j<vars.length;++j)
					rpe.setVarValue(refs[j],vals[j][i%num_vals].doubleValue());
				rpe.evaluate(list);
			}
			long t2 = System.currentTimeMillis();
			System.out.println("Using RpEval2:\t"+(t2-t1));
			rpe.cleanUp();
		}
		catch(Exception e) {System.out.println("Error"+e.getMessage());e.printStackTrace();}
	}
	
	static void doCos()
	{
		Double varVals[] = new Double[num_vals];
		Random generator = new Random(seed);
		for(int j=0;j<num_vals;++j)
			varVals[j] = new Double(generator.nextDouble());
		
		long t1 = System.currentTimeMillis();
		double x; 
		double y;
		for(int i=0;i<num_itts;++i)
		{
			x = varVals[i%num_vals].doubleValue();
			double c = Math.cos(x);
			double s = Math.sin(x);
			y = c*c+s*s;
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Using Java:\t"+(t2-t1));
	}

	static void doHorner()
	{
		Double varVals[] = new Double[num_vals];
		Random generator = new Random(seed);
		for(int j=0;j<num_vals;++j)
			varVals[j] = new Double(generator.nextDouble());
		
		long t1 = System.currentTimeMillis();
		double x; 
		double y;
		for(int i=0;i<num_itts;++i)
		{
			x = varVals[i%num_vals].doubleValue();
			y = 1+x*(1+x*(1+x*(1+x*(1+x))));
		}
		long t2 = System.currentTimeMillis();
		System.out.println("Using Java:\t"+(t2-t1));
	}

}
