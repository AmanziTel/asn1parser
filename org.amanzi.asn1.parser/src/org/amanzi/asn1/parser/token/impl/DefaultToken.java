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
 * Contains number of default Tokens
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public enum DefaultToken implements IToken {
    ADD("+"),
    ASSIGNMENT("::="),
    COMMA(","),
    COMMENT("--"),
    RANGE(".."),
    LEFT_BRACE("{"),
    LEFT_BRACKET("("),
    MULTIPLY("*"),
    RIGHT_BRACE("}"),
    RIGHT_BRACKET(")");
    
    /*
     * Text of Token
     */
    private String tokenText;
    
    /**
     * Constructor for Enum
     * 
     * @param tokenText
     */
    private DefaultToken(String tokenText) {
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

}
