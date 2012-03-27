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

package org.amanzi.asn1.parser.lexer;

import java.io.InputStream;

import org.amanzi.asn1.parser.AbstractStream;
import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.TokenAnalyzer;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;

/**
 * Lexical Analyzer
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class LexicalAnalyzer extends AbstractStream<ILexem> {
    
    private IStream<IToken> tokenStream;
    
    public LexicalAnalyzer(InputStream inputStream) {
        this.tokenStream = new TokenAnalyzer(inputStream);
    }
    
    public LexicalAnalyzer(IStream<IToken> tokenStream) {
        this.tokenStream = tokenStream;
    }

    @Override
    protected ILexem readNextElement() {
        parseToNextToken();
        
        return null;
    }
    
    /**
     * Parses to next non-Comment Token
     *
     * @return
     */
    private IToken parseToNextToken() {
        IToken result = null;
        while (tokenStream.hasNext() && (!ControlSymbol.COMMENT.equals(result))) {
            result = tokenStream.next();
        }
        
        return result;
    }

}
