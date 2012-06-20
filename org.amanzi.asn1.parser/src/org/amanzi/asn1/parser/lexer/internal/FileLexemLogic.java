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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

	private static HashSet<IToken> supportedTokens;
	private static List<String> importsList;

	/**
	 * States enumeration for {@link FileLexemLogic}
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private enum State implements IState {
		STARTED, BEGIN, IMPORT, IMPORT_VALUE, FROM, IMPORT_FILE_NAME, CLASS_DEFINITION, COMMA, DEFINITIONS_DELIMETER, END, ASSIGNMENT
	}

	private IState currentState;

	public FileLexemLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
		currentState = State.STARTED;
		importsList = new ArrayList<String>(0);
	}

	@Override
	protected FileLexem parseToken(FileLexem blankLexem, IToken token)
			throws SyntaxException {
		if (currentState == State.ASSIGNMENT) {
			blankLexem.setName(getPreviousToken().getTokenText());
		}
		if (currentState == State.IMPORT_VALUE) {
			importsList.add(token.getTokenText());
		}
		if (currentState == State.IMPORT_FILE_NAME) {
			// schema getFile();
			// blankLexem.addImportValueFromFile(file, importsList)
			importsList.clear();
		}
		if (currentState == State.CLASS_DEFINITION) {
			ClassDefinition definition = (ClassDefinition) parseSubLogic(token);
			blankLexem.addClassDefinitionName(definition.getClassName());
		}

		currentState = nextState(currentState);
		setPreviousToken(token);

		return blankLexem;
	}

	@Override
	protected boolean skipFirstToken() {
		// first token doesn't skipped, cuz this is File definition name
		return false;
	}

	@Override
	protected boolean canFinish() {
		return currentState == State.END;
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
			currentState = State.IMPORT_FILE_NAME;
		}
		if (ReservedWord.IMPORTS.getTokenText().equals(tokenText)) {
			currentState = State.IMPORT;
		}
		if (ControlSymbol.SEMIKOLON.getTokenText().equals(tokenText)) {
			currentState = State.CLASS_DEFINITION;
		}
		if (ReservedWord.DEFINITIONS_AUTOMATIC_TAGS.equals(tokenText)) {
			currentState = State.ASSIGNMENT;
		}
		// TODO class definitions delimeter ??
		if (ControlSymbol.RIGHT_BRACE.getTokenText().equals(tokenText)
				|| ControlSymbol.RIGHT_BRACKET.getTokenText().equals(tokenText)) {
			currentState = State.DEFINITIONS_DELIMETER;
		}

		return ReservedWord.END.getTokenText().equals(tokenText);
	}

	@Override
	protected IToken getTrailingToken() {
		return ReservedWord.END;
	}

	@Override
	protected Set<IToken> getSupportedTokens() {
		if (supportedTokens == null) {
			supportedTokens = new HashSet<IToken>();

			supportedTokens.addAll(Arrays.asList((IToken) ReservedWord.BEGIN,
					(IToken) ReservedWord.IMPORTS, (IToken) ReservedWord.END,
					(IToken) ReservedWord.FROM,
					(IToken) ControlSymbol.ASSIGNMENT,
					(IToken) ControlSymbol.SEMIKOLON));
			
			for (ClassDescriptionType type : ClassDescriptionType.values()) {
				supportedTokens.add(type.getToken());
			}
		}
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
			return State.IMPORT;
		case IMPORT:
			return State.IMPORT_VALUE;
		case IMPORT_VALUE:
			return State.COMMA;
		case COMMA:
			return State.IMPORT_VALUE;
		case FROM:
			return State.IMPORT_FILE_NAME;
		case IMPORT_FILE_NAME:
			return State.IMPORT_VALUE;
		case CLASS_DEFINITION:
			return State.END;
		case DEFINITIONS_DELIMETER:
			return State.CLASS_DEFINITION;
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
