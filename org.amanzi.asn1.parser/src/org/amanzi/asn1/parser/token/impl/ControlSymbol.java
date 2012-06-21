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

import java.util.HashMap;

import org.amanzi.asn1.parser.token.IToken;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Contains number of default Tokens
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public enum ControlSymbol implements IToken {
    ADD("+"),
    ASSIGNMENT("::="),
    COMMA(","),
    COMMENT("--"),
    RANGE(".."),
    LEFT_BRACE("{"),
    LEFT_BRACKET("("),
    MULTIPLY("*"),
    RIGHT_BRACE("}"),
    RIGHT_BRACKET(")"),
    SEMIKOLON(";");    
    
    /*
     * Cache of Possible Tokens per character
     */
    private static HashMap<Character, ControlSymbol[]> characterCache = new HashMap<Character, ControlSymbol[]>();
    
    /*
     * Text of Token
     */
    private String tokenText;
    
    /*
     * Length of Text for this Token
     */
    private int tokenLength;
    
    /**
     * Constructor for Enum
     * 
     * @param tokenText
     */
    private ControlSymbol(String tokenText) {
        this.tokenText = tokenText;
        this.tokenLength = tokenText.length();
    }

    @Override
    public String getTokenText() {
        return tokenText;
    }
    
    @Override
    public String toString() {
        return tokenText;
    }
    
    /**
     * Check if this text contains trailing token
     *
     * @param text
     * @return
     */
    public boolean checkText(String text) {
        return text.endsWith(tokenText);
    }
    
    /**
     * Cut a Trailing Token from the Text
     *
     * @param text
     * @return
     */
    public String cut(String text) {
        return text.substring(0, text.length() - tokenLength);
    }
    
    /**
     * Searches for a corresponding token by it's text
     *
     * @param text
     * @return
     */
    public static ControlSymbol findByText(String text) {
        for (ControlSymbol token : values()) {
            if (token.getTokenText().equals(text)) {
                return token;
            }
        }
        
        return null;
    }
    
    /**
     * Returns all Tokens that contains corresponding character
     *
     * @param character
     * @return
     */
    public static ControlSymbol[] getPossibleTokens(char character) {
        ControlSymbol[] result = characterCache.get(character);
        
        if (result == null) {
            result = new ControlSymbol[0];
            
            for (ControlSymbol singleToken : values()) {
                if (ArrayUtils.contains(singleToken.getTokenText().toCharArray(), character)) {
                    result = ArrayUtils.add(result, singleToken);
                }
            }
            
            characterCache.put(character, result);
        }
                
        return result;
    }

    @Override
    public boolean isDynamic() {
        return false;
    }

}
