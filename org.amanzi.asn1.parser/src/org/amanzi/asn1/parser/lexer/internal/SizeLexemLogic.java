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

import java.util.Set;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.Size;
import org.amanzi.asn1.parser.token.IToken;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class SizeLexemLogic extends AbstractLexemLogic<Size> {

    /**
     * @param tokenStream
     */
    public SizeLexemLogic(IStream<IToken> tokenStream) {
        super(tokenStream);
    }

    @Override
    protected boolean canFinish() {
        return false;
    }

    @Override
    protected IToken getStartToken() {
        return null;
    }

    @Override
    protected Size parseToken(Size blankLexem, IToken token) throws SyntaxException {
        return null;
    }

    @Override
    protected IToken getTrailingToken() {
        return null;
    }

    @Override
    protected Set<IToken> getSupportedTokens() {
        return null;
    }

    @Override
    protected String getLexemName() {
        return null;
    }

}
