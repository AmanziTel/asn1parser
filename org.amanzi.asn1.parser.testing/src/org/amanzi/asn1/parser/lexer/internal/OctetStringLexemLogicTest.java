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

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testExpectedOctetStringWithSizeResult() throws Exception  {
		OctetStringLexem lexem = parseOctetStringLexem(TokenStreamsData.OCTET_STRING_SIZE);
		assertNotNull("parsed tokens cannot be null", lexem);
		assertEquals("type cannot be null", ClassDescriptionType.OCTET_STRING,
				lexem.getType());
		assertNotNull("size cannot be null", lexem.getSize());
		assertEquals("unexpected Size", Size.class, lexem.getSize().getClass());
		assertNotNull("lexem members is empty", lexem.getMembers());
		assertEquals("lexem doesn't contains members, but something founded",
				0, lexem.getMembers().size());

	}

	@Test
	public void testExpectedOctetStringWithMembersResult() throws Exception {
		OctetStringLexem lexem = parseOctetStringLexem(TokenStreamsData.OCTET_STRING_WITH_MEMBERS);
		assertNotNull("parsed data cannot be null", lexem);
		assertEquals("type cannot be null", ClassDescriptionType.OCTET_STRING,
				lexem.getType());
		assertNotNull("size cannot be null", lexem.getSize());
		assertEquals("unexpected Size", Size.class, lexem.getSize().getClass());
		assertNotNull("lexem members is empty", lexem.getMembers());
		assertEquals("lexem contains members, but nothing founded", 8, lexem
				.getMembers().size());

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
	 * @throws Exception
	 */
	private OctetStringLexem parseOctetStringLexem(String[] tokens)
			throws Exception {
		IStream<IToken> tokenStream = new TestTokenStream(tokens);
		OctetStringLexemLogic logic = new OctetStringLexemLogic(tokenStream);
		return logic.parse(new OctetStringLexem());
	}
}
