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

package org.amanzi.asn1.parser.lexer.ranges.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class RangeTest {

	private static final String CONSTANT = "constant";
	private static final String CONSTANT_1 = "constant1";
	private static final String CONSTANT_2 = "constant2";
	private static final String UNEXPECTED_COMPUTING = "unexpected result of computing";
	private static final int LOWER_VALUE = 20;
	private static final int UPPER_VALUE = 30;
	private static final int COMPUTED_RANGE = 11;
	private static final int RANGE_BETWEEN_SAME_VALUES = 1;

	@Rule
	public ExpectedException syntaxException = ExpectedException.none();

	@Before
	public void setUp() {
		RangeStorage.getStorage().getLowerBoundCache().clear();
		RangeStorage.getStorage().getUpperBoundCache().clear();
	}

	@Test
	public void checkComputeRange() throws SyntaxException {
		Range range = new Range();
		range.setLowerBound("15");
		range.setUpperBound("25");

		assertEquals(UNEXPECTED_COMPUTING, COMPUTED_RANGE, range.computeRange());
	}

	@Test
	public void checkLowerBoundConstant() throws SyntaxException {
		Range range = new Range();
		range.setLowerBound(CONSTANT);
		range.setUpperBound("30");

		range.setLowerBoundValue(LOWER_VALUE);

		assertEquals(UNEXPECTED_COMPUTING, COMPUTED_RANGE, range.computeRange());
	}

	@Test
	public void testCheckUpperBoundConstant() throws SyntaxException {
		Range range = new Range();
		range.setLowerBound("20");
		range.setUpperBound(CONSTANT);

		range.setUpperBoundValue(UPPER_VALUE);

		assertEquals(UNEXPECTED_COMPUTING, COMPUTED_RANGE, range.computeRange());
	}

	@Test
	public void testCheckBothBoundsConstant() throws SyntaxException {
		Range range = new Range();
		range.setLowerBound(CONSTANT_1);
		range.setUpperBound(CONSTANT_2);

		range.setLowerBoundValue(UPPER_VALUE);
		range.setUpperBoundValue(UPPER_VALUE);

		assertEquals(UNEXPECTED_COMPUTING, RANGE_BETWEEN_SAME_VALUES,
				range.computeRange());
	}

	@Test
	public void testCheckIncorrectBoundException() throws SyntaxException {
		syntaxException.expect(SyntaxException.class);
		syntaxException
				.expectMessage(containsString(ErrorReason.INCORRECT_RANGE_BOUND
						.getMessage()));

		Range range = new Range();
		range.setUpperBoundValue(LOWER_VALUE);
		range.setLowerBoundValue(UPPER_VALUE);

		range.computeRange();
	}

	@Test
	public void testConstantNotFoundException() throws SyntaxException {
		syntaxException.expect(SyntaxException.class);
		syntaxException
				.expectMessage(containsString(ErrorReason.CONSTANT_NOT_FOUND
						.getMessage()));

		Range range = new Range();
		range.setUpperBound(CONSTANT);
		range.setLowerBoundValue(LOWER_VALUE);

		range.computeRange();
	}

	@Test
	public void testRangeRegistrationInStorage() {
		Range range1 = new Range();
		range1.setUpperBound(CONSTANT_1);

		Range range2 = new Range();
		range2.setLowerBound(CONSTANT_2);

		assertEquals("Unexpected size of lower bound storage",
				RANGE_BETWEEN_SAME_VALUES, RangeStorage.getStorage()
						.getLowerBoundCache().size());
		assertEquals("Unexpected size of upper bound storage",
				RANGE_BETWEEN_SAME_VALUES, RangeStorage.getStorage()
						.getUpperBoundCache().size());

		assertTrue("Upper bound should contain constant", RangeStorage
				.getStorage().getUpperBoundCache().containsKey(CONSTANT_1));
		assertTrue("Lower bound should contain constant", RangeStorage
				.getStorage().getLowerBoundCache().containsKey(CONSTANT_2));

		assertEquals("Unexpected size or Ranges for upper bound constant",
				RANGE_BETWEEN_SAME_VALUES, RangeStorage.getStorage()
						.getUpperBoundCache().get(CONSTANT_1).size());
		assertEquals("Unexpected size or Ranges for lower bound constant",
				RANGE_BETWEEN_SAME_VALUES, RangeStorage.getStorage()
						.getLowerBoundCache().get(CONSTANT_2).size());

		assertTrue("Unexpected range in upper bound ranges",
				RangeStorage.getStorage().getUpperBoundCache().get(CONSTANT_1)
						.contains(range1));
		assertTrue("Unexpected range in lower bound ranges",
				RangeStorage.getStorage().getLowerBoundCache().get(CONSTANT_2)
						.contains(range2));
	}
}
