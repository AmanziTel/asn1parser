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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.ClassReference;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceOfLexem;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * Logic of parsing for ClassDefinition element
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class ClassDefinitionLogic extends
		AbstractFabricLogic<ClassDefinition, IClassDescription> {

	private static final Pattern CLASS_DEFINITION_NAME_PATTERN = Pattern
			.compile("[a-zA-Z\\d0-9-]+");

	private static Set<IToken> supportedTokens;

	/**
	 * Supported tokens set initialization
	 */
	static {
		supportedTokens = new HashSet<IToken>();
		supportedTokens.add(ControlSymbol.ASSIGNMENT);
		for (ClassDescriptionType type : ClassDescriptionType.values()) {
			supportedTokens.add(type.getToken());
		}

	}

	private enum State implements IState {
		STARTED, ASSIGNMENT, DEFINITION;
	}

	private ClassDescriptionType currentType;

	/**
	 * @param tokenStream
	 */
	public ClassDefinitionLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.DEFINITION;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		return token.isDynamic()
				&& CLASS_DEFINITION_NAME_PATTERN.matcher(token.getTokenText())
						.matches()
				|| ReservedWord.END.getTokenText().equals(token.getTokenText())
				|| ControlSymbol.RIGHT_BRACKET.getTokenText().equals(
						token.getTokenText());
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		for (ClassDescriptionType type : ClassDescriptionType.values()) {
			if (token.equals(type.getToken())) {
				currentType = type;
				return true;
			}
		}
		if (ReservedWord.END.getTokenText().equals(token.getTokenText())
				|| ControlSymbol.RIGHT_BRACKET.getTokenText().equals(
						token.getTokenText())) {
			currentState = State.DEFINITION;
		}
		return currentState == State.DEFINITION;
	}

	@Override
	protected ClassDefinition parseToken(ClassDefinition blankLexem,
			IToken token) throws SyntaxException {
		switch ((State) currentState) {
		case STARTED:
			blankLexem.setClassName(token.getTokenText());
			break;
		case ASSIGNMENT:
			if (!token.equals(ControlSymbol.ASSIGNMENT)) {
				throw new SyntaxException(ErrorReason.TOKEN_NOT_SUPPORTED,
						"Only <::=> token can be placed after ClassDefinition name");
			}
			break;
		}

		currentState = nextState(currentState);

		return blankLexem;
	}

	@Override
	protected IToken getTrailingToken() {
		return null;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		return supportedTokens;
	}

	@Override
	protected String getLexemName() {
		return "Class Definition";
	}

	@Override
	protected ClassDefinition finishUp(ClassDefinition lexem, IToken token)
			throws SyntaxException {
		if (!ReservedWord.END.getTokenText().equals(token.getTokenText())
				&& !ControlSymbol.RIGHT_BRACKET.getTokenText().equals(
						token.getTokenText())) {
			defineCurrentDescriptionType(token);
			if (currentType == null) {
				ClassReference reference = new ClassReference();
				reference.setName(token.getTokenText());
				lexem.setClassReference(reference);
			} else {
				lexem.setClassDescription(parseSubLogic(token));
				descriptionManager.putClassDefinition(lexem.getClassName(),
						lexem.getClassDescription());
			}
		}
		return super.finishUp(lexem, token);
	}

	@Override
	protected IClassDescription createInitialSubLexem(IToken identifier) {
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
		default:
			return null;
		}
	}

	@Override
	protected IToken getStartToken() {
		// no start token
		return null;
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case STARTED:
			return State.ASSIGNMENT;
		case ASSIGNMENT:
			return State.DEFINITION;
		default:
			return null;
		}
	}

	@Override
	protected IState getInitialState() {
		return State.STARTED;
	}

	@Override
	protected boolean skipFirstToken() {
		return false;
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
}
