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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.ranges.impl.Range;
import org.amanzi.asn1.parser.token.IToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests of BitStringLexemLogic
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class BitStringLexemLogicTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testExpectedSizeResult() throws Exception {
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(TokenStreamsData.BIT_STRING_WITH_SIZE));
		sizeAssertion("1", "16", lexem);
		assertTrue(TokenStreamsData.UNEXPECTED_VALUE, lexem.getMembers()
				.isEmpty());
	}

	@Test
	public void testExpectedRangeSizeResult() throws Exception {
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(TokenStreamsData.BIT_STRING_WITH_RANGE_SIZE));
		sizeAssertion("5", "10", lexem);
		assertTrue(TokenStreamsData.UNEXPECTED_VALUE, lexem.getMembers()
				.isEmpty());
	}

	@Test
	public void testExpectedFirstConstRangeSizeResult() throws Exception {
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(TokenStreamsData.BIT_STRING_WITH_FIRST_CONST_RANGE_SIZE));

		Range actualRange = lexem.getSize().getRange();

		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, "hi",
				actualRange.getLowerBound());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, "10",
				actualRange.getUpperBound());
	}

	@Test
	public void testExpectedSecondConstRangeSizeResult() throws Exception {
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(TokenStreamsData.BIT_STRING_WITH_SECOND_CONST_RANGE_SIZE));

		Range actualRange = lexem.getSize().getRange();

		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, "14",
				actualRange.getLowerBound());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, "hello",
				actualRange.getUpperBound());
	}

	@Test
	public void testExpectedResultWithMembers() throws Exception {
		BitStringLexem lexem = getBitStringLexem(new TestTokenStream(
				TokenStreamsData.BIT_STRING_WITH_MEMBERS));
		sizeAssertion("1", "8", lexem);
		testMembers(lexem);
	}

	@Test
	public void testExpectedResultWithMembersAndSize() throws Exception {
		BitStringLexem lexem = getBitStringLexem(new TestTokenStream(
				TokenStreamsData.BIT_STRING_WITH_MEMBERS_AND_SIZE));

		sizeAssertion("1", "8", lexem);
		testMembers(lexem);
	}

	@Test
	public void testUnexpectedEndOfStream() throws SyntaxException {
		exception.expect(SyntaxException.class);
		exception
				.expectMessage(containsString(ErrorReason.UNEXPECTED_END_OF_STREAM
						.getMessage()));

		IStream<IToken> tokenStream = getSizeTokenStream(new String[] { "(",
				"SIZE", "(", "10" });
		BitStringLexemLogic logic = new BitStringLexemLogic(tokenStream);
		logic.parse(new BitStringLexem());
	}

	@Test
	public void testNoStartToken() throws SyntaxException {
		exception.expect(SyntaxException.class);
		exception.expectMessage(containsString(ErrorReason.NO_START_TOKEN
				.getMessage()));

		IStream<IToken> tokenStream = getSizeTokenStream(new String[] {
				"BIT_STRING", "(", "SIZE", "10", "}" });
		BitStringLexemLogic logic = new BitStringLexemLogic(tokenStream);
		logic.parse(new BitStringLexem());
	}

	private void testMembers(BitStringLexem lexem) {
		int expectedMemberValue = 1;
		for (String value : lexem.getMembers().values()) {
			assertEquals(TokenStreamsData.UNEXPECTED_VALUE
					+ expectedMemberValue,
					String.valueOf(expectedMemberValue++), value);
		}
	}

	private BitStringLexem getBitStringLexem(IStream<IToken> tokenStream)
			throws SyntaxException {
		BitStringLexemLogic logic = new BitStringLexemLogic(tokenStream);
		return logic.parse(new BitStringLexem());
	}

	private void sizeAssertion(String lowerBound, String upperBound,
			BitStringLexem lexem) throws SyntaxException {
		Range expectedRange = prepareSizeRange(lowerBound, upperBound);
		Range actualRange = lexem.getSize().getRange();

		int size = actualRange.computeRange();

		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, size, lexem
				.getSize().getSize());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, "BIT_STRING", lexem
				.getType().name());

		assertNull(TokenStreamsData.UNEXPECTED_VALUE, lexem.getValue());

		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				expectedRange.computeRange(), actualRange.computeRange());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				expectedRange.getLowerBound(), actualRange.getLowerBound());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				expectedRange.getUpperBound(), actualRange.getUpperBound());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				expectedRange.getLowerBoundValue(),
				actualRange.getLowerBoundValue());
	}

	private IStream<IToken> getSizeTokenStream(String... tokens) {
		return new TestTokenStream(tokens);
	}

	private Range prepareSizeRange(String lowerBound, String upperBound) {
		Range expectedRange = new Range();
		expectedRange.setLowerBound(lowerBound);
		expectedRange.setUpperBound(upperBound);
		return expectedRange;
	}
}
