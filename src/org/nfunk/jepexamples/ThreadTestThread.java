/*
 * Created by IntelliJ IDEA.
 * User: matthew.baird
 * Date: Jun 22, 2002
 * Time: 12:50:07 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package org.nfunk.jepexamples;


public class ThreadTestThread extends Thread
{
    ThreadTest test;

    public ThreadTestThread(ThreadTest test_in)
    {
        test = test_in;
    }

    public void run() {
        try {
            Thread.sleep(1);
            test.evaluate();
            Thread.yield();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
