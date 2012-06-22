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
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.ranges.impl.Range;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * 
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class IntegerLexemLogic extends AbstractLexemLogic<IntegerLexem> {

	private static final String CONST_MATCHER = "[a-zA-Z\\d0-9-]+";
	private static final String DEFAULT_LOWER_BOUND_VALUE = "1";

	private static final HashSet<IToken> SUPPORTED_TOKENS = new HashSet<IToken>(
			Arrays.asList((IToken) ControlSymbol.ASSIGNMENT,
					(IToken) ControlSymbol.RANGE,
					(IToken) ControlSymbol.LEFT_BRACKET,
					(IToken) ControlSymbol.RIGHT_BRACKET));

	/**
	 * States for {@link IntegerLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		STARTED, ASSIGMENT, LOWER_BOUND, UPPER_BOUND, RANGE, VALUE
	}

	private Range range;
	private IState currentState;

	public IntegerLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.STARTED;
		range = new Range();
	}

	@Override
	protected IntegerLexem parseToken(IntegerLexem blankLexem, IToken token)
			throws SyntaxException {
		if (currentState == State.STARTED || currentState == State.RANGE) {
			range.addBound(token.getTokenText());
		} else {
			if (currentState != State.LOWER_BOUND) {
				throw new SyntaxException(ErrorReason.NO_SEPARATOR,
						"No Range separator between values");
			}
		}

		currentState = nextState(currentState);

		return blankLexem;
	}

	@Override
	protected IntegerLexem finishUp(IntegerLexem lexem, IToken token)
			throws SyntaxException {
		if (currentState == State.UPPER_BOUND) {
			String lowerBound = range.getLowerBound();
			String upperBound = range.getUpperBound();
			if (lowerBound != null) {
				if (upperBound != null && !lowerBound.matches(CONST_MATCHER)) {
					lexem.setSize(upperBound.matches(CONST_MATCHER) ? Integer
							.parseInt(lowerBound) : range.computeRange());
				}
			}
			lexem.setRange(range);
		} else if (currentState == State.VALUE) {
			String lexemSize = token.getTokenText();
			range.setLowerBound(DEFAULT_LOWER_BOUND_VALUE);
			range.setUpperBound(lexemSize);
			lexem.setSize(Integer.valueOf(lexemSize));
			lexem.setRange(range);
		}

		return super.finishUp(lexem, token);
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		if (ControlSymbol.RIGHT_BRACKET.getTokenText().equals(
				token.getTokenText())) {
			currentState = State.UPPER_BOUND;
			return true;
		}
		return currentState == State.VALUE || currentState == State.UPPER_BOUND;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		return SUPPORTED_TOKENS;
	}

	@Override
	protected String getLexemName() {
		return ReservedWord.INTEGER.getTokenText();
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case STARTED:
			return State.LOWER_BOUND;
		case LOWER_BOUND:
			return State.RANGE;
		case RANGE:
			return State.UPPER_BOUND;
		case ASSIGMENT:
			return State.VALUE;
		}
		return null;
	}

	@Override
	protected IState getInitialState() {
		return State.STARTED;
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.VALUE || currentState == State.UPPER_BOUND;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		if (ControlSymbol.ASSIGNMENT.getTokenText()
				.equals(token.getTokenText())) {
			currentState = State.VALUE;
			return true;
		}
		return ControlSymbol.LEFT_BRACKET.getTokenText().equals(
				token.getTokenText());
	}

	@Override
	protected IToken getStartToken() {
		return null;
	}

	@Override
	protected IToken getTrailingToken() {
		return null;
	}

}
