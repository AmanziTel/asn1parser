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
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.ranges.impl.Range;
import org.amanzi.asn1.parser.token.IToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for Integer Definition Logic
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class IntegerLexemLogicTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testExpectedIntegerRangeResult() throws Exception {
		IntegerLexem lexem = getIntegerLexem(TokenStreamsData.INTEGER_RANGE);
		Range expectedRange = getExpectedRange("1", "45");
		Range actualRange = lexem.getRange();

		assertNotNull("result cannot be null", lexem);
		assertEquals("unexpected lexem type", ClassDescriptionType.INTEGER,
				lexem.getType());
		assertEquals("unexpected Range LOWER BOUND",
				expectedRange.getLowerBound(), actualRange.getLowerBound());
		assertEquals("unexpected Range UPPER BOUND",
				expectedRange.getUpperBound(), actualRange.getUpperBound());

		assertEquals("unexpected Size value", 0, lexem.getSize());
		assertEquals("unexpected computed Range size",
				expectedRange.computeRange(), actualRange.computeRange());

	}

	@Test
	public void testExpectedLeftConstRangeResult() throws Exception {
		IntegerLexem lexem = getIntegerLexem(TokenStreamsData.INTEGER_LEFT_CONST_RANGE);
		rangeAssertion("hello", "10", lexem, true);
		// size == 0 cuz left bound is constant
		assertEquals("unexpected Size value", 0, lexem.getSize());
	}

	@Test
	public void testExpectedRightConstRangeResult() throws Exception {
		IntegerLexem lexem = getIntegerLexem(TokenStreamsData.INTEGER_RIGHT_CONST_RANGE);
		rangeAssertion("23", "hello", lexem, true);
		assertEquals("unexpected integer lexem size ", 0, lexem.getSize());
	}

	@Test
	public void testExpectedConstsRangeResult() throws Exception {
		IntegerLexem lexem = getIntegerLexem(TokenStreamsData.INTEGER_CONSTS_RANGE);
		rangeAssertion("left", "right", lexem, true);
		assertEquals("unexpected integer lexem size", 0, lexem.getSize());
	}

	@Test
	public void testExpectedAssignmentResult() throws Exception {
		IntegerLexem lexem = getIntegerLexem(TokenStreamsData.INTEGER_ASSIGNMENT);
		rangeAssertion("1", "20", lexem, false);
	}

	@Test
	public void testExpectedIntegerWithOneMemberResult() throws Exception {
		IntegerLexem lexem = getIntegerLexem(TokenStreamsData.INTEGER_WITH_ONE_MEMBER);
		assertNotNull("result cannot be null", lexem);
		assertEquals("unexpected lexem type", ClassDescriptionType.INTEGER,
				lexem.getType());		
		assertEquals("unexpected Size value", 0, lexem.getSize());		
	}

	@Test
	public void testNoStartTokenResult() throws Exception {
		exception.expect(SyntaxException.class);
		exception.expectMessage(containsString(ErrorReason.NO_START_TOKEN
				.getMessage()));

		getIntegerLexem(new String[] { ";:=", "10" });
	}

	@Test
	public void testUnexpectedEndOfStream() throws Exception {
		exception.expect(SyntaxException.class);
		exception
				.expectMessage(containsString(ErrorReason.UNEXPECTED_END_OF_STREAM
						.getMessage()));
		getIntegerLexem(new String[] { "(", "1", ",,", "10" });
	}

	/**
	 * Get parsed integer lexem
	 * 
	 * @param tokens
	 *            all tokens
	 * @return parsed lexem
	 * @throws Exception
	 *             all throwable exceptions while parsing
	 */
	private IntegerLexem getIntegerLexem(String[] tokens) throws Exception {
		IStream<IToken> tokenStream = new TestTokenStream(tokens);
		IntegerLexemLogic logic = new IntegerLexemLogic(tokenStream);
		return logic.parse(new IntegerLexem());
	}

	/**
	 * Create expected range object
	 * 
	 * @param lowerBound
	 * @param upperBound
	 * @return range
	 */
	private Range getExpectedRange(String lowerBound, String upperBound) {
		Range expectedRange = new Range();
		expectedRange.setLowerBound(lowerBound);
		expectedRange.setUpperBound(upperBound);
		return expectedRange;
	}

	/**
	 * Contain basic assertions
	 * 
	 * @param lowerBound
	 * @param upperBound
	 * @param lexem
	 * @throws Exception
	 */
	private void rangeAssertion(String lowerBound, String upperBound,
			IntegerLexem lexem, boolean withConstants) throws Exception {

		Range expectedRange = getExpectedRange(lowerBound, upperBound);
		Range actualRange = lexem.getRange();

		assertNotNull("result cannot be null", lexem);
		assertEquals("unexpected lexem type", ClassDescriptionType.INTEGER,
				lexem.getType());
		assertEquals("unexpected Range LOWER BOUND",
				expectedRange.getLowerBound(), actualRange.getLowerBound());
		assertEquals("unexpected Range UPPER BOUND",
				expectedRange.getUpperBound(), actualRange.getUpperBound());

		if (!withConstants) {
			assertEquals("unexpected Size value", lexem.getRange()
					.computeRange(), lexem.getSize());
			assertEquals("unexpected computed Range size",
					expectedRange.computeRange(), actualRange.computeRange());
		}
	}

}
