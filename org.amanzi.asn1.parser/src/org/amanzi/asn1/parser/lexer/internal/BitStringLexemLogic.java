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
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.lexer.impl.Size;
import org.amanzi.asn1.parser.lexer.ranges.impl.Range;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * Bit String lexem logic
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class BitStringLexemLogic extends
		AbstractFabricLogic<BitStringLexem, ILexem> {

	private boolean sizeType = false;

	private static final HashSet<IToken> SUPPORTED_TOKENS = new HashSet<IToken>(
			Arrays.asList((IToken) ControlSymbol.COMMA,
					(IToken) ControlSymbol.LEFT_BRACKET,
					(IToken) ControlSymbol.LEFT_BRACE,
					(IToken) ControlSymbol.RIGHT_BRACKET));

	/**
	 * States enumeration for {@link BitStringLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		STARTED, VALUE, COMMA
	}

	public BitStringLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.STARTED;
	}

	@Override
	protected BitStringLexem parseToken(BitStringLexem blankLexem, IToken token)
			throws SyntaxException {
		if (currentState == State.STARTED || currentState == State.VALUE) {
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
		} else {
			if (currentState != State.COMMA) {
				throw new SyntaxException(ErrorReason.NO_SEPARATOR,
						"No separator between Enumeration values");
			}
		}
		currentState = nextState(currentState);

		return blankLexem;
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.COMMA || sizeType;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		return token.isDynamic()
				|| ControlSymbol.LEFT_BRACKET.getTokenText().equals(
						token.getTokenText())
				|| ReservedWord.SIZE.getTokenText()
						.equals(token.getTokenText())
				|| ControlSymbol.LEFT_BRACE.getTokenText().equals(
						token.getTokenText());

	}

	@Override
	protected BitStringLexem finishUp(BitStringLexem lexem, IToken token)
			throws SyntaxException {
		if (sizeType) {
			lexem.setSize((Size) parseSubLogic(token));
		} else {
			Size size = new Size();
			int membersCount = lexem.getMembers().size();
			size.setSize(membersCount);
			size.setRange(new Range(String.valueOf(membersCount)));
			lexem.setSize(size);
		}

		return super.finishUp(lexem, token);
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		return SUPPORTED_TOKENS;
	}

	@Override
	protected String getLexemName() {
		return ReservedWord.BIT_STRING.getTokenText();
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		String tokenText = token.getTokenText();
		if (ControlSymbol.LEFT_BRACKET.getTokenText().equals(tokenText)
				|| ControlSymbol.LEFT_BRACE.getTokenText().equals(tokenText)) {
			currentState = State.COMMA;
		}
		if (ControlSymbol.RIGHT_BRACE.getTokenText().equals(tokenText)) {
			currentState = State.COMMA;
		}
		if (ReservedWord.SIZE.name().equals(token.getTokenText())) {
			sizeType = true;
			return true;
		}

		return token.equals(ControlSymbol.RIGHT_BRACE);
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case STARTED:
			return State.VALUE;
		case COMMA:
			return State.VALUE;
		case VALUE:
			return State.COMMA;
		}
		return null;
	}

	@Override
	protected IState getInitialState() {
		return State.STARTED;
	}

	@Override
	protected ILexem createInitialSubLexem(IToken identifier) {
		return new Size();
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
