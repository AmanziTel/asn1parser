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
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.ConstantFileLexem;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
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

	private static HashSet<IToken> supportedTokens;

	/**
	 * States for {@link ConstantFileLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		STARTED, ASSIGNMENT, BEGIN, END, DEFINITIONS_AUTOMATIC_TAGS
	}

	public ConstantFileLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.STARTED;
	}

	@Override
	protected ILexem createInitialSubLexem(IToken identifier) {
		return new ClassDefinition();
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.BEGIN;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		return token.isDynamic()
				&& FILE_DEFINITION_NAME_PATTERN.matcher(token.getTokenText())
						.matches();
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
	protected boolean isTrailingToken(IToken token) {
		String tokenText = token.getTokenText();

		if (ReservedWord.DEFINITIONS_AUTOMATIC_TAGS.equals(tokenText)) {
			currentState = State.DEFINITIONS_AUTOMATIC_TAGS;
		}

		return currentState == State.BEGIN;
	}

	@Override
	protected ConstantFileLexem parseToken(ConstantFileLexem blankLexem,
			IToken token) throws SyntaxException {
		if (currentState == State.DEFINITIONS_AUTOMATIC_TAGS) {
			blankLexem.setName(getPreviousToken().getTokenText());
		} else if (currentState == State.BEGIN) {
			if (tokenStream.hasNext()) {
				// TODO Create ConstantDefinition and save
				IToken nextToken = tokenStream.next(); 
						while (ReservedWord.END != nextToken) {
								
						}
			}
			
		}
		currentState = nextState(currentState);
		return blankLexem;
	}

	@Override
	protected ConstantFileLexem finishUp(ConstantFileLexem lexem, IToken token)
			throws SyntaxException {
		if (currentState == State.BEGIN) {
			while (true) {
				lexem.addConstant((ClassDefinition) parseSubLogic(ControlSymbol.ASSIGNMENT));
			}
		}
		return super.finishUp(lexem, token);
	}

	@Override
	protected IToken getTrailingToken() {
		return null;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		if (supportedTokens == null) {
			supportedTokens = new HashSet<IToken>(Arrays.asList(
					(IToken) ControlSymbol.ASSIGNMENT,
					(IToken) ReservedWord.BEGIN, (IToken) ReservedWord.END,
					(IToken) ReservedWord.DEFINITIONS_AUTOMATIC_TAGS));

			for (ClassDescriptionType type : ClassDescriptionType.values()) {
				supportedTokens.add(type.getToken());
			}
		}
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
			return State.DEFINITIONS_AUTOMATIC_TAGS;
		case DEFINITIONS_AUTOMATIC_TAGS:
			return State.ASSIGNMENT;
		case ASSIGNMENT:
			return State.BEGIN;
		case BEGIN:		
			return State.END;
		default:
			return null;
		}
	}

	@Override
	protected IState getInitialState() {
		return State.STARTED;
	}

}
