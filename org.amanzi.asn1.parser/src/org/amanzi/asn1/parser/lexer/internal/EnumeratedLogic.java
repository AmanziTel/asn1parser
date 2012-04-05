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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * Logic of parsing for ENUMERATED element
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class EnumeratedLogic extends AbstractLexemLogic<Enumerated> {
    
    private final static HashSet<IToken> SUPPORTED_TOKENS = new HashSet<>(Arrays.asList((IToken)ControlSymbol.COMMA));
    
    private enum State {
        STARTED,
        VALUE,
        COMMA;
    }
    
    private State previousState;
    
    /**
     * @param tokenStream
     */
    public EnumeratedLogic(IStream<IToken> tokenStream) {
        super(tokenStream);
        previousState = State.STARTED;
    }

    @Override
    protected Enumerated parseToken(Enumerated blankLexem, IToken token) throws SyntaxException {
        //check state
        if (token.isDynamic()) {
            //can be at start of after comma
            if (previousState == State.STARTED || previousState == State.COMMA) {
                //add a member
                blankLexem.addMember(token.getTokenText());
            } else {
                throw new SyntaxException(ErrorReason.NO_SEPARATOR, "No comma separator between Enumeration values");
            }
        } else {
            //shoule be a value before
            if (previousState != State.VALUE) {
                throw new SyntaxException(ErrorReason.NO_SEPARATOR, "No comma separator between Enumeration values");
            }
        }
        
        previousState = nextState(previousState);
        
        return blankLexem;
    }

    @Override
    protected IToken getTrailingToken() {
        return ControlSymbol.RIGHT_BRACE;
    }

    @Override
    protected Set<IToken> getSupportedTokens() {
        return SUPPORTED_TOKENS;
    }

    @Override
    protected IToken getStartToken() {
        return ControlSymbol.LEFT_BRACE;
    }

    @Override
    protected String getLexemName() {
        return ReservedWord.ENUMERATED.getTokenText();
    }

    @Override
    protected boolean canFinish() {
        return previousState == State.VALUE;
    }

    private State nextState(State currentState) {
        switch (currentState) {
        case COMMA:
            return State.VALUE;
        case STARTED: 
            return State.VALUE;
        case VALUE:
            return State.COMMA;
        }
        return null;
    }
}
