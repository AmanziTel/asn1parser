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

import java.util.Set;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.utils.DescriptionManager;

/**
 * Abstract implementation of Lexem Logic
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
abstract class AbstractLexemLogic<T extends ILexem> implements ILexemLogic<T> {

	interface IState {

	}

	protected IStream<IToken> tokenStream;

	private IToken previousToken;

	protected IState currentState;

	protected DescriptionManager descriptionManager;

	public AbstractLexemLogic(IStream<IToken> tokenStream) {
		this.tokenStream = tokenStream;
		this.currentState = getInitialState();
		this.descriptionManager = DescriptionManager.getInstance();
	}

	@Override
	public T parse(T blankLexem) throws SyntaxException {
		boolean parsed = false;

		boolean started = false;

		while (tokenStream.hasNext()) {
			IToken token = tokenStream.next();

			// check start token
			if (!started) {
				if (!isStartToken(token)) {
					throw new SyntaxException(ErrorReason.NO_START_TOKEN, "<"
							+ getLexemName() + "> Lexem should start with <"
							+ getStartToken() + "> Token, but found <" + token
							+ ">");
				}
				started = true;

				if (skipFirstToken()) {
					continue;
				}
			}

			// check end token
			if (isTrailingToken(token)) {
				if (!canFinish()) {
					throw new SyntaxException(
							ErrorReason.UNEXPECTED_END_OF_LEXEM,
							"Lexem can't be finished after <"
									+ getPreviousToken() + "> Lexem");
				}
				parsed = true;

				blankLexem = finishUp(blankLexem, token);

				break;
			}

			// check supported token
			if (!token.isDynamic()) {
				if (!getSupportedTokens().contains(token)) {
					throw new SyntaxException(ErrorReason.TOKEN_NOT_SUPPORTED,
							"Token <" + token + "> not supported in Lexem <"
									+ getLexemName() + ">");
				}
			}

			blankLexem = parseToken(blankLexem, token);

			setPreviousToken(token);
		}

		if (!parsed) {
			throw new SyntaxException(ErrorReason.UNEXPECTED_END_OF_STREAM,
					"Stream has no element to finish Lexem <" + getLexemName()
							+ ">");
		}

		return blankLexem;
	}

	protected void setPreviousToken(IToken token) {
		this.previousToken = token;
	}

	protected IToken getPreviousToken() {
		return previousToken;
	}

	protected boolean isStartToken(IToken token) {
		return token.equals(getStartToken());
	}

	protected boolean isTrailingToken(IToken token) {
		return token.equals(getTrailingToken());
	}

	protected T finishUp(T lexem, IToken token) throws SyntaxException {
		return lexem;
	}

	protected boolean skipFirstToken() {
		return true;
	}

	protected abstract boolean canFinish();

	protected abstract IToken getStartToken();

	protected abstract T parseToken(T blankLexem, IToken token)
			throws SyntaxException;

	protected abstract IToken getTrailingToken();

	protected abstract Set<IToken> getSupportedTokens();

	protected abstract String getLexemName();

	protected abstract IState nextState(IState currentState);

	protected abstract IState getInitialState();

}
