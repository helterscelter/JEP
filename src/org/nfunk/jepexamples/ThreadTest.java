/*
 * ThreadTest
 *
 * 
 * User: matthew.baird
 * Date: Jun 22, 2002
 * Time: 12:35:19 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package org.nfunk.jepexamples;

public class ThreadTest {
	
	static long time = 0;
	
	/**
	 * Start 1000 threads
	 */
	public static void main(String[] args) {
		ThreadTest test = new ThreadTest();
		
		for (int i = 0; i < 1000; i++) {
			ThreadTestThread t = new ThreadTestThread(test);
			t.start();
		}
	}
	
	public ThreadTest() {
	}

	/**
	 * Perform a simple evaluation using a new JEP instance. This method
	 * is called by all ThreadTestThreads at very much the same time.
	 */
	public void evaluate() {
		for (int i = 0; i < 10; i++) {
            org.nfunk.jep.JEP myParser = new org.nfunk.jep.JEP();
            String fooValue = null;
            Math.random();
            
            if (Math.random()> 0.5) {
                fooValue="NLS";
            } else {
                fooValue="NLT";
            }
            
            myParser.addVariableAsObject("foo",fooValue);
            myParser.parseExpression("foo==\""+fooValue + "\"");
            
            //
            if (myParser.getValue() < 1.0)
                System.out.println("ERROR");
        }
    }
}
