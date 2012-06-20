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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ASN.1 File lexem
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class FileLexem implements ILexem {

	private Map<FileLexem, Set<String>> imports;	
	private List<String> fileClassDefinitions;
	private String name;

	/**
	 * {@link FileLexem} constructor
	 */
	public FileLexem() {
		imports = new HashMap<FileLexem, Set<String>>(0);		
		fileClassDefinitions = new ArrayList<String>(0);
	}

	/**
	 * Add imports list From file
	 * 
	 * @param file
	 *            {@link FileLexem}
	 * @param classDefinitions
	 *            class definitions from file
	 */
	public void addImportsFromFile(FileLexem file, Set<String> classDefinitions) {
		imports.put(file, classDefinitions);
	}

	/**
	 * Add parsed class definition name
	 * 
	 * @param classDefinitionName
	 *            parsed class definition name
	 */
	public void addClassDefinitionName(String classDefinitionName) {
		fileClassDefinitions.add(classDefinitionName);
	}

	/**
	 * Get current ASN.1 file name
	 * 
	 * @return {@link FileLexem} name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set ASN.1 file name
	 * 
	 * @param name
	 *            ASN.1 file name
	 */
	public void setName(String name) {
		this.name = name;
	}

}