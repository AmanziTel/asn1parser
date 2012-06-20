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
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.ClassReference;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceLexem;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * Logic for {@link ChoiceLexem}
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class ChoiceLexemLogic extends AbstractFabricLogic<ChoiceLexem, ILexem> {

	/**
	 * States for {@link ChoiceLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		COMMA, CLASS_DEFINITION_NAME, CLASS_DESCRIPTION
	}

	private IState currentState;
	private ClassDescriptionType currentType;

	public ChoiceLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.CLASS_DEFINITION_NAME;
	}

	@Override
	protected ChoiceLexem parseToken(ChoiceLexem blankLexem, IToken token)
			throws SyntaxException {
		if (currentState == State.CLASS_DESCRIPTION) {
			defineCurrentDescriptionType(token);
			ClassReference reference = new ClassReference();
			reference.setName(getPreviousToken().getTokenText());
			if (currentType == null) {
				descriptionManager
						.putReference(token.getTokenText(), reference);
			} else {
				reference
						.setClassDescription((IClassDescription) parseSubLogic(token));
			}
			blankLexem.putMember(reference);
		} else if ((currentState != State.CLASS_DEFINITION_NAME)
				&& (currentState != State.CLASS_DESCRIPTION)
				&& (currentState != State.COMMA)) {
			throw new SyntaxException(ErrorReason.TOKEN_NOT_SUPPORTED,
					"Token doesn't supported");
		}
		currentState = nextState(currentState);
		setPreviousToken(token);

		return blankLexem;
	}

	@Override
	protected IToken getStartToken() {
		return ControlSymbol.LEFT_BRACE;
	}

	@Override
	protected IToken getTrailingToken() {
		return null;
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		if (ControlSymbol.COMMA.getTokenText().equals(token.getTokenText())
				|| ControlSymbol.RIGHT_BRACE.getTokenText().equals(
						token.getTokenText())) {
			currentState = State.COMMA;
		}
		return ControlSymbol.RIGHT_BRACE.getTokenText().equals(
				token.getTokenText());
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.COMMA;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		HashSet<IToken> supportedTokens = new HashSet<IToken>();
		supportedTokens.addAll(Arrays.asList((IToken) ControlSymbol.COMMA,
				(IToken) ControlSymbol.RIGHT_BRACE,
				(IToken) ControlSymbol.RIGHT_BRACKET));
		for (ClassDescriptionType type : ClassDescriptionType.values()) {
			supportedTokens.add(type.getToken());
		}
		return supportedTokens;
	}

	@Override
	protected String getLexemName() {
		return ClassDescriptionType.CHOICE.getToken().getTokenText();
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case CLASS_DEFINITION_NAME:
			return State.CLASS_DESCRIPTION;
		case CLASS_DESCRIPTION:
			return State.COMMA;
		case COMMA:
			return State.CLASS_DEFINITION_NAME;
		default:
			return null;
		}
	}

	@Override
	protected IState getInitialState() {
		return State.CLASS_DEFINITION_NAME;
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
			default:
				currentType = null;
				break;
			}
		} else {
			currentType = null;
		}
	}

}
