/* @author rich
 * Created on 04-Jul-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;

import org.lsmp.djep.xjep.MacroFunction;
import org.nfunk.jep.*;

/**
   * If your really lazy, you don't even neeed to workout the derivatives
   * of a function defined by a macro yourself.
   * This class will automatically calculate the rules for you.
   */
  public class MacroFunctionDiffRules extends ChainRuleDiffRules
  {
	/**
	 * Calculates the rules for the given function.
	 */
	  public MacroFunctionDiffRules(DJepI djep,MacroFunction fun)  throws ParseException
	  {
		  //super(dv);
		  name = fun.getName();
		  pfmc = fun;
		  
		SymbolTable localSymTab = djep.getSymbolTable().newInstance();
		localSymTab.copyConstants(djep.getSymbolTable());
		DJepI localJep = (DJepI) djep.newInstance(localSymTab);

		  int nargs = fun.getNumberOfParameters();
		  rules = new Node[nargs];
		  if(nargs == 1)
			  rules[0] = localJep.differentiate((Node) fun.getTopNode(),"x");
		  else if(nargs == 2)
		  {
			  rules[0] = localJep.differentiate(fun.getTopNode(),"x");
			  rules[1] = localJep.differentiate(fun.getTopNode(),"y");
		  }
		  else
		  {
			  for(int i=0;i<nargs;++i)
				  rules[i] = localJep.differentiate(fun.getTopNode(),"x"+ String.valueOf(i));
		  }
		  for(int i=0;i<nargs;++i)
				rules[i] = localJep.simplify(rules[i]);
		  //fixVarNames();
	  }
  }
