package org.nfunk.jepexamples;

/**
 * This class tests the thread safety of the JEP package. 1000 threads
 * are started, and each one invokes the evaluate method. The evaluate method
 * creates 10 JEP instances.
 * <p>
 * Thanks to Matthew Baird and Daniel Teng for this code.
 */
public class ThreadTest {
	
	static long time = 0;
	
	/**
	 * Start 1000 threads.
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

			if (myParser.getValue() < 1.0)
				System.out.println("ERROR");
		}
	}
}
