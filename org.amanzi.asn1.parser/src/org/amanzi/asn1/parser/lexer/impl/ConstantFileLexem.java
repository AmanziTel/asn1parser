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
 * Lexem for constants definitions;
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class ConstantFileLexem implements ILexem {

	private Set<ConstantDefinition> constants;
	private String name;

	/**
	 * {@link ConstantFileLexem} constructor
	 */
	public ConstantFileLexem() {
		this.constants = new HashSet<ConstantDefinition>();
	}

	/**
	 * Add parsed constant
	 * 
	 * @param definition
	 *            parsed constant definition
	 */
	public void addConstant(ConstantDefinition definition) {
		constants.add(definition);
	}

	/**
	 * Get all parsed constants from {@link ConstantFileLexem}
	 * 
	 * @return constants of {@link ConstantFileLexem}
	 */
	public Set<ConstantDefinition> getConstants() {
		return constants;
	}

	/**
	 * 
	 * @return {@link ConstantFileLexem} name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param fileName
	 *            {@link ConstantFileLexem} name
	 */
	public void setName(String fileName) {
		this.name = fileName;
	}
}
