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
import org.amanzi.asn1.parser.lexer.impl.SequenceOfLexem;
import org.amanzi.asn1.parser.lexer.impl.Size;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * SEQUENCE OF Lexem logic
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class SequenceOfLexemLogic extends
		AbstractFabricLogic<SequenceOfLexem, ILexem> {

	private static Set<IToken> supportedTokens;

	/**
	 * Supported tokens set initialization
	 */
	static {
		supportedTokens = new HashSet<IToken>(Arrays.asList(
				(IToken) ControlSymbol.RIGHT_BRACKET,
				(IToken) ReservedWord.SIZE, (IToken) ReservedWord.OF));
		for (ClassDescriptionType type : ClassDescriptionType.values()) {
			supportedTokens.add(type.getToken());
		}
	}

	/**
	 * States for {@link SequenceOfLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		SIZE, RIGHT_BRACKET, OF, DESCRIPTION
	}

	private ClassDescriptionType currentType;
	private IState currentState;

	public SequenceOfLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.SIZE;
	}

	@Override
	protected SequenceOfLexem parseToken(SequenceOfLexem blankLexem,
			IToken token) throws SyntaxException {
		if (currentState == State.SIZE) {
			blankLexem.setSize((Size) parseSubLogic(token));
		} else {
			if (currentState != State.RIGHT_BRACKET && currentState != State.OF
					&& currentState != State.DESCRIPTION) {
				throw new SyntaxException(ErrorReason.TOKEN_NOT_SUPPORTED,
						"Token doesn't supported");
			}
		}
		currentState = nextState(currentState);
		return blankLexem;
	}

	@Override
	protected SequenceOfLexem finishUp(SequenceOfLexem lexem, IToken token)
			throws SyntaxException {
		defineCurrentDescriptionType(token);
		ClassReference classReference = new ClassReference();
		if (currentType == null) {
			classReference.setName(token.getTokenText());
			descriptionManager.putReference(token.getTokenText(),
					classReference);
		} else {
			classReference
					.setClassDescription((IClassDescription) parseSubLogic(token));
		}
		lexem.setClassReference(classReference);
		return super.finishUp(lexem, token);
	}

	@Override
	protected IState getInitialState() {
		return State.SIZE;
	}

	@Override
	protected boolean skipFirstToken() {
		return false;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		return currentState == State.SIZE;
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		return currentState == State.DESCRIPTION;
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.DESCRIPTION;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		return supportedTokens;
	}

	@Override
	protected String getLexemName() {
		return ReservedWord.SEQUENCE_OF.getTokenText();
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
			default:
				return null;
			}
		}
		return new Size();
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case SIZE:
			return State.RIGHT_BRACKET;
		case RIGHT_BRACKET:
			return State.OF;
		case OF:
			return State.DESCRIPTION;
		default:
			return null;
		}
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
			}
		} else {
			currentType = null;
		}
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
