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

import java.util.HashSet;
import java.util.Set;

/**
 * SEQUENCE lexem
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class SequenceLexem extends AbstractSequenceLexem {

	private Set<ClassReference> sequenceMembers = new HashSet<ClassReference>(0);

	/**
	 * Add new SEQUENCE member
	 * 
	 * @param name
	 *            class definition name
	 * @param classReference
	 *            class reference
	 */
	public void addMember(ClassReference classReference) {
		sequenceMembers.add(classReference);
	}

	/**
	 * @return SEQUENCE members
	 */
	public Set<ClassReference> getMembers() {
		return sequenceMembers;
	}

	@Override
	public ClassDescriptionType getType() {
		return ClassDescriptionType.SEQUENCE;
	}
}
