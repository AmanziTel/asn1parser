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
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class SizeLexemLogic extends AbstractLexemLogic<Size> {

	private static final Set<IToken> SUPPORTED_TOKENS = new HashSet<IToken>(
			Arrays.asList((IToken) ControlSymbol.LEFT_BRACKET,
					(IToken) ControlSymbol.RIGHT_BRACKET,
					(IToken) ControlSymbol.RANGE));

	private enum State implements IState {
		STARTED, RANGE, LOWER_BOUND, UPPER_BOUND
	}

	private Range range;

	/**
	 * @param tokenStream
	 */
	public SizeLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);

		range = new Range();
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.UPPER_BOUND
				|| currentState == State.LOWER_BOUND;
	}

	@Override
	protected IToken getStartToken() {
		return ControlSymbol.LEFT_BRACKET;
	}

	@Override
	protected Size parseToken(Size blankLexem, IToken token)
			throws SyntaxException {
		if (token.isDynamic()) {
			// can be after Started or only after Range state
			if (currentState == State.STARTED || currentState == State.RANGE) {
				range.addBound(token.getTokenText());
			} else {
				throw new SyntaxException(ErrorReason.NO_SEPARATOR,
						"No Range separator between values");
			}
		} else {
			if (currentState != State.LOWER_BOUND
					&& currentState != State.UPPER_BOUND) {
				throw new SyntaxException(ErrorReason.NO_SEPARATOR,
						"No '..' separator between Range values");
			}
		}

		currentState = nextState(currentState);

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

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
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
	protected Size finishUp(Size lexem, IToken token) throws SyntaxException {
		try {
			lexem.setSize(range.computeRange());
			lexem.setRange(range);
		} catch (SyntaxException e) {
			if (currentState == State.LOWER_BOUND) {
				Integer lowerBoundValue = range.getLowerBoundValue();
				if (lowerBoundValue != null && lowerBoundValue > 1) {
					// in case if we have ("value"..null),
					// set range lower bound default value 1,
					// upper bound: "value"
					// size as "value" (lowerBoundValue)
					range.setUpperBound(String.valueOf(lowerBoundValue));
					range.setLowerBound("1");
					lexem.setRange(range);
					lexem.setSize(lowerBoundValue);
				}
			} else {
				lexem.setRange(range);
			}
		}

		return lexem;
	}

	@Override
	protected IState getInitialState() {
		return State.STARTED;
	}
}
