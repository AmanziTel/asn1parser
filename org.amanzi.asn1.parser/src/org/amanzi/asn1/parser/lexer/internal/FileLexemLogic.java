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
import org.amanzi.asn1.parser.lexer.impl.FileLexem;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * Logic for {@link FileLexem}
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class FileLexemLogic extends AbstractFabricLogic<FileLexem, ILexem> {

	private static final Pattern FILE_DEFINITION_NAME_PATTERN = Pattern
			.compile("[a-zA-Z\\d0-9-]+");

	private static Set<IToken> supportedTokens;
	private Set<String> importsSet = new HashSet<String>(0);

	/**
	 * Supported tokens Set initialization
	 */
	static {
		supportedTokens = new HashSet<IToken>(Arrays.asList(
				(IToken) ReservedWord.BEGIN, (IToken) ReservedWord.IMPORTS,
				(IToken) ReservedWord.FROM, (IToken) ControlSymbol.ASSIGNMENT,
				(IToken) ControlSymbol.SEMIKOLON,
				(IToken) ReservedWord.DEFINITIONS_AUTOMATIC_TAGS,
				(IToken) ControlSymbol.COMMA,
				(IToken) ControlSymbol.RIGHT_BRACE));
		for (ClassDescriptionType type : ClassDescriptionType.values()) {
			supportedTokens.add(type.getToken());
		}

	}

	/**
	 * States enumeration for {@link FileLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		STARTED, BEGIN, IMPORTS, IMPORT_VALUE, FROM, IMPORT_FILE_NAME, DEFINITION, COMMA, ASSIGNMENT
	}

	private IState currentState;

	public FileLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.STARTED;
	}

	@Override
	protected FileLexem parseToken(FileLexem blankLexem, IToken token)
			throws SyntaxException {
		switch ((State) currentState) {
		case ASSIGNMENT:
			blankLexem.setName(getPreviousToken().getTokenText());
			break;
		case IMPORT_VALUE:
			importsSet.add(token.getTokenText());
			break;
		case IMPORT_FILE_NAME:
			blankLexem.addImports(token.getTokenText(), importsSet);
			importsSet.clear();
			break;
		}

		currentState = nextState(currentState);

		return blankLexem;
	}

	@Override
	protected FileLexem finishUp(FileLexem lexem, IToken token)
			throws SyntaxException {
		if (currentState == State.DEFINITION) {
			while (tokenStream.hasNext()) {
				ClassDefinition definition = (ClassDefinition) parseSubLogic(ControlSymbol.ASSIGNMENT);
				if (definition.getClassDescription() != null
						&& definition.getClassName() != null) {
					lexem.addClassDefinition(definition);
				}
			}
		}
		return super.finishUp(lexem, token);
	}

	@Override
	protected boolean skipFirstToken() {
		// first token doesn't skipped, cuz this is File definition name
		return false;
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.DEFINITION;
	}

	@Override
	protected boolean isStartToken(IToken token) {
		return token.isDynamic()
				&& FILE_DEFINITION_NAME_PATTERN.matcher(token.getTokenText())
						.matches();
	}

	@Override
	protected boolean isTrailingToken(IToken token) {
		String tokenText = token.getTokenText();
		if (ReservedWord.FROM.getTokenText().equals(tokenText)) {
			currentState = State.FROM;
		}
		if (ReservedWord.IMPORTS.getTokenText().equals(tokenText)) {
			currentState = State.IMPORTS;
		}
		if (ControlSymbol.SEMIKOLON.getTokenText().equals(tokenText)) {
			currentState = State.DEFINITION;
		}
		if (ReservedWord.DEFINITIONS_AUTOMATIC_TAGS.getTokenText().equals(
				tokenText)) {
			currentState = State.ASSIGNMENT;
		}

		return currentState == State.DEFINITION;
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
		return "FileLexem";
	}

	@Override
	protected IState nextState(IState currentState) {
		switch ((State) currentState) {
		case STARTED:
			return State.ASSIGNMENT;
		case ASSIGNMENT:
			return State.BEGIN;
		case BEGIN:
			return State.IMPORTS;
		case IMPORTS:
			return State.IMPORT_VALUE;
		case IMPORT_VALUE:
			return State.COMMA;
		case COMMA:
			return State.IMPORT_VALUE;
		case FROM:
			return State.IMPORT_FILE_NAME;
		case IMPORT_FILE_NAME:
			return State.IMPORT_VALUE;
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
		return new ClassDefinition();
	}

	@Override
	protected IToken getStartToken() {
		return null;
	}

}
