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
 * Lexem for constants definitions;
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class ConstantFileLexem implements ILexem {

	private Map<String, IClassDescription> constants;
	private String fileName;

	/**
	 * {@link ConstantFileLexem} constructor
	 */
	public ConstantFileLexem() {
		constants = new HashMap<String, IClassDescription>(0);
	}

	/**
	 * Add parsed constant
	 * 
	 * @param definition parsed class definition
	 */
	public void addConstant(ClassDefinition definition) {
		constants.put(definition.getClassName(),
				definition.getClassDescription());
	}

	/**
	 * Get all parsed constants from {@link ConstantFileLexem}
	 * 
	 * @return constants of {@link ConstantFileLexem}
	 */
	public Map<String, IClassDescription> getConstants() {
		return constants;
	}

	/**
	 * 
	 * @return {@link ConstantFileLexem} name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 
	 * @param fileName
	 *            {@link ConstantFileLexem} name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
