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
import org.amanzi.asn1.parser.lexer.impl.Size;
import org.amanzi.asn1.parser.lexer.ranges.impl.Range;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class SizeLexemLogic extends AbstractLexemLogic<Size> {
    
    private final static HashSet<IToken> SUPPORTED_TOKENS = new HashSet<>(Arrays.asList((IToken)ControlSymbol.LEFT_BRACKET,
                                                                                        (IToken)ControlSymbol.RIGHT_BRACKET,
                                                                                        (IToken)ControlSymbol.RANGE)); 
    
    private enum State {
        STARTED,
        RANGE,
        LOWER_BOUND,
        UPPER_BOUND
    }
    
    private State previousState;
    
    private Range range;

    /**
     * @param tokenStream
     */
    public SizeLexemLogic(IStream<IToken> tokenStream) {
        super(tokenStream);
        previousState = State.STARTED;
        
        range = new Range();
    }

    @Override
    protected boolean canFinish() {
        return previousState == State.UPPER_BOUND || previousState == State.LOWER_BOUND;
    }

    @Override
    protected IToken getStartToken() {
        return ControlSymbol.LEFT_BRACKET;
    }

    @Override
    protected Size parseToken(Size blankLexem, IToken token) throws SyntaxException {
        if (token.isDynamic()) {
            //can be after Started or only after Range state
            if (previousState == State.STARTED || previousState == State.RANGE) {
                range.addBound(token.getTokenText());
            } else {
                throw new SyntaxException(ErrorReason.NO_SEPARATOR, "No Range separator between values");
            }
        } else {
            if (previousState != State.LOWER_BOUND && previousState != State.UPPER_BOUND) {
                throw new SyntaxException(ErrorReason.NO_SEPARATOR, "No '..' separator between Range values");
            }
        }
        
        previousState = nextState(previousState);
        
        return blankLexem;
    }

    @Override
    protected IToken getTrailingToken() {
        return ControlSymbol.RIGHT_BRACKET;
    }

    @Override
    protected Set<IToken> getSupportedTokens() {
        return SUPPORTED_TOKENS;
    }

    @Override
    protected String getLexemName() {
        return ReservedWord.SIZE.getTokenText();
    }

    private State nextState(State currentState) {
        switch (currentState) {
        case RANGE:
            return State.UPPER_BOUND;
        case STARTED: 
            return State.LOWER_BOUND;
        case LOWER_BOUND:
            return State.RANGE;
        }
        return null;
    }

    @Override
    protected Size finishUp(Size lexem) throws SyntaxException {
        try {
            lexem.setSize(range.computeRange());
        } catch (SyntaxException e) {
            if (previousState == State.LOWER_BOUND) {
                if (range.getLowerBoundValue() == null) {
                    throw new SyntaxException(ErrorReason.TOKEN_NOT_SUPPORTED, "Size cannot contain only Constant <" + range.getLowerBound() + ">");
                } else {
                    lexem.setSize(range.getLowerBoundValue());
                }
            } else {
                lexem.setRange(range);
            }
        }
        
        return lexem;
    }
}
