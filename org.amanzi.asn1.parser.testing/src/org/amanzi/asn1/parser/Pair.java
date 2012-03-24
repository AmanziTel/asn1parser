/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse Public License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.amanzi.asn1.parser;

/**
 * Class that represents a Pair of Values
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class Pair<LEFT, RIGHT> implements Comparable<Pair<LEFT, RIGHT>> {

    private LEFT left;

    private RIGHT right;
    
    /**
     * 
     */
    public Pair() {
        super();
    }

    /**
     * @param left
     * @param right
     */
    public Pair(LEFT left, RIGHT right) {
        super();
        this.left = left;
        this.right = right;
    }

    /**
     * @return Returns the left.
     */
    public LEFT getLeft() {
        return left;
    }

    /**
     * @return Returns the right.
     */
    public RIGHT getRight() {
        return right;
    }
    
    /**
     * @param left The left to set.
     */
    public void setLeft(LEFT left) {
        this.left = left;
    }

    /**
     * @param right The right to set.
     */
    public void setRight(RIGHT right) {
        this.right = right;
    }

    @Override
    public int compareTo(Pair<LEFT, RIGHT> o) {
        if (null != o) {
            if (o.equals(this)) {
                return 0;
            } else if (o.hashCode() > this.hashCode()) {
                return 1;
            } else if (o.hashCode() < this.hashCode()) {
                return -1;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("Left: ");
        buff.append(left);
        buff.append("\tRight: ");
        buff.append(right);
        return buff.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object p1) {
        if (p1 instanceof Pair) {
            Pair<LEFT, RIGHT> pair = (Pair<LEFT, RIGHT>)p1;
            if (null != p1) {
                if (pair.left.equals(this.left) && pair.right.equals(this.right)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = left.hashCode() + (31 * right.hashCode());
        return hashCode;
    }

}
