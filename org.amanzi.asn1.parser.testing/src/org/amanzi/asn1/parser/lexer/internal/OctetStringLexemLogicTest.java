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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.Size;
import org.amanzi.asn1.parser.token.IToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link OctetStringLexemLogic}
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class OctetStringLexemLogicTest {

	private static final int EXPECTED_SIZE = 8;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testExpectedOctetStringWithSizeResult() throws SyntaxException {
		OctetStringLexem lexem = parseOctetStringLexem(TokenStreamsData.OCTET_STRING_SIZE);
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				ClassDescriptionType.OCTET_STRING, lexem.getType());
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, lexem.getSize());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, Size.class, lexem
				.getSize().getClass());
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, lexem.getMembers());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, 0, lexem
				.getMembers().size());
	}

	@Test
	public void testExpectedOctetStringWithMembersResult()
			throws SyntaxException {
		OctetStringLexem lexem = parseOctetStringLexem(TokenStreamsData.OCTET_STRING_WITH_MEMBERS);
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				ClassDescriptionType.OCTET_STRING, lexem.getType());
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, lexem.getSize());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, Size.class, lexem
				.getSize().getClass());
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, lexem.getMembers());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, EXPECTED_SIZE,
				lexem.getMembers().size());

	}

	@Test
	public void testUnexpectedEndOfStream() throws SyntaxException {
		exception.expect(SyntaxException.class);
		exception
				.expectMessage(containsString(ErrorReason.UNEXPECTED_END_OF_STREAM
						.getMessage()));

		IStream<IToken> tokenStream = new TestTokenStream(new String[] { "(" });

		OctetStringLexemLogic logic = new OctetStringLexemLogic(tokenStream);
		logic.parse(new OctetStringLexem());
	}

	@Test
	public void testNoStartToken() throws SyntaxException {
		exception.expect(SyntaxException.class);
		exception.expectMessage(containsString(ErrorReason.NO_START_TOKEN
				.getMessage()));

		IStream<IToken> tokenStream = new TestTokenStream(new String[] { "1",
				",", "2", "}" });

		OctetStringLexemLogic logic = new OctetStringLexemLogic(tokenStream);
		logic.parse(new OctetStringLexem());
	}

	/**
	 * Create new token stream and parse OctetStringLexem
	 * 
	 * @param tokens
	 *            tokens array
	 * @return parsed lexem or null
	 * @throws SyntaxException
	 */
	private OctetStringLexem parseOctetStringLexem(String[] tokens)
			throws SyntaxException {
		IStream<IToken> tokenStream = new TestTokenStream(tokens);
		OctetStringLexemLogic logic = new OctetStringLexemLogic(tokenStream);
		return logic.parse(new OctetStringLexem());
	}
}
