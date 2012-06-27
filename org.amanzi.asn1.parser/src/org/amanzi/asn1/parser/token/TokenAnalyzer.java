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

package org.amanzi.asn1.parser.token;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.amanzi.asn1.parser.AbstractStream;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;
import org.amanzi.asn1.parser.token.impl.SimpleToken;
import org.apache.commons.lang3.ArrayUtils;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class TokenAnalyzer extends AbstractStream<IToken> {

	/** int END_OF_FILE_CHARACTER field */
	private static final int END_OF_FILE_CHARACTER = -1;

	/**
	 * Default size of Stream Buffer
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 100;

	/**
	 * Character for a Next Line
	 */
	private static final int NEXT_LINE_CHARACTER = '\n';

	/**
	 * Default Separations Characters
	 */
	private static final int[] DEFAULT_SKIP_CHARACTERS = new int[] { ' ',
			NEXT_LINE_CHARACTER, '\t', '\r' };

	/*
	 * Cache for Tokens to prevent re-creation of similar tokens
	 */
	private Map<String, IToken> tokenCache = new HashMap<String, IToken>();

	/*
	 * Input Stream for an Analyzer
	 */
	private InputStream inputStream;

	/*
	 * String to a Default Token that trails main Token
	 */
	private String trailingToken;

	/*
	 * True if parsed token is BIT STRING or OCTET STRING
	 */
	private boolean isBitOrOctetString;

	private boolean isPreviousCharacter;
	private char previousCharacter;

	/**
	 * Creates a TokenAnalyzer based on InputStream
	 * 
	 * @param inputStream
	 */
	public TokenAnalyzer(InputStream inputStream) {
		this.inputStream = new BufferedInputStream(inputStream,
				DEFAULT_BUFFER_SIZE);
	}

	@Override
	protected IToken readNextElement() {
		try {
			String tokenText = readNextToken();

			if (tokenText.isEmpty()) {
				return null;
			}

			IToken token = tokenCache.get(tokenText);
			if (token == null) {
				token = convertToToken(tokenText);
				tokenCache.put(tokenText, token);
			}

			return token;
		} catch (IOException e) {
			// TODO: add logger, log exception here
		}

		return null;
	}

	/**
	 * Converts read Text to a Token entity
	 * 
	 * @param tokenText
	 * @return
	 */
	private IToken convertToToken(String tokenText) {
		IToken result = null;

		result = ControlSymbol.findByText(tokenText);

		if (result == null) {
			result = ReservedWord.findByText(tokenText);
		}

		if (result == null) {
			result = new SimpleToken(tokenText);
		}

		return result;
	}

	/**
	 * Read next Token from an input stream
	 * 
	 * @return
	 */
	private String readNextToken() throws IOException {
		if (trailingToken != null) {
			String toReturn = trailingToken;
			trailingToken = null;
			return toReturn;
		}

		StringBuffer token = new StringBuffer();
		
		parsing_loop: while (true) {
			int read = inputStream.read();

			if (read != END_OF_FILE_CHARACTER) {
				char readChar = (char) read;

				if (isPreviousCharacter) {
					token.append(previousCharacter);
				}

				isPreviousCharacter = false;
				boolean space = false;

				if (ArrayUtils.contains(DEFAULT_SKIP_CHARACTERS, read)) {
					if (token.length() == 0) {
						continue;
					} else {
						space = true;
					}
				}

				token.append(readChar);
				
				if (isBitOrOctetString) {
					isBitOrOctetString = false;
					if ((!ControlSymbol.LEFT_BRACE.getTokenText().equals(
							token.toString()))
							&& (!ControlSymbol.LEFT_BRACKET.getTokenText()
									.equals(token.toString()))) {
						isPreviousCharacter = true;
						previousCharacter = readChar;
						return ControlSymbol.RIGHT_BRACE.getTokenText();
					}
				}

				String tokenString = token.toString();

				for (ReservedWord reservedWord : ReservedWord
						.getPossibleTokens(readChar)) {
					if (reservedWord.getTokenText().startsWith(tokenString)) {
						continue parsing_loop;
					}
				}
				
				if (space) {
					String result = token.toString().trim();
					if (ReservedWord.BIT_STRING.getTokenText().equals(result)
							|| ReservedWord.OCTET_STRING.getTokenText().equals(
									result)) {
						isBitOrOctetString = true;
					}
					return result;
				}

				for (ControlSymbol singleToken : ControlSymbol
						.getPossibleTokens(readChar)) {
					if (singleToken.checkText(tokenString)) {
						trailingToken = singleToken.getTokenText();
						String toReturn = singleToken.cut(tokenString);

						if (toReturn.isEmpty()) {
							toReturn = trailingToken;
							trailingToken = null;
						}

						if (singleToken == ControlSymbol.COMMENT) {
							skipUntilNextLine();
							token.delete(0, token.length());
							break;
						}

						return toReturn;

					}
				}
			} else {
				if ((token.length() > 0) || (read == END_OF_FILE_CHARACTER)) {
					break;
				}
			}
		}

		return token.toString();
	}

	/**
	 * In case of
	 */
	private void skipUntilNextLine() throws IOException {
		int read = END_OF_FILE_CHARACTER;

		do {
			read = inputStream.read();
		} while ((read != END_OF_FILE_CHARACTER)
				&& (read != NEXT_LINE_CHARACTER));
	}

}
