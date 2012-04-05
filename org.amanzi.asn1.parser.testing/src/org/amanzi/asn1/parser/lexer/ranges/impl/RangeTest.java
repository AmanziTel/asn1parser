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
    
    @Rule
    public ExpectedException syntaxException = ExpectedException.none();
    
    @Before
    public void setUp() {
        RangeStorage.getStorage().lowerBoundCache.clear();
        RangeStorage.getStorage().upperBoundCache.clear();
    }

    @Test
    public void checkComputeRange() throws SyntaxException {
        Range range = new Range();
        range.setLowerBound("15");
        range.setUpperBound("25");
        
        assertEquals("unexpected result of computing", 11, range.computeRange());
    }
    
    @Test
    public void checkLowerBoundConstant() throws SyntaxException {
        Range range = new Range();
        range.setLowerBound("constant");
        range.setUpperBound("30");
        
        range.setLowerBoundValue(20);
        
        assertEquals("unexpected result of computing", 11, range.computeRange());
    }
    
    
    @Test
    public void testCheckUpperBoundConstant() throws SyntaxException {
        Range range = new Range();
        range.setLowerBound("20");
        range.setUpperBound("constant");
        
        range.setUpperBoundValue(30);
        
        assertEquals("unexpected result of computing", 11, range.computeRange());
    }
    
    @Test
    public void testCheckBothBoundsConstant() throws SyntaxException {
        Range range = new Range();
        range.setLowerBound("constant1");
        range.setUpperBound("constant2");
        
        range.setLowerBoundValue(30);
        range.setUpperBoundValue(30);
        
        assertEquals("unexpected result of computing", 1, range.computeRange());
    }

    @Test
    public void testCheckIncorrectBoundException() throws SyntaxException {
        syntaxException.expect(SyntaxException.class);
        syntaxException.expectMessage(containsString(ErrorReason.INCORRECT_RANGE_BOUND.getMessage()));
        
        Range range = new Range();
        range.setUpperBoundValue(5);
        range.setLowerBoundValue(20);
        
        range.computeRange();
    }
    
    @Test
    public void testConstantNotFoundException() throws SyntaxException {
        syntaxException.expect(SyntaxException.class);
        syntaxException.expectMessage(containsString(ErrorReason.CONSTANT_NOT_FOUND.getMessage()));
        
        Range range = new Range();
        range.setUpperBound("constant");
        range.setLowerBoundValue(10);
        
        range.computeRange();
    }
    
    @Test
    public void testRangeRegistrationInStorage() {
        Range range1 = new Range();
        range1.setUpperBound("constant1");
        
        Range range2 = new Range();
        range2.setLowerBound("constant2");
        
        assertEquals("Unexpected size of lower bound storage", 1, RangeStorage.getStorage().lowerBoundCache.size());
        assertEquals("Unexpected size of upper bound storage", 1, RangeStorage.getStorage().upperBoundCache.size());
        
        assertTrue("Upper bound should contain constant", RangeStorage.getStorage().upperBoundCache.containsKey("constant1"));
        assertTrue("Lower bound should contain constant", RangeStorage.getStorage().lowerBoundCache.containsKey("constant2"));
        
        assertEquals("Unexpected size or Ranges for upper bound constant", 1, RangeStorage.getStorage().upperBoundCache.get("constant1").size());
        assertEquals("Unexpected size or Ranges for lower bound constant", 1, RangeStorage.getStorage().lowerBoundCache.get("constant2").size());
        
        assertTrue("Unexpected range in upper bound ranges", RangeStorage.getStorage().upperBoundCache.get("constant1").contains(range1));
        assertTrue("Unexpected range in lower bound ranges", RangeStorage.getStorage().lowerBoundCache.get("constant2").contains(range2));
    }
}
