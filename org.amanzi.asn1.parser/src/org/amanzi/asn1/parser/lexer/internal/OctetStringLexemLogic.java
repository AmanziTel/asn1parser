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
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.Size;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * Logic for {@link OctetStringLexem}
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class OctetStringLexemLogic extends
		AbstractFabricLogic<OctetStringLexem, ILexem> {

	private static final Set<IToken> SUPPORTED_TOKENS = new HashSet<IToken>(
			Arrays.asList((IToken) ControlSymbol.COMMA,
					(IToken) ControlSymbol.LEFT_BRACKET,
					(IToken) ControlSymbol.LEFT_BRACE,
					(IToken) ControlSymbol.RIGHT_BRACE,
					(IToken) ControlSymbol.RIGHT_BRACKET,
					(IToken) ReservedWord.SIZE));

	/**
	 * States enumeration for {@link OctetStringLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		STARTED, VALUE, SIZE, COMMA, RIGHT_BRACE, WITHOUT_PARAMETERS
	}

	private boolean skipFirstToken = true;
	private IState currentState;

	public OctetStringLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.STARTED;
	}

	@Override
	protected OctetStringLexem parseToken(OctetStringLexem blankLexem,
			IToken token) throws SyntaxException {
		if (currentState == State.VALUE) {
			String name = token.getTokenText();
			Byte bitIndex = 0;
			while (true) {
				if (tokenStream.hasNext()) {
					IToken bitIndexToken = tokenStream.next();
					if (ControlSymbol.LEFT_BRACKET.getTokenText().equals(
							bitIndexToken.getTokenText())) {
						continue;
					} else if (ControlSymbol.RIGHT_BRACKET.getTokenText()
							.equals(bitIndexToken.getTokenText())) {
						break;
					}
					bitIndex = Byte.parseByte(bitIndexToken.getTokenText());
				} else {
					break;
				}
			}
			blankLexem.putMember(bitIndex, name);
		}
		currentState = nextState(currentState);

		return blankLexem;
	}

	@Override
	protected boolean skipFirstToken() {
		return skipFirstToken;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		if (ControlSymbol.RIGHT_BRACE.getTokenText().equals(
				token.getTokenText())
				|| ControlSymbol.COMMA.getTokenText().equals(
						token.getTokenText())) {
			skipFirstToken = false;
			currentState = State.WITHOUT_PARAMETERS;
			return true;
		}
		if (ControlSymbol.LEFT_BRACE.getTokenText()
				.equals(token.getTokenText())) {
			currentState = State.VALUE;
			return true;
		} else if (ControlSymbol.LEFT_BRACKET.getTokenText().equals(
				token.getTokenText())) {
			currentState = State.SIZE;
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		if (currentState == State.WITHOUT_PARAMETERS) {
			return true;
		}

		if (ControlSymbol.RIGHT_BRACE.getTokenText().equals(
				token.getTokenText())
				&& tokenStream.hasNext()) {
			// in case if after bitstring member went size (example: .., 8(7)}
			// (SIZE(8)))
			tokenStream.next();
			currentState = State.RIGHT_BRACE;
		}
		if (ControlSymbol.RIGHT_BRACKET.getTokenText().equals(
				token.getTokenText())) {
			currentState = State.SIZE;
		}
		return currentState == State.SIZE
				|| currentState == State.WITHOUT_PARAMETERS;
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.COMMA || currentState == State.SIZE
				|| currentState == State.WITHOUT_PARAMETERS;
	}

	@Override
	protected OctetStringLexem finishUp(OctetStringLexem lexem, IToken token)
			throws SyntaxException {
		if (currentState == State.SIZE) {
			lexem.setSize((Size) parseSubLogic(token));
		}
		return super.finishUp(lexem, token);
	}

	@Override
	protected IToken getStartToken() {
		return null;
	}

	@Override
	protected IToken getTrailingToken() {
		return null;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		return SUPPORTED_TOKENS;
	}

	@Override
	protected String getLexemName() {
		return ClassDescriptionType.OCTET_STRING.getToken().getTokenText();
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case VALUE:
			return State.COMMA;
		case COMMA:
			return State.VALUE;
		case RIGHT_BRACE:
			return State.SIZE;
		default:
			return null;
		}
	}

	@Override
	protected IState getInitialState() {
		return State.STARTED;
	}

	@Override
	protected ILexem createInitialSubLexem(IToken identifier) {
		return new Size();
	}

}
