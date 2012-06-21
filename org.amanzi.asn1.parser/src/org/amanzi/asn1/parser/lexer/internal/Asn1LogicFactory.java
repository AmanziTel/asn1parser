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

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * Factory that creates Parse-Logic classes
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class Asn1LogicFactory {

	/**
	 * Creates a Token by KeyWord
	 * 
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <V extends ILexem, T extends ILexemLogic<V>> T createLogic(
			ReservedWord token, IStream<IToken> tokenStream) {
		if (token != null) {
			switch (token) {
			case ENUMERATED:
				return (T) new EnumeratedLogic(tokenStream);
			case SIZE:
				return (T) new SizeLexemLogic(tokenStream);
			case SEQUENCE:
				return (T) new SequenceLexemLogic(tokenStream);
			case SEQUENCE_OF:
				return (T) new SequenceOfLexemLogic(tokenStream);
			case INTEGER:
				return (T) new IntegerLexemLogic(tokenStream);
			case BIT_STRING:
				return (T) new BitStringLexemLogic(tokenStream);
			case CHOICE:
				return (T) new ChoiceLexemLogic(tokenStream);
			case OCTET_STRING:
				return (T) new OctetStringLexemLogic(tokenStream);			
			}

		}

		return null;
	}

	/**
	 * Creates a Token by Terminate symbol
	 * 
	 * @param token
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <V extends ILexem, T extends ILexemLogic<V>> T createLogic(
			ControlSymbol symbol, IStream<IToken> tokenStream) {
		if (symbol != null) {
			switch (symbol) {
			case ASSIGNMENT:
				return (T) new ClassDefinitionLogic(tokenStream);
			}
		}

		return null;
	}

	/**
	 * Creates a Token based on it's Parsed representation
	 * 
	 * @param token
	 * @return
	 */
	public static <T extends ILexemLogic<?>> T createLogic(IToken token,
			IStream<IToken> tokenStream) {
		if (token instanceof ReservedWord) {
			return createLogic((ReservedWord) token, tokenStream);
		} else if (token instanceof ControlSymbol) {
			return createLogic((ControlSymbol) token, tokenStream);
		}

		return null;
	}

}
