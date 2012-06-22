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
 * Definition of any constant
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class ConstantDefinition implements ILexem {

	private String name;
	private IClassDescription description;

	/**
	 * Constant definition name
	 * 
	 * @return {@link String} name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set constant definition name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Constant description
	 * 
	 * @return {@link IClassDescription}
	 */
	public IClassDescription getDescription() {
		return description;
	}

	/**
	 * Set constant {@link IClassDescription} value
	 * 
	 * @param description
	 */
	public void setDescription(IClassDescription description) {
		this.description = description;
	}

}
