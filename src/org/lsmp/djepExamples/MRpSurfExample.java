/* @author rich
 * Created on 01-Apr-2005
 *
 * See LICENSE.txt for license information.
 */
package org.lsmp.djepExamples;

import java.util.Enumeration;
import java.util.Vector;

import org.lsmp.djep.matrixJep.MatrixJep;
import org.lsmp.djep.matrixJep.MatrixVariableI;
import org.lsmp.djep.rpe.MRpCommandList;
import org.lsmp.djep.rpe.MRpEval;
import org.lsmp.djep.rpe.RpObj;
import org.lsmp.djep.vectorJep.VectorJep;
import org.lsmp.djep.vectorJep.values.MVector;
import org.lsmp.djep.vectorJep.values.MatrixValueI;
import org.lsmp.djep.vectorJep.values.Scaler;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.Variable;

/**
 * @author Rich Morris
 * Created on 01-Apr-2005
 */
public class MRpSurfExample {
	MatrixJep mj;
	VectorJep vj;
	MRpEval mrpe=null;
	MRpCommandList allCommands[];
	Node allEqns[];
	Node vecEqns[];
	LVars psVars[];
	MatrixVariableI xVar,yVar;
	Variable xVVar,yVVar;

	public static void main(String args[]) {
		timePrint("\tStart");
		MRpSurfExample surf = new MRpSurfExample();
		timePrint("\tDone init");
		try {
		surf.equationChanged("th=pi*x;phi=pi*y;f=[cos(th) cos(phi),sin(th) cos(phi),sin(phi)];dx=diff(f,x);dy=diff(f,y);dx^dy;");
		surf.vecEquationChanged(new String[]{"th=pi*x;","phi=pi*y;","[cos(th) cos(phi),sin(th) cos(phi),sin(phi)];"});
		timePrint("\tDone parse");

		surf.calcMRPE();
		surf.mrpe.cleanUp();
		timePrint("\tDone mrpe");
		
		surf.calcMJ();
		timePrint("\tDone MJ");
		
		surf.calcVJ();
		timePrint("\tDone VJ");
		} catch(Exception e) { System.out.println(e.getClass().getName()+": "+e.getMessage()); }
	}
	static long oldTime = 0;
	public static void timePrint(String msg) {
		long time = System.currentTimeMillis();
		long timediff = time-oldTime;
		oldTime = time;
		System.out.println(""+timediff+"\t"+msg);
	}
	public MRpSurfExample() {
		vj = new VectorJep();
		vj.setAllowAssignment(true);
		vj.setAllowUndeclared(true);
		vj.setImplicitMul(true);
		vj.addComplex();
		vj.addStandardConstants();
		vj.addStandardFunctions();

		mj = new MatrixJep();
		mj.setAllowAssignment(true);
		mj.setAllowUndeclared(true);
		mj.setImplicitMul(true);
		mj.addComplex();
		mj.addStandardConstants();
		mj.addStandardFunctions();
		mj.addStandardDiffRules();
		mrpe = new MRpEval(mj);
		psVars = new LVars[]{
				new LVars("x",-1.,1.,1000),
				new LVars("y",-1.,1.,1000)};
	}

	public void equationChanged(String text)
	{
		mj.restartParser(text);
		try
		{
			Vector coms = new Vector();
			Vector eqns = new Vector();
			Node n;
			while((n = mj.continueParsing())!=null) {
				Node n2 = mj.preprocess(n);
				MRpCommandList com = mrpe.compile(n2);
				coms.add(com);
				eqns.add(n2);
			}
			int i=0;
			allCommands = new MRpCommandList[coms.size()];
			for(Enumeration en=coms.elements();en.hasMoreElements();++i)
				allCommands[i] = (MRpCommandList) en.nextElement();
			i=0;
			allEqns = new Node[eqns.size()];
			for(Enumeration en=eqns.elements();en.hasMoreElements();++i)
				allEqns[i] = (Node) en.nextElement();
			xVar = (MatrixVariableI) mj.getVar("x");
			yVar = (MatrixVariableI) mj.getVar("y");
		}
		catch(ParseException e) {e.getMessage();}
	}

	public void vecEquationChanged(String lines[])
	{
		try
		{
			Vector coms = new Vector();
			Vector eqns = new Vector();
			vecEqns = new Node[lines.length];
			for(int i=0;i<lines.length;++i) {
				Node n2 = vj.parse(lines[i]);
				vecEqns[i]=n2;
			}
			xVVar = vj.getVar("x");
			yVVar = vj.getVar("y");
		}
		catch(ParseException e) {e.getMessage();}
	}


	public void calcMRPE() {
//		System.out.println("Num vertieies: "+m_geom.getNumVertices());
//		System.out.println("x steps "+psVars[0].steps);
		int index=0;
		for(int i=0;i<=psVars[0].steps;++i) {
			double x = psVars[0].min + ((psVars[0].max - psVars[0].min)*i)/psVars[0].steps;
			mrpe.setVarValue(psVars[0].ref,x);
			for(int j=0;j<=psVars[1].steps;++j) {
				double y = psVars[1].min + ((psVars[1].max - psVars[1].min)*j)/psVars[1].steps;
				mrpe.setVarValue(psVars[1].ref,y);

				RpObj res=null;
				for(int k=0;k<allCommands.length;++k)
					res = mrpe.evaluate(allCommands[k]);
				double topRes[] = (double []) res.toArray();
				
				//System.out.println("["+x+","+y+"]->["+topRes[0]+","+topRes[1]+","+topRes[2]+"]");
			}
		}
	}

	public void calcMJ() throws ParseException {
//		System.out.println("Num vertieies: "+m_geom.getNumVertices());
//		System.out.println("x steps "+psVars[0].steps);
		int index=0;
		Scaler xVal = (Scaler) xVar.getMValue();
		Scaler yVal = (Scaler) yVar.getMValue();
		xVar.setValidValue(true);
		yVar.setValidValue(true);
		for(int i=0;i<=psVars[0].steps;++i) {
			double x = psVars[0].min + ((psVars[0].max - psVars[0].min)*i)/psVars[0].steps;
			xVal.setEle(0,new Double(x));
			for(int j=0;j<=psVars[1].steps;++j) {
				double y = psVars[1].min + ((psVars[1].max - psVars[1].min)*j)/psVars[1].steps;
				yVal.setEle(0,new Double(y));

				Object res=null;
				for(int k=0;k<allEqns.length;++k)
					res = mj.evaluate(allEqns[k]);
				Object topRes[] = ((MVector) res).getEles();
				
				//System.out.println("["+x+","+y+"]->["+topRes[0]+","+topRes[1]+","+topRes[2]+"]");
			}
		}
	}

	public void calcVJ() throws ParseException,Exception {
//		System.out.println("Num vertieies: "+m_geom.getNumVertices());
//		System.out.println("x steps "+psVars[0].steps);
		int index=0;
		for(int i=0;i<=psVars[0].steps;++i) {
			double x = psVars[0].min + ((psVars[0].max - psVars[0].min)*i)/psVars[0].steps;
			xVVar.setValue(new Double(x));
			for(int j=0;j<=psVars[1].steps;++j) {
				double y = psVars[1].min + ((psVars[1].max - psVars[1].min)*j)/psVars[1].steps;
				yVVar.setValue(new Double(y));

				Object res=null;
				for(int k=0;k<vecEqns.length;++k)
					res = vj.evaluate(vecEqns[k]);
				Object topRes[] = ((MVector) res).getEles();
				
				//System.out.println("["+x+","+y+"]->["+topRes[0]+","+topRes[1]+","+topRes[2]+"]");
			}
		}
	}

	class LVars {
		String name;
		double min,max;
		int steps;
		int ref;
		LVars(String name,Double min,Double max,Integer steps) {
			this.name = name;
			this.min = min.doubleValue();
			this.max = max.doubleValue();
			this.steps = steps.intValue();
		}
		LVars(String name,double min,double max,int steps) {
			this.name = name;
			this.min = min;
			this.max = max;
			this.steps = steps;
		}
		void set(double min,double max,int steps) {
			this.min = min;
			this.max = max;
			this.steps = steps;
		}
	}

}
