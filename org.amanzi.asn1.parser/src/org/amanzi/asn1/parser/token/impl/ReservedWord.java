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
 * Enum with Reserved Words 
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public enum ReservedWord implements IToken {
    BEGIN("BEGIN"),
    BIT_STRING("BIT STRING"),
    BOOLEAN("BOOLEAN"),
    CHOISE("CHOISE"),
    CONTAINING("CONTAINING"),
    DEFINITIONS_AUTOMATIC_TAGS("DEFINITIONS AUTOMATIC TAGS"),
    END("END"),
    ENUMERATED("ENUMERATED"),
    FROM("FROM"),
    IMPORTS("IMPORTS"),
    INTEGER("INTEGER"),
    NULL("NULL"),
    OCTET_STRING("OCTET STRING"),
    OF("OF"),
    OPTIONAL("OPTIONAL"),
    SEQUENCE("SEQUENCE"),
    SIZE("SIZE");
    
    /*
     * Cache of Possible Tokens per character
     */
    private static HashMap<Character, ReservedWord[]> characterCache = new HashMap<Character, ReservedWord[]>();
    
    /**
     * Text of this Word
     */
    private String text;
    
    /**
     * Creates a Enum instance
     * 
     * @param text
     */
    private ReservedWord(String text) {
        this.text = text;
    }

    @Override
    public String getTokenText() {
        return text;
    }
    
    /**
     * Returns all Tokens that contains corresponding character
     *
     * @param character
     * @return
     */
    public static ReservedWord[] getPossibleTokens(char character) {
        ReservedWord[] result = characterCache.get(character);
        
        if (result == null) {
            result = new ReservedWord[0];
            
            for (ReservedWord singleToken : values()) {
                if (ArrayUtils.contains(singleToken.getTokenText().toCharArray(), character)) {
                    result = ArrayUtils.add(result, singleToken);
                }
            }
            
            characterCache.put(character, result);
        }
                
        return result;
    }

    /**
     * Searches for a corresponding token by it's text
     *
     * @param text
     * @return
     */
    public static ReservedWord findByText(String text) {
        for (ReservedWord token : values()) {
            if (token.getTokenText().equals(text)) {
                return token;
            }
        }
        
        return null;
    }

    @Override
    public boolean isDynamic() {
        return false;
    }
}
