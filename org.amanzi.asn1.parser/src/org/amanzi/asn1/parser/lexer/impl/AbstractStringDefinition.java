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

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that describes definition of string Value
 * 
 * Describe general parts for BitString and OctetString statements
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class AbstractStringDefinition extends AbstractValueDefinition<String> {

	private Size stringSize;
	private Map<Byte, String> stringMembers = new HashMap<Byte, String>();
	private Byte bitNumber = 0;
	private ClassReference containingValue;

	/**
	 * Add new Member to a {@link BitStringLexem} or {@link OctetStringLexem}
	 * members
	 * 
	 * @param bitNumber
	 *            member position.
	 * @param name
	 *            name of current member
	 */
	public void putMember(Byte bitNumber, String name) {
		stringMembers.put(bitNumber, name);
	}

	/**
	 * Get all {@link BitStringLexem} or {@link OctetStringLexem} members
	 * 
	 * @return {@link BitStringLexem} or {@link OctetStringLexem} members
	 */
	public Map<Byte, String> getMembers() {
		return stringMembers;
	}

	/**
	 * Size of {@link BitStringLexem} or {@link OctetStringLexem}
	 * 
	 * @return size {@link Size}
	 */
	public Size getSize() {
		return stringSize;
	}

	/**
	 * Set size of {@link BitStringLexem} or {@link OctetStringLexem}
	 * 
	 * @param size
	 *            {@link Size}
	 */
	public void setSize(Size size) {
		this.stringSize = size;
	}

	@Override
	public ClassDescriptionType getType() {
		return null;
	}

	/**
	 * Increase current bit value;
	 * 
	 * @return position of current Bit
	 */
	public Byte increaseBitNumber() {
		return bitNumber++;
	}

	/**
	 * Get CONTAINING value
	 * 
	 * @return {@link ClassReference}
	 */
	public ClassReference getContainingValue() {
		return containingValue;
	}

	/**
	 * Add CONTAINING Value
	 * 
	 * @param containingValue
	 *            {@link ClassReference}
	 */
	public void setContainingValue(ClassReference containingValue) {
		this.containingValue = containingValue;
	}

}
