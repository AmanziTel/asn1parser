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

import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;

/**
 * Class that represents a Range
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class Range {

	private static final String DEFAULT_BOUND_VALUE = "1";

	private String upperBound;

	private String lowerBound;

	private Integer upperBoundValue;

	private Integer lowerBoundValue;

	public Range() {
	}

	public Range(String upperBound) {
		this.lowerBound = DEFAULT_BOUND_VALUE;
		this.upperBound = upperBound;
		setLowerBound(lowerBound);
		setUpperBound(upperBound);
	}

	public void addBound(String bound) {
		if (lowerBound == null) {
			setLowerBound(bound);
		} else {
			setUpperBound(bound);
		}
	}

	public final void setUpperBound(String upperBound) {
		this.upperBound = upperBound;

		try {
			upperBoundValue = Integer.parseInt(upperBound);
		} catch (NumberFormatException e) {
			RangeStorage.getStorage().addUpperBoundRange(this);
		}
	}

	public final void setLowerBound(String lowerBound) {
		this.lowerBound = lowerBound;

		try {
			lowerBoundValue = Integer.parseInt(lowerBound);
		} catch (NumberFormatException e) {
			RangeStorage.getStorage().addLowerBoundRange(this);
		}
	}

	public String getLowerBound() {
		return lowerBound;
	}

	public String getUpperBound() {
		return upperBound;
	}

	public Integer getLowerBoundValue() {
		return lowerBoundValue;
	}

	public Integer getUpperBoundValue() {
		return upperBoundValue;
	}

	void setUpperBoundValue(int value) {
		upperBoundValue = value;
	}

	void setLowerBoundValue(int value) {
		lowerBoundValue = value;
	}

	public int computeRange() throws SyntaxException {
		if (lowerBoundValue == null) {
			throw new SyntaxException(ErrorReason.CONSTANT_NOT_FOUND,
					"Constant <" + lowerBound + "> was not processed");
		}

		if (upperBoundValue == null) {
			throw new SyntaxException(ErrorReason.CONSTANT_NOT_FOUND,
					"Constant <" + upperBound + "> was not processed");
		}

		if (upperBoundValue < lowerBoundValue) {
			throw new SyntaxException(ErrorReason.INCORRECT_RANGE_BOUND,
					"Lower bound of Range cannot be more than Upper bound");
		}

		return upperBoundValue - lowerBoundValue + 1;
	}

}
