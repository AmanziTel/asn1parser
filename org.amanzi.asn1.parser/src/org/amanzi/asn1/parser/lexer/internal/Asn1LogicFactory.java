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

package org.amanzi.asn1.parser.lexer.internal;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * Factory that creates Parse-Logic classes
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class Asn1LogicFactory {
    
    /**
     * Creates a Token by KeyWord
     *
     * @param token
     * @return
     */
    private static ILexemLogic<?> createLogic(ReservedWord token, IStream<IToken> tokenStream) {
        if (token != null) {
            switch (token) {
            case ENUMERATED:
                return new EnumeratedLogic(tokenStream);
            }
        }
        
        return null;
    }
    
    /**
     * Creates a Token by Terminate symbol
     *
     * @param token
     * @return
     */
    private static ILexemLogic<?> createLogic(ControlSymbol symbol, IStream<IToken> tokenStream) {
        if (symbol != null) {
            switch (symbol) {
            case ASSIGNMENT:
                return new ClassDefinitionLogic(tokenStream);
            }
        }
        
        return null;
    }

    /**
     * Creates a Token based on it's Parsed representation
     *
     * @param token
     * @return
     */
    public static ILexemLogic<?> createLogic(IToken token, IStream<IToken> tokenStream) {
        if (token instanceof ReservedWord) {
            return createLogic((ReservedWord)token, tokenStream);
        } else if (token instanceof ControlSymbol) {
            return createLogic((ControlSymbol)token, tokenStream);
        }
        
        return null;
    }
    
}
