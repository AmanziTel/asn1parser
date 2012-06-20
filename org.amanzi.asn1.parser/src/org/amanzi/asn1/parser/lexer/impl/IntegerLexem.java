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

package org.amanzi.asn1.parser.lexer.impl;

import org.amanzi.asn1.parser.lexer.ranges.impl.Range;

/**
 * Integer Lexem
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class IntegerLexem extends AbstractValueDefinition<Integer> {

	private Range range;

	private int size;

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public ClassDescriptionType getType() {
		return ClassDescriptionType.INTEGER;
	}

}
