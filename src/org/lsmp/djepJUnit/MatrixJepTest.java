package org.lsmp.djepJUnit;

import junit.framework.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import org.lsmp.djep.djep.*;
import org.lsmp.djep.matrixJep.*;
/* @author rich
 * Created on 19-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */

/**
 * JUnit test for full MatrixJep
 * 
 * @author Rich Morris
 * Created on 19-Nov-2003
 */
public class MatrixJepTest extends TestCase {
	MatrixJep j;
	public static final boolean SHOW_BAD=false;
	
	public MatrixJepTest(String name) {
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
		j = new MatrixJep();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		//j.setTraverse(true);
		j.setAllowAssignment(true);
		j.setAllowUndeclared(true);
		j.setImplicitMul(true);
		j.addStandardDiffRules();
	}

	public static Test suite() {
		return new TestSuite(MatrixJepTest.class);
	}

	public void testGood()
	{
		assertEquals(1,1);
	}

	public void valueTest(String expr,double dub) throws ParseException
	{
		valueTest(expr,new Double(dub));
	}
	public void valueTest(String expr,Object expected) throws ParseException
	{
		Node node = j.parse(expr);
		Node matEqn = j.preprocess(node);
		Object res = j.evaluate(matEqn);
		if(j.hasError())
			fail("Evaluation Failure: "+expr+j.getErrorInfo());
		assertEquals("<"+expr+">",expected,res);
		System.out.println("Sucess value of <"+expr+"> is "+res);
	}

	public void valueTest(String expr,String expected) throws ParseException
	{
		Node node = j.parse(expr);
		Node matEqn = j.preprocess(node);
		Object res = j.evaluate(matEqn);
		if(j.hasError())
			fail("Evaluation Failure: "+expr+j.getErrorInfo());
		assertEquals("<"+expr+">",expected,res.toString());
		System.out.println("Sucess value of <"+expr+"> is "+res.toString());
	}

	public void complexValueTest(String expr,Complex expected,double tol) throws Exception
	{
		Node node = j.preprocess(j.parse(expr));
		Object res = j.evaluate(node);
		assertTrue("<"+expr+"> expected: <"+expected+"> but was <"+res+">",
			expected.equals((Complex) res,tol));
		System.out.println("Sucess value of <"+expr+"> is "+res);
	}

	public Object calcValue(String expr) throws ParseException
	{
		Node node = j.parse(expr);
		Node matEqn = j.preprocess(node);
		Object res = j.evaluate(matEqn);
		return res;
	}
	
	public void simplifyTest(String expr,String expected) throws ParseException
	{
		Node node = j.parse(expr);
		Node matEqn = j.preprocess(node);
		Node simp = j.simplify(matEqn);
		String res = j.toString(simp);
		
		Node node2 = j.parse(expected);
		Node matEqn2 = j.preprocess(node2);
		Node simp2 = j.simplify(matEqn2);
		String res2 = j.toString(simp2);


		if(!res2.equals(res))		
			System.out.println("Error: Value of \""+expr+"\" is \""+res+"\" should be \""+res2+"\"");
		assertEquals("<"+expr+">",res2,res);
		System.out.println("Sucess: Value of \""+expr+"\" is \""+res+"\"");
			
//		System.out.print("Full Brackets:\t");
//		j.pv.setFullBrackets(true);
//		j.pv.println(simp);
//		j.pv.setFullBrackets(false);

	}

	public void simplifyTestString(String expr,String expected) throws ParseException
	{
		Node node = j.parse(expr);
		Node matEqn = j.preprocess(node);
		String res = j.toString(matEqn);
		
		if(!expected.equals(res))		
			System.out.println("Error: Value of \""+expr+"\" is \""+res+"\" should be \""+expected+"\"");
		assertEquals("<"+expr+">",expected,res);
		System.out.println("Sucess: Value of \""+expr+"\" is \""+res+"\"");
			
//		System.out.print("Full Brackets:\t");
//		j.pv.setFullBrackets(true);
//		j.pv.println(simp);
//		j.pv.setFullBrackets(false);

	}

	public void testSimpleSum() throws ParseException
	{
		valueTest("1+2",3);		
		valueTest("2*6+3",15);		
		valueTest("2*(6+3)",18);
	}
	
	public void testOperators()  throws ParseException
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
	
	public void testSimp() throws ParseException
	{
		simplifyTest("2+3","5");
		simplifyTest("2*3","6");
		simplifyTest("2^3","8");
		simplifyTest("3/2","1.5");
		simplifyTest("2*3+4","10");
		simplifyTest("2*(3+4)","14");

		simplifyTest("0+x","x");
		simplifyTest("x+0","x");
		simplifyTest("0-x","0-x");
		simplifyTest("x-0","x");
		simplifyTest("0*x","0");
		simplifyTest("x*0","0");
		simplifyTest("1*x","x");
		simplifyTest("x*1","x");
		simplifyTest("-1*x","-x");
		simplifyTest("x*-1","-x");
		simplifyTest("-(-x)","x");
		simplifyTest("-(-(-x))","-x");
		simplifyTest("(-1)*(-1)*x","x");
		simplifyTest("(-1)*(-1)*(-1)*x","-x");
		
		simplifyTest("0/x","0");
		simplifyTest("x/0","1/0");
		
		simplifyTest("x^0","1");
		simplifyTest("x^1","x");
		simplifyTest("0^x","0");
		simplifyTest("1^x","1");

		// (a+b)+c
		simplifyTest("(2+3)+x","5+x");
		simplifyTest("(2+x)+3","5+x");
		simplifyTest("(x+2)+3","5+x");
		// a+(b+c)
		simplifyTest("x+(2+3)","5+x");
		simplifyTest("2+(x+3)","5+x");
		simplifyTest("2+(3+x)","5+x");
		// (a+b)-c
		simplifyTest("(2+3)-x","5-x");
		simplifyTest("(2+x)-3","x-1");
		simplifyTest("(x+2)-3","x-1");
		// (a-b)+c
		simplifyTest("(2-3)+x","-1+x");
		simplifyTest("(2-x)+3","5-x");
		simplifyTest("(x-2)+3","1+x");
		// a-(b+c)
		simplifyTest("x-(2+3)","x-5");
		simplifyTest("2-(x+3)","-1-x");
		simplifyTest("2-(3+x)","-1-x");
		// a+(b-c)
		simplifyTest("x+(2-3)","x-1");
		simplifyTest("2+(x-3)","-1+x");
		simplifyTest("2+(3-x)","5-x");
		// a-(b-c)
		simplifyTest("x-(2-3)","1+x");
		simplifyTest("2-(x-3)","5-x");
		simplifyTest("2-(3-x)","-1+x");
		// (a-b)-c
		simplifyTest("(2-3)-x","-1-x");
		simplifyTest("(2-x)-3","-1-x");
		simplifyTest("(x-2)-3","x-5");

		// (a*b)*c
		simplifyTest("(2*3)*x","6*x");
		simplifyTest("(2*x)*3","6*x");
		simplifyTest("(x*2)*3","6*x");
		// a+(b+c)
		simplifyTest("x*(2*3)","6*x");
		simplifyTest("2*(x*3)","6*x");
		simplifyTest("2*(3*x)","6*x");
		// (a+b)-c
		simplifyTest("(2*3)/x","6/x");
		simplifyTest("(3*x)/2","1.5*x");
		simplifyTest("(x*3)/2","1.5*x");
		// (a-b)+c
		simplifyTest("(3/2)*x","1.5*x");
		simplifyTest("(3/x)*2","6/x");
		simplifyTest("(x/2)*3","1.5*x");
		// a-(b+c)
		simplifyTest("x/(2*3)","x/6");
		simplifyTest("3/(x*2)","1.5/x");
		simplifyTest("3/(2*x)","1.5/x");
		// a+(b-c)
		simplifyTest("x*(3/2)","1.5*x");
		simplifyTest("3*(x/2)","1.5*x");
		simplifyTest("3*(2/x)","6/x");
		// a-(b-c)
		simplifyTest("x/(3/2)","x/1.5");
		simplifyTest("2/(x/3)","6/x");
		simplifyTest("3/(2/x)","1.5*x");
		// (a-b)-c
		simplifyTest("(3/2)/x","1.5/x");
		simplifyTest("(3/x)/2","1.5/x");
		simplifyTest("(x/3)/2","x/6");


		simplifyTest("x*(3+2)","5*x");
		simplifyTest("3*(x+2)","6+3*x");
		simplifyTest("3*(2+x)","6+3*x");
		simplifyTest("(3+2)*x","5*x");
		simplifyTest("(3+x)*2","6+2*x");
		simplifyTest("(x+3)*2","6+x*2");

		simplifyTest("x*(3-2)","x");
		simplifyTest("3*(x-2)","-6+3*x");
		simplifyTest("3*(2-x)","6-3*x");
		simplifyTest("(3-2)*x","x");
		simplifyTest("(3-x)*2","6-2*x");
		simplifyTest("(x-3)*2","-6+2*x");

		simplifyTest("3+(x/4)","3+x/4");
		simplifyTest("2*(x/4)","0.5*x");
		simplifyTest("(2*(3+(x/4)))","6+0.5*x");
		simplifyTest("1+(2*(3+(x/4)))","7+0.5*x");
		simplifyTest("((3+(x/4))*2)+1","7+0.5*x");
		
	}

	public void testComplex() throws Exception
	{
		double tol = 0.00000001;

		complexValueTest("z=complex(3,2)",new Complex(3,2),tol);
		complexValueTest("z*z-z",new Complex(2,10),tol);
		complexValueTest("z^3",new Complex(-9,46),tol);
		complexValueTest("(z*z-z)/z",new Complex(2,2),tol);
		complexValueTest("w=polar(2,pi/2)",new Complex(0,2),tol);
		
	}

	public void testIf()  throws ParseException
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

	public void testAssign()  throws ParseException
	{
		valueTest("x=3",3);
		valueTest("y=3+4",7);
		valueTest("z=x+y",10);
		valueTest("a=b=c=z",10);
		valueTest("b",10);
		valueTest("d=f=a-b",0);
	}

						
	public void testDiff() throws ParseException
	{
		simplifyTest("diff(x^2,x)","2 x");
		simplifyTest("diff(x^3,x)","3 x^2");
		simplifyTest("diff(x,x)","1");
		simplifyTest("diff(1,x)","0");
		simplifyTest("diff(x^2+x+1,x)","2 x+1");
		simplifyTest("diff((x+x^2)*(x+x^3),x)","(1+2*x)*(x+x^3)+(x+x^2)*(1+3*x^2)");
		simplifyTest("diff((x+x^2)/(x+x^3),x)","((1+2*x)*(x+x^3)-(x+x^2)*(1+3*x^2))/((x+x^3)*(x+x^3))");
		simplifyTest("diff(sin(x),x)","cos(x)");
		simplifyTest("diff(-(x-5)^3,x)","-(3.0*(x-5.0)^2.0)");
		

		simplifyTest("diff((x+1)^2,x)","2+2*x");
		simplifyTest("diff((x+y)^2,x)","2*(x+y)");
		simplifyTest("diff((x+x^2)^3,x)","3*(x+x^2)^2*(1+2*x)");
		
		simplifyTest("diff(sin(x+1),x)","cos(x+1)");
		simplifyTest("diff(sin(x+x^2),x)","cos(x+x^2)*(1+2*x)");

		simplifyTest("diff(cos(x),x)","-sin(x)"); 	
		simplifyTest("diff(tan(x),x)","1/((cos(x))^2)");

		simplifyTest("diff(sec(x),x)","sec(x)*tan(x)");
		simplifyTest("diff(cosec(x),x)","-cosec(x) * cot(x)");
		simplifyTest("diff(cot(x),x)","-(cosec(x))^2");
		
		simplifyTest("diff(sec(x),x)","sec(x) * tan(x)");
		simplifyTest("diff(cosec(x),x)","-cosec(x) * cot(x)");
		simplifyTest("diff(cot(x),x)","-(cosec(x))^2");
			
		simplifyTest("diff(asin(x),x)","1/(sqrt(1-x^2))");
		simplifyTest("diff(acos(x),x)","-1/(sqrt(1-x^2))");
		simplifyTest("diff(atan(x),x)","1/(1+x^2)");

		simplifyTest("diff(sinh(x),x)","cosh(x)");
		simplifyTest("diff(cosh(x),x)","sinh(x)");
		simplifyTest("diff(tanh(x),x)","1-(tanh(x))^2");

		simplifyTest("diff(asinh(x),x)","1/(sqrt(1+x^2))");
		simplifyTest("diff(acosh(x),x)","1/(sqrt(x^2-1))");
		simplifyTest("diff(atanh(x),x)","1/(1-x^2)");

		simplifyTest("diff(sqrt(x),x)","1/(2 (sqrt(x)))");
		
		simplifyTest("diff(exp(x),x)","exp(x)");
//		simplifyTest("diff(pow(x,y),x)","y*(pow(x,y-1))");
//		simplifyTest("diff(pow(x,y),y)","(ln(x)) (pow(x,y))");
		simplifyTest("diff(ln(x),x)","1/x");
		simplifyTest("diff(log(x),x)","(1/ln(10)) /x");
		simplifyTest("diff(abs(x),x)","abs(x)/x");
		simplifyTest("diff(angle(x,y),x)","y/(x^2+y^2)");
		simplifyTest("diff(angle(x,y),y)","-x/(x^2+y^2)");
		simplifyTest("diff(mod(x,y),x)","1");
		simplifyTest("diff(mod(x,y),y)","0");
		simplifyTest("diff(sum(x,x^2,x^3),x)","sum(1,2 x,3 x^2)");

//		addDiffRule(new PassThroughDiffRule(this,"sum"));
//		addDiffRule(new PassThroughDiffRule(this,"re"));
//		addDiffRule(new PassThroughDiffRule(this,"im"));
//		addDiffRule(new PassThroughDiffRule(this,"rand"));
//		
//		MacroFunction complex = new MacroFunction("complex",2,"x+i*y",xjep);
//		xjep.addFunction("complex",complex);
//		addDiffRule(new MacroFunctionDiffRules(this,complex));
//		
//		addDiffRule(new PassThroughDiffRule(this,"\"<\"",new Comparative(0)));
//		addDiffRule(new PassThroughDiffRule(this,"\">\"",new Comparative(1)));
//		addDiffRule(new PassThroughDiffRule(this,"\"<=\"",new Comparative(2)));
//		addDiffRule(new PassThroughDiffRule(this,"\">=\"",new Comparative(3)));
//		addDiffRule(new PassThroughDiffRule(this,"\"!=\"",new Comparative(4)));
//		addDiffRule(new PassThroughDiffRule(this,"\"==\"",new Comparative(5)));
	}

	public void myAssertEquals(String msg,String actual,String expected)
	{
		if(!actual.equals(expected))
			System.out.println("Error \""+msg+"\" is \""+actual+" should be "+expected+"\"");
		assertEquals("<"+msg+">",expected,actual);
		System.out.println("Success: Value of \""+msg+"\" is \""+actual+"\"");
	}
	public void testAssignDiff() throws ParseException
	{
		simplifyTestString("y=x^5","y=x^5.0");
		simplifyTestString("z=diff(y,x)","z=5.0*x^4.0");
		Node n1 = ((DSymbolTable) j.getSymbolTable()).getPartialDeriv("y",new String[]{"x"}).getEquation();
		myAssertEquals("dy/dx","5.0*x^4.0",j.toString(n1));
		simplifyTestString("w=diff(z,x)","w=20.0*x^3.0");
		Node n2 = ((DSymbolTable) j.getSymbolTable()).getPartialDeriv("y",new String[]{"x","x"}).getEquation();
		myAssertEquals("d^2y/dxdx","20.0*x^3.0",j.toString(n2));
		j.getSymbolTable().clearValues();
		valueTest("x=2",2);
		valueTest("y",32); // x^5
		valueTest("z",80); // 5 x^4 
		valueTest("w",160); // 20 x^3
	}

	public void testMatrix() throws ParseException
	{
		j.getSymbolTable().clearValues();
		valueTest("x=2",2);
		valueTest("y=[x^3,x^2,x]","[8.0,4.0,2.0]");
		valueTest("z=diff(y,x)","[12.0,4.0,1.0]");
		valueTest("3*y","[24.0,12.0,6.0]");
		valueTest("y*4","[32.0,16.0,8.0]");
		valueTest("y*z","[[96.0,32.0,8.0],[48.0,16.0,4.0],[24.0,8.0,2.0]]");
		valueTest("z*y","[[96.0,48.0,24.0],[32.0,16.0,8.0],[8.0,4.0,2.0]]");
		valueTest("w=y^z","[-4.0,16.0,-16.0]");
		simplifyTestString("diff(w,x)","[3.0*x^2.0,2.0*x,1.0]^z+y^[6.0*x,2.0,0.0]");
		simplifyTestString("diff(y . z,x)","[3.0*x^2.0,2.0*x,1.0].z+y.[6.0*x,2.0,0.0]");
		valueTest("w.y",0.0);
		valueTest("w.z",0.0);
		valueTest("sqrt(w . z)",0.0);
		valueTest("sqrt([3,4].[3,4])",5.0); // tests result is unwrapped from scaler
		valueTest("y+z","[20.0,8.0,3.0]");
		valueTest("y-z","[-4.0,0.0,1.0]");
		j.getSymbolTable().clearValues();
		// the following two tests insure that ^ is printed correctly
		simplifyTestString("y^z","y^z");
		simplifyTestString("[8.0,4.0,2.0]^[12.0,4.0,1.0]","[8.0,4.0,2.0]^[12.0,4.0,1.0]");
		simplifyTestString("y=[cos(x),sin(x)]","y=[cos(x),sin(x)]");
		simplifyTestString("z=diff(y,x)","z=[-sin(x),cos(x)]");
		valueTest("y.y",1.0);
		valueTest("y.z",0.0);
		valueTest("z.z",1.0);
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
	public void testBad() throws ParseException
	{
		if(SHOW_BAD)
		{
			simplifyTest("1&&(1||x)","1");
			simplifyTest("diff(sgn(x),x)","0");	// sgn not implemented
			simplifyTest("diff(re(x+i y),x)","1"); // not smart enought to work out re(i) = 1
			simplifyTest("diff(re(x+i y),y)","0");
			simplifyTest("diff(im(x+i y),x)","0");
			simplifyTest("diff(im(x+i y),y)","1");
			simplifyTest("(x/2)*3","x*1.5");
		}
	}
}
