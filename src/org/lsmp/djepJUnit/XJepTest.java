package org.lsmp.djepJUnit;

import junit.framework.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import org.lsmp.djep.xjep.*;
import org.lsmp.djep.xjep.rewriteRules.*;

/* @author rich
 * Created on 19-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */

/**
 * @author Rich Morris
 * Created on 19-Nov-2003
 */
public class XJepTest extends TestCase {
	XJep j;
	public static final boolean SHOW_BAD=false;
	
	public XJepTest(String name) {
		super(name);
	}

	public static void main(String args[]) {
		// Create an instance of this class and analyse the file

		TestSuite suite= new TestSuite(DJepTest.class);
//		DJepTest jt = new DJepTest("DJepTest");
//		jt.setUp();
		suite.run(new TestResult());
	}	

	protected void setUp() {
		j = new XJep();
		j.addStandardConstants();
		j.addStandardFunctions();
		j.addComplex();
		//j.setTraverse(true);
		j.setAllowAssignment(true);
		j.setAllowUndeclared(true);
		j.setImplicitMul(true);
	}

	public static Test suite() {
		return new TestSuite(XJepTest.class);
	}

	public void myAssertEquals(String msg,String actual,String expected)
	{
		if(!actual.equals(expected))
			System.out.println("Error \""+msg+"\" is \""+actual+" should be "+expected+"\"");
		assertEquals("<"+msg+">",expected,actual);
		System.out.println("Success: Value of \""+msg+"\" is \""+actual+"\"");
	}
	/** just test JUnit working OK */
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
	public void complexValueTest(String expr,Complex expected,double tol) throws Exception
	{
		Node node = j.parse(expr);
		Object res = j.evaluate(node);
		assertTrue("<"+expr+"> expected: <"+expected+"> but was <"+res+">",
			expected.equals((Complex) res,tol));
		System.out.println("Sucess value of <"+expr+"> is "+res);
	}

	public Object calcValue(String expr)
	{
		j.parseExpression(expr);
		if(j.hasError())
			fail("Parse Failure: "+expr);
		return j.getValueAsObject();
	}
	
	public void simplifyTest(String expr,String expected) throws ParseException
	{
		Node node = j.parse(expr);
		Node processed = j.preprocess(node);
		Node simp = j.simplify(processed);
		String res = j.toString(simp);
		
		Node node2 = j.parse(expected);
		Node processed2 = j.preprocess(node2);
		Node simp2 = j.simplify(processed2);
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
		Node processed = j.preprocess(node);
		Node simp = j.simplify(processed);
		String res = j.toString(simp);
		
		if(!expected.equals(res))		
			System.out.println("Error: Value of \""+expr+"\" is \""+res+"\" should be \""+expected+"\"");
		assertEquals("<"+expr+">",expected,res);
		System.out.println("Sucess: Value of \""+expr+"\" is \""+res+"\"");
			
//		System.out.print("Full Brackets:\t");
//		j.pv.setFullBrackets(true);
//		j.pv.println(simp);
//		j.pv.setFullBrackets(false);

	}
	
	public Node parseProcSimpEval(String expr,Object expected) throws ParseException,Exception
	{
		Node node = j.parse(expr);
		Node processed = j.preprocess(node);
		Node simp = j.simplify(processed);
		Object res = j.evaluate(simp);
		
		if(!expected.equals(res))		
			System.out.println("Error: Value of \""+expr+"\" is \""+res+"\" should be \""+expected+"\"");
		assertEquals("<"+expr+">",expected,res);
		System.out.println("Sucess: Value of \""+expr+"\" is \""+res+"\"");
		return simp;
	}


	public void testSimpleSum() throws Exception
	{
		valueTest("1+2",3);		
		valueTest("2*6+3",15);		
		valueTest("2*(6+3)",18);
	}
	
	public void testOperators() throws Exception
	{
		OperatorSet opSet = j.getOperatorSet();
		if(!((XOperator) opSet.getMultiply()).isDistributiveOver(opSet.getAdd()))
			fail("* should be distrib over +");
		if(((XOperator) opSet.getMultiply()).isDistributiveOver(opSet.getDivide()))
			fail("* should not be distrib over /");
		if(((XOperator) opSet.getMultiply()).getPrecedence() > ((XOperator) opSet.getAdd()).getPrecedence())
			fail("* should have a lower precedence than +");

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
	
	public void testPrint() throws ParseException
	{
		simplifyTestString("(a+b)+c","a+b+c");
		simplifyTestString("(a-b)+c","a-b+c");
		simplifyTestString("(a+b)-c","a+b-c"); 
		simplifyTestString("(a-b)-c","a-b-c");

		simplifyTestString("a+(b+c)","a+b+c");
		simplifyTestString("a-(b+c)","a-(b+c)");
		simplifyTestString("a+(b-c)","a+b-c");   
		simplifyTestString("a-(b-c)","a-(b-c)");

		simplifyTestString("(a*b)*c","a*b*c");
		simplifyTestString("(a/b)*c","(a/b)*c");
		simplifyTestString("(a*b)/c","a*b/c"); 
		simplifyTestString("(a/b)/c","(a/b)/c");

		simplifyTestString("a*(b*c)","a*b*c");
		simplifyTestString("a/(b*c)","a/(b*c)");
		simplifyTestString("a*(b/c)","a*b/c");
		simplifyTestString("a/(b/c)","a/(b/c)");

		simplifyTestString("a=(b=c)","a=b=c");
		//simplifyTestString("(a=b)=c","a/(b/c)");

		simplifyTestString("(a*b)+c","a*b+c");
		simplifyTestString("(a+b)*c","(a+b)*c");
		simplifyTestString("a*(b+c)","a*(b+c)"); 
		simplifyTestString("a+(b*c)","a+b*c");

		simplifyTestString("(a||b)||c","a||b||c");
		simplifyTestString("(a&&b)||c","a&&b||c");
		simplifyTestString("(a||b)&&c","(a||b)&&c"); 
		simplifyTestString("(a&&b)&&c","a&&b&&c");

		simplifyTestString("a||(b||c)","a||b||c");
		simplifyTestString("a&&(b||c)","a&&(b||c)");
		simplifyTestString("a||(b&&c)","a||b&&c");   
		simplifyTestString("a&&(b&&c)","a&&b&&c");
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

	public void testMacroFun() throws Exception
	{
		j.addFunction("zap",new MacroFunction("zap",1,"x*(x-1)/2",j));
		valueTest("zap(10)",45);
	}
	public void testVariableReuse() throws Exception
	{
		System.out.println("\nTesting variable reuse");
		parseProcSimpEval("x=3",new Double(3));
		Node node13 = parseProcSimpEval("y=x^2",new Double(9));
		Node node15 = parseProcSimpEval("z=y+x",new Double(12));
			
		j.setVarValue("x",new Double(4));
		System.out.println("j.setVarValue(\"x\",new Double(4));");
		System.out.println("j.getVarValue(y): "+j.getVarValue("y"));
		myAssertEquals("eval y eqn",j.evaluate(node13).toString(),"16.0");
		System.out.println("j.getVarValue(y): "+j.getVarValue("y"));
		myAssertEquals("eval z eqn",j.evaluate(node15).toString(),"20.0");

//		j.getSymbolTable().clearValues();
		j.setVarValue("x",new Double(5));
		System.out.println("j.setVarValue(\"x\",new Double(5));");
		myAssertEquals("j.findVarValue(y)",j.calcVarValue("y").toString(),"25.0");
		myAssertEquals("j.findVarValue(z)",j.calcVarValue("z").toString(),"30.0");

		j.getSymbolTable().clearValues();
		j.setVarValue("x",new Double(6));
		System.out.println("j.setVarValue(\"x\",new Double(6));");
		myAssertEquals("j.findVarValue(z)",j.calcVarValue("z").toString(),"42.0");
		myAssertEquals("j.findVarValue(y)",j.calcVarValue("y").toString(),"36.0");

		parseProcSimpEval("x=7",new Double(7));
		myAssertEquals("eval y eqn",j.evaluate(node13).toString(),"49.0");
		myAssertEquals("eval z eqn",j.evaluate(node15).toString(),"56.0");
	}
	
	public void testDotInName() throws ParseException,Exception
	{
		valueTest("x.x=3",3);
		valueTest("x.x+1",4);
	}

	public void testReentrant() throws ParseException,Exception
	{
		j.restartParser("x=1; // semi-colon; in comment\n y=2; z=x+y;");
		Node node = j.continueParsing();
		myAssertEquals("x=1; ...",j.evaluate(node).toString(),"1.0");
		node = j.continueParsing();
		myAssertEquals("..., y=2; ...",j.evaluate(node).toString(),"2.0");
		node = j.continueParsing();
		myAssertEquals("..., z=x+y;",j.evaluate(node).toString(),"3.0");
		node = j.continueParsing();
		assertNull("empty string ",node);
	}
	
	public void testRewrite()  throws ParseException,Exception
	{
		RewriteVisitor rwv = new RewriteVisitor();
		ExpandBrackets eb = new ExpandBrackets(j);
		ExpandPower ep = new ExpandPower(j);

		Node n1 = j.parse("(a+b)*(c+d)");
		Node n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{eb},false);
		myAssertEquals("expand((a+b)*(c+d))",j.toString(n2),"a*c+a*d+b*c+b*d");

		n1 = j.parse("(a+b)*(a+b)");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{eb},false);
		myAssertEquals("expand((a+b)*(a+b))",j.toString(n2),"a*a+a*b+b*a+b*b");

		n1 = j.parse("(a-b)*(a-b)");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{eb},true);
		myAssertEquals("expand((a+b)*(a+b))",j.toString(n2),"a*a-a*b-(b*a-b*b)");

		n1 = j.parse("(x+7.6)*(x+5.8832)*(x-55.12)");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{eb},false);
		Node n3 = rwv.rewrite(n1,j,new RewriteRuleI[]{eb},true);
		j.println(n2);
		j.println(n3);
		
		n1 = j.parse("(a+b)^0");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a+b)^0",j.toString(n2),"1.0");

		n1 = j.parse("(a-b)^0");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a-b)^0",j.toString(n2),"1.0");

		n1 = j.parse("(a+b)^1");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a+b)^1",j.toString(n2),"a+b");

		n1 = j.parse("(a-b)^1");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a-b)^1",j.toString(n2),"a-b");

		n1 = j.parse("(a+b)^2");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a+b)^2",j.toString(n2),"a^2.0+2.0*a*b+b^2.0");

		n1 = j.parse("(a-b)^2");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a-b)^2",j.toString(n2),"a^2.0-(2.0*a*b-b^2.0)");

		n1 = j.parse("(a+b)^3");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a+b)^3",j.toString(n2),"a^3.0+3.0*a^2.0*b+3.0*a*b^2.0+b^3.0");

		n1 = j.parse("(a-b)^3");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a-b)^3",j.toString(n2),"a^3.0-(3.0*a^2.0*b-(3.0*a*b^2.0-b^3.0))");

		n1 = j.parse("(a+b)^4");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a+b)^4",j.toString(n2),"a^4.0+4.0*a^3.0*b+6.0*a^2.0*b^2.0+4.0*a*b^3.0+b^4.0");

		n1 = j.parse("(a-b)^4");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a-b)^4",j.toString(n2),"a^4.0-(4.0*a^3.0*b-(6.0*a^2.0*b^2.0-(4.0*a*b^3.0-b^4.0)))");
		
		n1 = j.parse("(a+b)^5");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a+b)^5",j.toString(n2),"a^5.0+5.0*a^4.0*b+10.0*a^3.0*b^2.0+10.0*a^2.0*b^3.0+5.0*a*b^4.0+b^5.0");

		n1 = j.parse("(a-b)^5");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},false);
		myAssertEquals("(a-b)^5",j.toString(n2),"a^5.0-(5.0*a^4.0*b-(10.0*a^3.0*b^2.0-(10.0*a^2.0*b^3.0-(5.0*a*b^4.0-b^5.0))))");

		n1 = j.parse("(a+1)^5");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},true);
		myAssertEquals("(a+1)^5",j.toString(n2),"a^5.0+5.0*a^4.0+10.0*a^3.0+10.0*a^2.0+1.0+5.0*a");

		n1 = j.parse("(a-1)^5");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},true);
		myAssertEquals("(a-1)^5",j.toString(n2),"a^5.0-(5.0*a^4.0-(10.0*a^3.0-(10.0*a^2.0-(5.0*a-1.0))))");

		n1 = j.parse("(a+2)^5");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep},true);
		myAssertEquals("(a+1)^5",j.toString(n2),"a^5.0+10.0*a^4.0+40.0*a^3.0+80.0*a^2.0+32.0+80.0*a");

		j.getPrintVisitor().setMaxLen(80);
		n1=j.parse("(xx^2+yy^2+zz^2+ww^2)^8");
		n2 = rwv.rewrite(n1,j,new RewriteRuleI[]{ep,eb},true);

		j.getPrintVisitor().setMaxLen(80);
		j.println(n2);		
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
