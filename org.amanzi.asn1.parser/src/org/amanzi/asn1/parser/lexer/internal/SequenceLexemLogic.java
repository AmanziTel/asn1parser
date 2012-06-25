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
import org.amanzi.asn1.parser.lexer.impl.AbstractSequenceLexem;
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.ClassReference;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceOfLexem;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * SEQUENCE lexem logic
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class SequenceLexemLogic extends
		AbstractFabricLogic<AbstractSequenceLexem, ILexem> {

	private static Set<IToken> supportedTokens;

	/**
	 * Supported tokens set initialization
	 */
	static {
		supportedTokens = new HashSet<IToken>(Arrays.asList(
				(IToken) ControlSymbol.COMMA,
				(IToken) ControlSymbol.LEFT_BRACE,
				(IToken) ControlSymbol.LEFT_BRACKET,
				(IToken) ControlSymbol.RIGHT_BRACKET,
				(IToken) ReservedWord.SIZE, (IToken) ReservedWord.OPTIONAL,
				(IToken) ReservedWord.BOOLEAN, (IToken) ReservedWord.NULL));
		for (ClassDescriptionType type : ClassDescriptionType.values()) {
			supportedTokens.add(type.getToken());
		}
	}

	/**
	 * States for {@link SequenceLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		DESCRIPTION, COMMA, REFERENCE, OPTIONAL, SEQUENCE_OF
	}

	private ClassDescriptionType currentType;
	private IState currentState;
	private boolean skipFirstToken = false;

	public SequenceLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
	}

	@Override
	protected AbstractSequenceLexem parseToken(
			AbstractSequenceLexem blankLexem, IToken token)
			throws SyntaxException {
		defineCurrentDescriptionType(token);
		if (currentState == State.DESCRIPTION) {
			ClassReference reference = new ClassReference();
			String previousTokenName = getPreviousToken() != null ? getPreviousToken()
					.getTokenText() : null;
			reference.setName(previousTokenName);
			if (currentType == null) {
				descriptionManager
						.putReference(token.getTokenText(), reference);
			} else {
				reference
						.setClassDescription((IClassDescription) parseSubLogic(token));
				descriptionManager.setPreviousReference(reference);
			}
			((SequenceLexem) blankLexem).addMember(reference);
		} else if (currentState == State.OPTIONAL) {
			ClassReference reference = descriptionManager
					.getPreviousReference();
			reference.setOptional(true);
			descriptionManager.updateReference(getPreviousToken()
					.getTokenText(), reference);
		} else {
			if ((currentState != State.COMMA)
					&& (currentState != State.REFERENCE)
					&& (currentState != State.OPTIONAL)
					&& (currentState != State.SEQUENCE_OF)) {
				throw new SyntaxException(ErrorReason.TOKEN_NOT_SUPPORTED,
						"Token doesn't supported");
			}
		}
		currentState = nextState(currentState);
		setPreviousToken(token);

		return blankLexem;
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.COMMA || currentState == State.SEQUENCE_OF;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		String tokenText = token.getTokenText();
		if (ControlSymbol.LEFT_BRACKET.getTokenText().equals(tokenText)) {
			currentState = State.SEQUENCE_OF;
			skipFirstToken = false;
			return true;
		} else if (ControlSymbol.LEFT_BRACE.getTokenText().equals(tokenText)) {
			currentState = State.REFERENCE;
			skipFirstToken = true;
			return true;
		}
		return false;
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		if (ReservedWord.OPTIONAL.getTokenText().equals(token.getTokenText())) {
			currentState = State.OPTIONAL;
		} else if (ControlSymbol.COMMA.getTokenText().equals(
				token.getTokenText())
				|| ControlSymbol.RIGHT_BRACE.getTokenText().equals(
						token.getTokenText())) {
			currentState = State.COMMA;
		}
		return ControlSymbol.RIGHT_BRACE.getTokenText().equals(
				token.getTokenText())
				|| currentState == State.SEQUENCE_OF;
	}

	@Override
	protected AbstractSequenceLexem finishUp(AbstractSequenceLexem lexem,
			IToken token) throws SyntaxException {
		if (currentState == State.SEQUENCE_OF) {
			defineCurrentDescriptionType(ReservedWord.SEQUENCE_OF);
			lexem = (AbstractSequenceLexem) parseSubLogic(ReservedWord.SEQUENCE_OF);
		}
		return super.finishUp(lexem, token);
	}

	@Override
	protected boolean skipFirstToken() {
		return skipFirstToken;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		return supportedTokens;
	}

	@Override
	protected String getLexemName() {
		return ReservedWord.SEQUENCE.getTokenText();
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case DESCRIPTION:
			return State.COMMA;
		case COMMA:
			return State.REFERENCE;
		case REFERENCE:
			return State.DESCRIPTION;
		case OPTIONAL:
			return State.COMMA;
		}
		return null;
	}

	@Override
	protected IState getInitialState() {
		return null;
	}

	@Override
	protected ILexem createInitialSubLexem(IToken identifier) {
		if (currentType != null) {
			switch (currentType) {
			case BIT_STRING:
				return new BitStringLexem();
			case CHOICE:
				return new ChoiceLexem();
			case ENUMERATED:
				return new Enumerated();
			case INTEGER:
				return new IntegerLexem();
			case OCTET_STRING:
				return new OctetStringLexem();
			case SEQUENCE:
				return new SequenceLexem();
			case SEQUENCE_OF:
				return new SequenceOfLexem();
			}
		}
		return null;
	}

	/**
	 * Define current description type by token instance
	 * 
	 * @param token
	 */
	private void defineCurrentDescriptionType(IToken token) {
		if (token instanceof ReservedWord) {
			switch ((ReservedWord) token) {
			case BIT_STRING:
				currentType = ClassDescriptionType.BIT_STRING;
				break;
			case CHOICE:
				currentType = ClassDescriptionType.CHOICE;
				break;
			case ENUMERATED:
				currentType = ClassDescriptionType.ENUMERATED;
				break;
			case INTEGER:
				currentType = ClassDescriptionType.INTEGER;
				break;
			case OCTET_STRING:
				currentType = ClassDescriptionType.OCTET_STRING;
				break;
			case SEQUENCE:
				currentType = ClassDescriptionType.SEQUENCE;
				break;
			case SEQUENCE_OF:
				currentType = ClassDescriptionType.SEQUENCE_OF;
				break;
			default:
				currentType = null;
				break;
			}
		} else {
			currentType = null;
		}
	}

	@Override
	protected IToken getTrailingToken() {
		return null;
	}

	@Override
	protected IToken getStartToken() {
		return null;
	}
}