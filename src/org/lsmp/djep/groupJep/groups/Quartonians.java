/* @author rich
 * Created on 16-May-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.groups;

import org.nfunk.jep.JEP;
import org.lsmp.djep.groupJep.interfaces.*;
/**
 * Possibly the quatonians, compleatly untested.
 * 
 * @author Rich Morris
 * Created on 16-May-2004
 */
public class Quartonians extends Group implements RingI {

	public static class Quartonian extends Number {
		double x,y,z,w;
		public Quartonian(double x,double y,double z,double w){
			this.w = x; this.y = y; this.z=z; this.w=w;
		}
		public double doubleValue() {return x;}
		public float floatValue() {return (float) x;}
		public int intValue() {return (int) x;}
		public long longValue() {return (long) x;}
		// TODO pretty print so 0 + 0 i + 0 j + 1 k printed as k
		public String toString() {return ""+x+"+"+y+" i +"+z+" j +"+w+" k";
	//		StringBuffer sb = new StringBuffer();
	//		if(x!=0.0) sb.append(x);
			
		}
	}
	private Quartonian ZERO = new Quartonian(0,0,0,0);
	private Quartonian ONE = new Quartonian(1,0,0,0);
	private Quartonian I = new Quartonian(0,1,0,0);
	private Quartonian J = new Quartonian(0,0,1,0);
	private Quartonian K = new Quartonian(0,0,0,1);

	public Number getZERO() {return ZERO;}
	public Number getONE() {return ONE;	}

	public Number getInverse(Number num) {
		Quartonian q = (Quartonian) num;
		return new Quartonian(-q.x,-q.y,-q.z,-q.w);
	}

	public Number add(Number a, Number b) {
		Quartonian p = (Quartonian) a;
		Quartonian q = (Quartonian) b;
		return new Quartonian(p.x+q.x,p.y+q.y,p.z+q.z,p.w+q.w);
	}

	public Number sub(Number a, Number b) {
		Quartonian p = (Quartonian) a;
		Quartonian q = (Quartonian) b;
		return new Quartonian(p.x-q.x,p.y-q.y,p.z-q.z,p.w-q.w);
	}


	public Number mul(Number a, Number b) {
		Quartonian p = (Quartonian) a;
		Quartonian q = (Quartonian) b;
		return new Quartonian(
			p.x*q.x - p.y*q.y - p.z*q.z - p.w*q.w,
			p.x*q.y - p.y*q.x + p.z*q.w - p.w*q.z,
			p.x*q.z - p.y*q.w + p.z*q.x + p.w*q.y,
			p.x*q.w - p.y*q.z - p.z*q.y + p.w*q.x
			);
	}

	public boolean equals(Number a, Number b) {
		Quartonian p = (Quartonian) a;
		Quartonian q = (Quartonian) b;
		return (p.x==q.x)&&(p.y==q.y)&&(p.z==q.z)&&(p.w==q.w);
	}

	public Number valueOf(String s) {
		return new Quartonian(Double.parseDouble(s),0,0,0);
	}

	public void addStandardConstants(JEP j) {
		super.addStandardConstants(j);
		j.addConstant("i",I);
		j.addConstant("j",J);
		j.addConstant("k",K);
	}
	public String toString() {return "quaternions";}

}
