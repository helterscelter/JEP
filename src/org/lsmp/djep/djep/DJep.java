/* @author rich
 * Created on 16-Nov-2003
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.djep;
import org.lsmp.djep.xjep.*;
import org.nfunk.jep.*;
/**
 * Adds differentation to an extended JEP.
 * 
 * @author Rich Morris
 * Created on 16-Nov-2003
 */
public class DJep extends XJep {
	protected DifferentationVisitor dv = new DifferentationVisitor(this);

	public DJep()
	{
		this.pv = new DPrintVisitor();
		this.vf = new DVariableFactory();
		this.symTab = new DSymbolTable(vf);
	}
	public Node differentiate(Node node,String name) throws ParseException
	{
		return dv.differentiate(node,name,this);
	}
	protected DJep(DJep j)
	{
		super((XJep) j);
		this.dv=j.dv;
	}

	public XJep newInstance()
	{
		DJep newJep = new DJep(this);
		return newJep;
	}
	public XJep newInstance(SymbolTable st)
	{
		DJep newJep = new DJep(this);
		newJep.symTab = st;
		return newJep;
	}

	public DifferentationVisitor getDifferentationVisitor() { return dv; }
	public DSymbolTable getVarTab() { return (DSymbolTable) this.getSymbolTable(); } 
	public boolean addStandardDiffRules() { return dv.addStandardDiffRules(); }
	public DPrintVisitor getDPrintVisitor() { return (DPrintVisitor) pv; }
}
