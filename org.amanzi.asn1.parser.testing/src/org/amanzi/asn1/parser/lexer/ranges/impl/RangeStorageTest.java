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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class RangeStorageTest {

	@Before
	public void setUp() {
		RangeStorage.getStorage().getLowerBoundCache().clear();
		RangeStorage.getStorage().getUpperBoundCache().clear();
	}

	@Test
	public void testAddLowerBoundRange() {
		Range range = new Range();
		range.setLowerBound("constant");

		assertTrue("Upper bound cache should be empty", RangeStorage
				.getStorage().getUpperBoundCache().isEmpty());
		assertFalse("Lower bound cache should not be empty", RangeStorage
				.getStorage().getLowerBoundCache().isEmpty());

		assertEquals("Unexpected size of bound cache", 1, RangeStorage
				.getStorage().getLowerBoundCache().size());
		assertNotNull("List of Ranges cannot be null", RangeStorage
				.getStorage().getLowerBoundCache().get("constant"));
		assertEquals("Unexpected size of List of Ranges", 1, RangeStorage
				.getStorage().getLowerBoundCache().get("constant").size());
		assertTrue("List of Ranges should contain range",
				RangeStorage.getStorage().getLowerBoundCache().get("constant")
						.contains(range));
	}

	@Test
	public void testAddUpperBoundRange() {
		Range range = new Range();
		range.setUpperBound("constant");

		assertTrue("Lower bound cache should be empty", RangeStorage
				.getStorage().getLowerBoundCache().isEmpty());
		assertFalse("upper bound cache should not be empty", RangeStorage
				.getStorage().getUpperBoundCache().isEmpty());

		assertEquals("Unexpected size of bound cache", 1, RangeStorage
				.getStorage().getUpperBoundCache().size());
		assertNotNull("List of Ranges cannot be null", RangeStorage
				.getStorage().getUpperBoundCache().get("constant"));
		assertEquals("Unexpected size of List of Ranges", 1, RangeStorage
				.getStorage().getUpperBoundCache().get("constant").size());
		assertTrue("List of Ranges should contain range",
				RangeStorage.getStorage().getUpperBoundCache().get("constant")
						.contains(range));
	}

	@Test
	public void checkLowerBoundRangesProcessing() {
		String[] lowerBoundsConstants = new String[] { "constant1",
				"constant2", "constant3" };
		Range[] lowerBoundRanges = new Range[lowerBoundsConstants.length];

		int i = 0;
		for (String bound : lowerBoundsConstants) {
			Range range = new Range();
			range.setLowerBound(bound);

			lowerBoundRanges[i++] = range;
		}

		for (i = 0; i < lowerBoundsConstants.length; i++) {
			RangeStorage.getStorage().processConstant(lowerBoundsConstants[i],
					i);
		}

		assertTrue("Storage should be clean after all processing", RangeStorage
				.getStorage().getLowerBoundCache().isEmpty());

		for (i = 0; i < lowerBoundRanges.length; i++) {
			assertEquals("Unexpected lower value of Range", Integer.valueOf(i),
					lowerBoundRanges[i].getLowerBoundValue());
		}
	}

	@Test
	public void checkUpperBoundRangesProcessing() {
		String[] upperBoundsConstants = new String[] { "constant1",
				"constant2", "constant3" };
		Range[] upperBoundRanges = new Range[upperBoundsConstants.length];

		int i = 0;
		for (String bound : upperBoundsConstants) {
			Range range = new Range();
			range.setUpperBound(bound);

			upperBoundRanges[i++] = range;
		}

		for (i = 0; i < upperBoundsConstants.length; i++) {
			RangeStorage.getStorage().processConstant(upperBoundsConstants[i],
					i);
		}

		assertTrue("Storage should be clean after all processing", RangeStorage
				.getStorage().getUpperBoundCache().isEmpty());

		for (i = 0; i < upperBoundRanges.length; i++) {
			assertEquals("Unexpected lower value of Range", Integer.valueOf(i),
					upperBoundRanges[i].getUpperBoundValue());
		}
	}

}
