/* @author rich
 * Created on 15-Mar-2004
 *
 * This code is covered by a Creative Commons
 * Attribution, Non Commercial, Share Alike license
 * <a href="http://creativecommons.org/licenses/by-nc-sa/1.0">License</a>
 */
package org.lsmp.djep.groupJep.groups;

import org.lsmp.djep.groupJep.GroupI;
import org.lsmp.djep.groupJep.values.*;
import org.lsmp.djep.groupJep.interfaces.*;

/**
 * @author Rich Morris
 * Created on 15-Mar-2004
 */
public class PermutationGroup extends Group implements GroupI , HasListI {

	protected Permutation zeroPerm;
	
	public PermutationGroup(int n)
	{
		Integer perm[] = new Integer[n];
		for(int i=0;i<n;++i)
			perm[i]=new Integer(i+1);
		zeroPerm = new Permutation(this,perm);
	}
	/* (non-Javadoc)
	 * @see org.lsmp.djep.groupJep.GroupI#getZERO()
	 */
	public Number getZERO() {
		// TODO Auto-generated method stub
		return zeroPerm;
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.groupJep.GroupI#getInverse(java.lang.Number)
	 */
	public Number getInverse(Number a) {
		return ((Permutation) a).getInverse();
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.groupJep.GroupI#add(java.lang.Number, java.lang.Number)
	 */
	public Number add(Number a, Number b) {
		return ((Permutation) a).add((Permutation) b);
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.groupJep.GroupI#sub(java.lang.Number, java.lang.Number)
	 */
	public Number sub(Number a, Number b) {
		return ((Permutation) a).sub((Permutation) b);
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.groupJep.GroupI#equals(java.lang.Number, java.lang.Number)
	 */
	public boolean equals(Number a, Number b) {
		return ((Permutation) a).equals((Permutation) b);
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.groupJep.GroupI#valueOf(java.lang.String)
	 */
	public Number valueOf(String s) {
		return Integer.valueOf(s);
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.groupJep.interfaces.HasListI#list(java.lang.Number[])
	 */
	public Number valueOf(Number[] eles) {
		
		Integer perm[] = new Integer[eles.length];
		for(int i=0;i<eles.length;++i)
			perm[i]=new Integer(eles[i].intValue());
		Permutation res = new Permutation(this,perm);
		return res;
	}

	/* (non-Javadoc)
	 * @see org.lsmp.djep.groupJep.interfaces.HasListI#list(java.lang.Number[])
	 */
	public Number list(Number[] eles) {
		return this.valueOf(eles);
	}

}
