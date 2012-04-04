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
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;

/**
 * Logic of parsing for ENUMERATED element
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class EnumeratedLogic extends AbstractLexemLogic<Enumerated> {

    /**
     * @param tokenStream
     */
    public EnumeratedLogic(IStream<IToken> tokenStream) {
        super(tokenStream);
    }

    @Override
    protected Enumerated parseToken(Enumerated blankLexem) {
        return null;
    }

    @Override
    protected IToken getTrailingToken() {
        return ControlSymbol.RIGHT_BRACE;
    }

    @Override
    protected IToken[] getSupportedTokens() {
        return null;
    }

}
