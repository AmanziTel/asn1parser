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
 * Describes {@link FileLexem} and {@link ConstantFileLexem}
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public abstract class AbstractFileLexem implements ILexem {

	private String name;

	/**
	 * File name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set file name
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
