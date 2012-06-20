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
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.Size;
import org.amanzi.asn1.parser.lexer.ranges.impl.Range;
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

	/**
	 * States enumeration for {@link OctetStringLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		VALUE, COMMA
	}

	private boolean sizeToken = false;
	private IState currentState;

	public OctetStringLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.VALUE;
	}

	@Override
	protected OctetStringLexem parseToken(OctetStringLexem blankLexem,
			IToken token) throws SyntaxException {
		if (currentState == State.VALUE) {
			blankLexem.putMember(blankLexem.increaseBitNumber(),
					token.getTokenText());
		} else if (currentState != State.COMMA) {
			throw new SyntaxException(ErrorReason.TOKEN_NOT_SUPPORTED,
					"Token doesn't supported");
		}

		currentState = nextState(currentState);

		return blankLexem;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		return ControlSymbol.LEFT_BRACKET.getTokenText().equals(
				token.getTokenText())
				|| ControlSymbol.LEFT_BRACE.getTokenText().equals(
						token.getTokenText());
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		if (ReservedWord.SIZE.getTokenText().equals(token.getTokenText())) {
			sizeToken = true;
			return true;
		}

		return ControlSymbol.RIGHT_BRACE.getTokenText().equals(
				token.getTokenText())
				|| ControlSymbol.RIGHT_BRACKET.getTokenText().equals(
						token.getTokenText());
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.COMMA || sizeToken;
	}

	@Override
	protected OctetStringLexem finishUp(OctetStringLexem lexem, IToken token)
			throws SyntaxException {
		if (sizeToken) {
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
	protected IToken getStartToken() {
		return null;
	}

	@Override
	protected IToken getTrailingToken() {
		return null;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		HashSet<IToken> tokens = new HashSet<IToken>(
				Arrays.asList((IToken) ControlSymbol.COMMA));
		return tokens;
	}

	@Override
	protected String getLexemName() {
		return ClassDescriptionType.OCTET_STRING.getToken().getTokenText();
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case COMMA:
			return State.VALUE;
		case VALUE:
			return State.COMMA;
		default:
			return null;
		}
	}

	@Override
	protected IState getInitialState() {
		return State.VALUE;
	}

	@Override
	protected ILexem createInitialSubLexem(IToken identifier) {
		return new Size();
	}

}
