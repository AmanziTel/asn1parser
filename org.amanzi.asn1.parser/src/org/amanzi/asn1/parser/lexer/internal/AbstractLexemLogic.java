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
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.token.IToken;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
abstract class AbstractLexemLogic<T extends ILexem> implements ILexemLogic<T> {
    
    protected IStream<IToken> tokenStream;

    public AbstractLexemLogic(IStream<IToken> tokenStream) {
        this.tokenStream = tokenStream;
    }
    
    @Override
    public T parse(T blankLexem) throws SyntaxException{
        boolean parsed = false;
        
        while (tokenStream.hasNext()) {
            IToken token = tokenStream.next();
            
            if (token.equals(getTrailingToken())) {
                parsed = true;
                break;
            }
            
            
        }
        
        if (!parsed) { 
            throw new SyntaxException("Stream has no element to finish Enumeration");
        }
        
        return null;
    }
    
    
    
    protected abstract T parseToken(T blankLexem);
    
    protected abstract IToken getTrailingToken();
    
    protected abstract IToken[] getSupportedTokens(); 
    
}
