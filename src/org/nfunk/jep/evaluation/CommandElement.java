/*
 * 
 * Created on 15-Aug-2003
 */
package org.nfunk.jep.evaluation;

import org.nfunk.jep.function.*;
/**
 * 
 * @author nathan
 */
public class CommandElement {
	public final static int VAR   = 0; 
	public final static int CONST = 1; 
	public final static int FUNC  = 2; 
	private int                 type;
	private String              varName;
	private PostfixMathCommandI pfmc;
	private int                 nParam;
	private Object              value;

	/**
	 * @return
	 */
	public final PostfixMathCommandI getPFMC() {
		return pfmc;
	}

	/**
	 * @return
	 */
	public final int getType() {
		return type;
	}

	/**
	 * @return
	 */
	public final Object getValue() {
		return value;
	}

	/**
	 * @return
	 */
	public final String getVarName() {
		return varName;
	}

	/**
	 * @return
	 */
	public final int getNumParam() {
		return nParam;
	}

	/**
	 * @param commandI
	 */
	public final void setPFMC(PostfixMathCommandI commandI) {
		pfmc = commandI;
	}

	/**
	 * @param i
	 */
	public final void setType(int i) {
		type = i;
	}

	/**
	 * @param object
	 */
	public final void setValue(Object object) {
		value = object;
	}

	/**
	 * @param string
	 */
	public final void setVarName(String string) {
		varName = string;
	}

	/**
	 * @param i
	 */
	public final void setNumParam(int i) {
		nParam = i;
	}

}
