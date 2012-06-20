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
 * Choice lexem
 * 
 * @author bondoronok_p
 * @since 1.0.0
 */
public class ChoiceLexem implements IClassDescription {

	private Set<ClassReference> choiceMembers = new HashSet<ClassReference>();

	/**
	 * Add new member to a members set
	 * 
	 * @param reference
	 *            {@link ClassReference}
	 */
	public void putMember(ClassReference reference) {
		choiceMembers.add(reference);
	}

	/**
	 * Get all {@link ChoiceLexem} members
	 * 
	 * @return {@link ChoiceLexem} members
	 */
	public Set<ClassReference> getMembers() {
		return choiceMembers;
	}

	@Override
	public ClassDescriptionType getType() {
		return ClassDescriptionType.CHOICE;
	}

}
