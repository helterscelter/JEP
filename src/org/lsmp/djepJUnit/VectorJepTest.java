package org.lsmp.djepJUnit;

import junit.framework.*;
import org.nfunk.jep.*;
import org.lsmp.djep.vectorJep.*;
/* @author rich
 * Created on 19-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */

/**
 * JUnit test for VectorJep
 * 
 * @author Rich Morris
 * Created on 19-Nov-2003
 */
public class VectorJepTest extends TestCase {
	VectorJep j;
	public static final boolean SHOW_BAD=false;
	
	public VectorJepTest(String name) {
		super(name);
	}

	public static void main(String args[]) {
		// Create an instance of this class and analyse the file

		TestSuite suite= new TestSuite(MatrixJepTest.class);
//		DJepTest jt = new DJepTest("DJepTest");
//		jt.setUp();
		suite.run(new TestResult());
	}	

	protected void setUp() {
		j = new VectorJep();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		//j.setTraverse(true);
		j.setAllowAssignment(true);
		j.setAllowUndeclared(true);
		j.setImplicitMul(true);
	}

	public static Test suite() {
		return new TestSuite(VectorJepTest.class);
	}

	public void testGood()
	{
		assertEquals(1,1);
	}

	public void valueTest(String expr,double dub) throws Exception
	{
		valueTest(expr,new Double(dub));
	}
	public void valueTest(String expr,Object expected) throws Exception
	{
		Node node = j.parse(expr);
		Object res = j.evaluate(node);
		assertEquals("<"+expr+">",expected,res);
		System.out.println("Sucess value of <"+expr+"> is "+res);
	}

	public void valueTest(String expr,String expected) throws Exception
	{
		Node node = j.parse(expr);
		Object res = j.evaluate(node);
		assertEquals("<"+expr+">",expected,res.toString());
		System.out.println("Sucess value of <"+expr+"> is "+res.toString());
	}

	public Object calcValue(String expr) throws Exception
	{
		Node node = j.parse(expr);
		Object res = j.evaluate(node);
		return res;
	}
	

	public void testSimpleSum() throws Exception
	{
		valueTest("1+2",3);		
		valueTest("2*6+3",15);		
		valueTest("2*(6+3)",18);
	}
	
	public void testOperators()  throws Exception
	{
//		if(!Operator.OP_MULTIPLY.isDistributiveOver(Operator.OP_ADD))
//			fail("* should be distrib over +");
//		if(Operator.OP_MULTIPLY.isDistributiveOver(Operator.OP_DIVIDE))
//			fail("* should not be distrib over /");
//		if(Operator.OP_MULTIPLY.getPrecedence() > Operator.OP_ADD.getPrecedence())
//			fail("* should have a lower precedence than +");

		valueTest("T=1",1);
		valueTest("F=0",0);
		calcValue("a=F"); calcValue("b=F"); calcValue("c=F");
		valueTest("(a&&(b||c)) == ((a&&b)||(a&&c))",1);
		valueTest("(a||(b&&c)) == ((a||b)&&(a||c))",1);
		calcValue("a=F"); calcValue("b=F"); calcValue("c=T");
		valueTest("(a&&(b||c)) == ((a&&b)||(a&&c))",1);
		valueTest("(a||(b&&c)) == ((a||b)&&(a||c))",1);
		calcValue("a=F"); calcValue("b=T"); calcValue("c=F");
		valueTest("(a&&(b||c)) == ((a&&b)||(a&&c))",1);
		valueTest("(a||(b&&c)) == ((a||b)&&(a||c))",1);
		calcValue("a=F"); calcValue("b=T"); calcValue("c=T");
		valueTest("(a&&(b||c)) == ((a&&b)||(a&&c))",1);
		valueTest("(a||(b&&c)) == ((a||b)&&(a||c))",1);

		calcValue("a=T"); calcValue("b=F"); calcValue("c=F");
		valueTest("(a&&(b||c)) == ((a&&b)||(a&&c))",1);
		valueTest("(a||(b&&c)) == ((a||b)&&(a||c))",1);
		calcValue("a=T"); calcValue("b=F"); calcValue("c=T");
		valueTest("(a&&(b||c)) == ((a&&b)||(a&&c))",1);
		valueTest("(a||(b&&c)) == ((a||b)&&(a||c))",1);
		calcValue("a=T"); calcValue("b=T"); calcValue("c=F");
		valueTest("(a&&(b||c)) == ((a&&b)||(a&&c))",1);
		valueTest("(a||(b&&c)) == ((a||b)&&(a||c))",1);
		calcValue("a=T"); calcValue("b=T"); calcValue("c=T");
		valueTest("(a&&(b||c)) == ((a&&b)||(a&&c))",1);
		valueTest("(a||(b&&c)) == ((a||b)&&(a||c))",1);
	}

	public void testIf()  throws Exception
	{
		valueTest("if(1,2,3)",2);		
		valueTest("if(-1,2,3)",3);		
		valueTest("if(0,2,3)",3);		
		valueTest("if(1,2,3,4)",2);		
		valueTest("if(-1,2,3,4)",3);		
		valueTest("if(0,2,3,4)",4);		
		valueTest("if(0>=0,2,3,4)",2);		
		valueTest("x=3",3);		
		valueTest("if(x==3,1,-1)",1);		
		valueTest("if(x!=3,1,-1)",-1);		
		valueTest("if(x>=3,1,-1)",1);		
		valueTest("if(x>3,1,-1)",-1);		
		valueTest("if(x<=3,1,-1)",1);		
		valueTest("if(x<3,1,-1)",-1);		
	}

	public void testAssign()  throws Exception
	{
		valueTest("x=3",3);
		valueTest("y=3+4",7);
		valueTest("z=x+y",10);
		valueTest("a=b=c=z",10);
		valueTest("b",10);
		valueTest("d=f=a-b",0);
	}

						

	public void myAssertEquals(String msg,String actual,String expected)
	{
		if(!actual.equals(expected))
			System.out.println("Error \""+msg+"\" is \""+actual+" should be "+expected+"\"");
		assertEquals("<"+msg+">",expected,actual);
		System.out.println("Success: Value of \""+msg+"\" is \""+actual+"\"");
	}

	public void testMatrix() throws Exception
	{
		j.getSymbolTable().clearValues();
		valueTest("x=2",2);
		valueTest("(x*x)*x*(x*x)",32.0);
		valueTest("y=[x^3,x^2,x]","[8.0,4.0,2.0]");
		valueTest("z=[3*x^2,2*x,1]","[12.0,4.0,1.0]");
		valueTest("w=y^^z","[-4.0,16.0,-16.0]");
		valueTest("w.y","0.0");
		valueTest("w.z","0.0");
		valueTest("sqrt(w . z)","0.0"); // tests result is unwrapped from scaler
		valueTest("sqrt([3,4] . [3,4])","5.0"); // tests result is unwrapped from scaler
		valueTest("y+z","[20.0,8.0,3.0]");
		valueTest("y-z","[-4.0,0.0,1.0]");
		valueTest("3*y","[24.0,12.0,6.0]");
		valueTest("y*4","[32.0,16.0,8.0]");
		valueTest("y*z","[[96.0,32.0,8.0],[48.0,16.0,4.0],[24.0,8.0,2.0]]");
		valueTest("z*y","[[96.0,48.0,24.0],[32.0,16.0,8.0],[8.0,4.0,2.0]]");
		j.getSymbolTable().clearValues();
		j.evaluate(j.parse("y=[cos(x),sin(x)]"));
		j.evaluate(j.parse("z=[-sin(x),cos(x)]"));
		valueTest("y . y","1.0");
		valueTest("y . z","0.0");
		valueTest("z . z","1.0");
		j.getSymbolTable().clearValues();
		valueTest("x=[[1,2],[3,4]]","[[1.0,2.0],[3.0,4.0]]");
		valueTest("y=[1,-1]","[1.0,-1.0]");
		valueTest("x*y","[-1.0,-1.0]");			
		valueTest("y*x","[-2.0,-2.0]");
		valueTest("x+[y,y]","[[2.0,1.0],[4.0,3.0]]");	
		valueTest("ele(y,1)","1.0");              // Value: 2.0
		valueTest("ele(y,2)","-1.0");              // Value: 2.0
		valueTest("ele(x,[1,1])","1.0");          // Value: 2.0
		valueTest("ele(x,[1,2])","2.0");          // Value: 2.0
		valueTest("ele(x,[2,1])","3.0");          // Value: 2.0
		valueTest("ele(x,[2,2])","4.0");          // Value: 2.0
	}
	public void testBad() throws Exception
	{
		if(SHOW_BAD)
		{
		}
	}
}
