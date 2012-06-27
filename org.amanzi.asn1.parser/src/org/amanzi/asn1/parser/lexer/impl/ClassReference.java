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

/**
 * ClassReference for SEQUENCE, SEQUENCE OF and CHOICE definitions
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class ClassReference implements ILexem {

	private String name;
	private IClassDescription classDescription;
	private boolean optional;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IClassDescription getClassDescription() {
		return classDescription;
	}

	public void setClassDescription(IClassDescription classDescription) {
		this.classDescription = classDescription;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

}
