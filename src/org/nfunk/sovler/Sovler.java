/*
 * @(#)Sovler.java 1.0 02/03/30
 *
 * You can modify the template of this file in the
 * directory ..\JCreator\Templates\Template_1\Project_Name.java
 *
 * You can also create your own project template by making a new
 * folder in the directory ..\JCreator\Template\. Use the other
 * templates as examples.
 *
 */
package org.nfunk.sovler;

import org.nfunk.jep.*;

class Sovler {
	
	public Sovler() {
	}

	public static void main(String args[]) {
		System.out.println("Starting Sovler...");
		
	}
	
	public boolean isLinear(Node topNode)
	{
		if (topNode != null)
			return true;
			
		return false;
	}
}
