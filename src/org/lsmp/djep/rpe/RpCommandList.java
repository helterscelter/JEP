/* @author rich
 * Created on 04-May-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.rpe;

/** A list of commands */
public final class RpCommandList {
	
	/** Data type for the command string */
	static final class RpCommand {
		short command;
		short aux1; 
		private RpCommand(short command){
			this.command = command; this.aux1 = -1;
		}
		private RpCommand(short command,short aux){
			this.command = command; this.aux1 = aux;
		}
		public String toString() {
			switch(command)
			{
				case RpEval.CONST: return "Constant\tno "+aux1;
				case RpEval.VAR: return "Variable\tnum "+aux1;
				case RpEval.ADD: return "ADD";
				case RpEval.SUB: return "SUB";
				case RpEval.MUL: return "MUL";
				case RpEval.DIV: return "DIV";
				case RpEval.MOD: return "MOD";
				case RpEval.POW: return "POW";
				case RpEval.AND: return "AND";
				case RpEval.OR: return "OR";
				case RpEval.NOT: return "NOT";
				case RpEval.LT: return "LT";
				case RpEval.LE: return "LE";
				case RpEval.GT: return "GT";
				case RpEval.GE: return "GE";
				case RpEval.EQ: return "EQ";
				case RpEval.NE: return "NE";
				case RpEval.ASSIGN: return "Assign\tnum "+aux1;
				case RpEval.FUN: return "Function\tnum "+aux1;
			}
			return "WARNING unknown command: "+command+" "+aux1;
		}
	}

	/** Incremental size for list of commands **/
	private static final int STACK_INC=10;
	/** List of commands **/
	RpCommand commands[] = new RpCommand[STACK_INC];
	/** Current position in the command Stack. **/
	private short commandPos;
	/** The return type at end of evaluation */
	private int finalType;
	
	/** Package private constructor */
	RpCommandList() {}
	/** Adds a command to the list */
	final void addCommand(short command,short aux)
	{
		if(commandPos == commands.length)
		{
			RpCommand newCommands[] = new RpCommand[commands.length+STACK_INC];
			System.arraycopy(commands,0,newCommands,0,commands.length);
			commands = newCommands;
		}
		commands[commandPos]=new RpCommand(command,aux);
		++commandPos;
//		++maxCommands;
	}
	final void addCommand(short command)
	{
		if(commandPos == commands.length)
		{
			RpCommand newCommands[] = new RpCommand[commands.length+STACK_INC];
			System.arraycopy(commands,0,newCommands,0,commands.length);
			commands = newCommands;
		}
		commands[commandPos]=new RpCommand(command);
		++commandPos;
//		++maxCommands;
	}

	public int getNumCommands() { return commandPos;}
	public int getFinalType() {	return finalType;	}
	public void setFinalType(int i) { finalType = i;}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<commandPos;++i) {
			sb.append(commands[i].toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}