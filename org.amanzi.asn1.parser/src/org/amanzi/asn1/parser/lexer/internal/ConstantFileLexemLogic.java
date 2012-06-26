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
import java.util.regex.Pattern;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.ConstantDefinition;
import org.amanzi.asn1.parser.lexer.impl.ConstantFileLexem;
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
 * Logic for {@link ConstantFileLexem}
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class ConstantFileLexemLogic extends
		AbstractFabricLogic<ConstantFileLexem, ILexem> {

	private static final Pattern FILE_DEFINITION_NAME_PATTERN = Pattern
			.compile("[a-zA-Z\\d0-9-]+");

	private static Set<IToken> supportedTokens;

	/**
	 * Supported token set initialization
	 */
	static {
		supportedTokens = new HashSet<IToken>(Arrays.asList(
				(IToken) ControlSymbol.ASSIGNMENT, (IToken) ReservedWord.BEGIN,
				(IToken) ReservedWord.END,
				(IToken) ReservedWord.DEFINITIONS_AUTOMATIC_TAGS));
		for (ClassDescriptionType type : ClassDescriptionType.values()) {
			supportedTokens.add(type.getToken());
		}
	}

	/**
	 * States for {@link ConstantFileLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		STARTED, ASSIGNMENT, CONSTANTS, DEFINITION_AUTOMATIC_TAGS
	}

	private IState currentState;
	private ClassDescriptionType currentType;

	public ConstantFileLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.STARTED;
	}

	@Override
	protected ConstantFileLexem parseToken(ConstantFileLexem blankLexem,
			IToken token) throws SyntaxException {
		if (currentState == State.DEFINITION_AUTOMATIC_TAGS) {
			blankLexem.setName(getPreviousToken().getTokenText());
		}
		currentState = nextState(currentState);
		return blankLexem;
	}

	@Override
	protected ConstantFileLexem finishUp(ConstantFileLexem lexem, IToken token)
			throws SyntaxException {
		if (currentState == State.CONSTANTS) {
			while (tokenStream.hasNext()) {
				String constantName = tokenStream.next().getTokenText();
				if (ReservedWord.END.getTokenText().equals(constantName)) {
					break;
				}
				if (tokenStream.hasNext()) {
					IToken constantDescription = tokenStream.next();
					defineCurrentDescriptionType(constantDescription);
					ConstantDefinition definition = new ConstantDefinition();
					definition.setName(constantName);
					definition
							.setDescription((IClassDescription) parseSubLogic(constantDescription));
					lexem.addConstant(definition);
				}

			}
		}
		return super.finishUp(lexem, token);
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.CONSTANTS;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		return token.isDynamic()
				&& FILE_DEFINITION_NAME_PATTERN.matcher(token.getTokenText())
						.matches();
	}

	@Override
	protected IState getInitialState() {
		return State.STARTED;
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		return currentState == State.CONSTANTS;
	}

	@Override
	protected IToken getTrailingToken() {
		return null;
	}

	@Override
	protected IToken getStartToken() {
		return null;
	}

	@Override
	protected boolean skipFirstToken() {
		return false;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		return supportedTokens;
	}

	@Override
	protected String getLexemName() {
		return "ConstantFileLexem";
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case STARTED:
			return State.DEFINITION_AUTOMATIC_TAGS;
		case DEFINITION_AUTOMATIC_TAGS:
			return State.ASSIGNMENT;
		case ASSIGNMENT:
			return State.CONSTANTS;
		default:
			return null;
		}
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
}
