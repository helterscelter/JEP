/*****************************************************************************

@header@
@date@
@copyright@
@license@

*****************************************************************************/

package org.nfunk.jepexamples;


/**
 * The ThreadTestThread waits for 5 seconds before calling the evaluate method
 * of the ThreadTest instance.
 * <p>
 * Thanks to Matthew Baird and Daniel Teng for this code.
 */
public class ThreadTestThread extends Thread
{
    ThreadTest test;

    public ThreadTestThread(ThreadTest test_in)
    {
        test = test_in;
    }

    public void run() {

        try {
            Thread.sleep(5000);
            test.evaluate();
            Thread.yield();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
