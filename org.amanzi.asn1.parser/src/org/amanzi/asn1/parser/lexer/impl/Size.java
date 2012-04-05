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

package org.amanzi.asn1.parser.lexer.impl;

import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.ranges.impl.Range;

/**
 * Size Lexem
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class Size implements ILexem {
    
    private int size;
    
    private Range range;
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getSize() throws SyntaxException {
        computeRange();
        return size;
    }
    
    public void setRange(Range range) {
        this.range = range;
    }
    
    public Range getRange() {
        return range;
    }
    
    private void computeRange() throws SyntaxException {
        if (range != null) {
            size = range.computeRange();
        }
    }

}
