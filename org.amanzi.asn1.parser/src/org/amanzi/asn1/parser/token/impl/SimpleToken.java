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

package org.amanzi.asn1.parser.token.impl;

import org.amanzi.asn1.parser.token.IToken;

/**
 * Simple Token entity that wrapps a Text
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class SimpleToken implements IToken {
    
    /*
     * Text of this Token
     */
    private String tokenText;
    
    /**
     * Constructor for Token
     * 
     * @param tokenText
     */
    public SimpleToken(String tokenText) {
        this.tokenText = tokenText;
    }

    @Override
    public String getTokenText() {
        return tokenText;
    }
    
    @Override
    public String toString() {
        return tokenText;
    }
    
    @Override
    public boolean equals(Object anotherToken) {
        if (anotherToken instanceof IToken) {
            IToken token = (IToken)anotherToken;
            
            return getTokenText().equals(token.getTokenText());
        }
        
        return false;
        
    }

}
