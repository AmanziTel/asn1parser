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
import org.apache.commons.lang3.ArrayUtils;
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
		String[] bitString = new String[] { "BIT_STRING", "(" };
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(bitString,
				TokenStreamsData.SIZE));
		sizeAssertion("1", "16", lexem);
		assertTrue("unexpected BIT_STRING members", lexem.getMembers()
				.isEmpty());
	}

	@Test
	public void testExpectedRangeSizeResult() throws Exception {
		String[] bitString = new String[] { "BIT_STRING", "(" };
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(bitString,
				TokenStreamsData.RANGE_SIZE));
		sizeAssertion("5", "10", lexem);
		assertTrue("unexpected BIT_STRING members", lexem.getMembers()
				.isEmpty());
	}

	@Test
	public void testExpectedFirstConstRangeSizeResult() throws Exception {
		String[] bitString = new String[] { "BIT_STRING", "(" };
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(bitString,
				TokenStreamsData.FIRST_CONST_RANGE_SIZE));

		Range actualRange = lexem.getSize().getRange();

		assertNotNull(lexem);
		assertEquals("unexpected BIT_STRING lower bound", "hi",
				actualRange.getLowerBound());
		assertEquals("unexpected BIT_STRING upper bound", "10",
				actualRange.getUpperBound());
	}

	@Test
	public void testExpectedSecondConstRangeSizeResult() throws Exception {
		String[] bitString = new String[] { "BIT_STRING", "(" };
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(bitString,
				TokenStreamsData.SECOND_CONST_RANGE_SIZE));

		Range actualRange = lexem.getSize().getRange();

		assertNotNull(lexem);
		assertEquals("unexpected BIT_STRING lower bound", "14",
				actualRange.getLowerBound());
		assertEquals("unexpected BIT_STRING upper bound", "hello",
				actualRange.getUpperBound());
	}

	@Test
	public void testExpectedResultWithMembers() throws Exception {
		String[] bitString = new String[] { "BIT_STRING" };
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(bitString,
				TokenStreamsData.BIT_STRING_WITH_MEMBERS));

		sizeAssertion("1", "8", lexem);
		testMembers(lexem);
	}

	@Test
	public void testExpectedResultWithMembersAndSize() throws Exception {
		String[] bitString = new String[] { "BIT_STRING" };
		BitStringLexem lexem = getBitStringLexem(getSizeTokenStream(bitString,
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

		String[] bitString = new String[] { "BIT_STRING", "(" };

		IStream<IToken> tokenStream = getSizeTokenStream(bitString,
				new String[] { "(", "SIZE", "(", "10" });
		BitStringLexemLogic logic = new BitStringLexemLogic(tokenStream);
		logic.parse(new BitStringLexem());
	}

	@Test
	public void testNoStartToken() throws SyntaxException {
		exception.expect(SyntaxException.class);
		exception.expectMessage(containsString(ErrorReason.NO_START_TOKEN
				.getMessage()));

		String[] bitString = new String[] { "BIT_STRING", "(" };

		IStream<IToken> tokenStream = getSizeTokenStream(bitString,
				new String[] { "(", "SIZE", "10", "}" });
		BitStringLexemLogic logic = new BitStringLexemLogic(tokenStream);
		logic.parse(new BitStringLexem());
	}

	private void testMembers(BitStringLexem lexem) {
		int expectedMemberValue = 1;
		for (String value : lexem.getMembers().values()) {
			assertEquals("unexpected member: " + expectedMemberValue,
					String.valueOf(expectedMemberValue++), value);
		}
	}

	private BitStringLexem getBitStringLexem(IStream<IToken> tokenStream)
			throws Exception {
		BitStringLexemLogic logic = new BitStringLexemLogic(tokenStream);
		return logic.parse(new BitStringLexem());
	}

	private void sizeAssertion(String lowerBound, String upperBound,
			BitStringLexem lexem) throws Exception {

		Range expectedRange = prepareSizeRange(lowerBound, upperBound);
		Range actualRange = lexem.getSize().getRange();

		int size = actualRange.computeRange();

		assertNotNull(lexem);
		assertEquals("unexpected parsed SIZE of BIT_STRING", size, lexem
				.getSize().getSize());
		assertEquals("unexpected lexem type", "BIT_STRING", lexem.getType()
				.name());

		assertNull("unexpected BIT_STRING value", lexem.getValue());

		assertEquals("unexpected BIT_STRING SIZE Range",
				expectedRange.computeRange(), actualRange.computeRange());
		assertEquals("unexpected BIT_STRING SIZE Range lower Bound",
				expectedRange.getLowerBound(), actualRange.getLowerBound());
		assertEquals("unexpected BIT_STRING SIZE Range upper Bound",
				expectedRange.getUpperBound(), actualRange.getUpperBound());
		assertEquals("unexpected BIT_STRING SIZE Range integer lower bound",
				expectedRange.getLowerBoundValue(),
				actualRange.getLowerBoundValue());
	}

	private IStream<IToken> getSizeTokenStream(String[] firstTokens,
			String... lastTokens) {
		return new TestTokenStream(ArrayUtils.addAll(firstTokens, lastTokens));
	}

	private Range prepareSizeRange(String lowerBound, String upperBound) {
		Range expectedRange = new Range();
		expectedRange.setLowerBound(lowerBound);
		expectedRange.setUpperBound(upperBound);
		return expectedRange;
	}
}
