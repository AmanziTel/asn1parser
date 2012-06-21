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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.amanzi.asn1.parser.utils.DescriptionManager;

/**
 * ASN.1 File lexem
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class FileLexem implements ILexem {

	private Map<String, Set<ClassReference>> imports;
	private List<ClassDefinition> fileClassDefinitions;
	private String name;
	private DescriptionManager descriptionManager;

	/**
	 * {@link FileLexem} constructor
	 */
	public FileLexem() {
		imports = new HashMap<String, Set<ClassReference>>(0);
		fileClassDefinitions = new ArrayList<ClassDefinition>(0);
		descriptionManager = DescriptionManager.getInstance();
	}

	/**
	 * Add imports list from file
	 * 
	 * @param file
	 *            {@link FileLexem} name
	 * @param classDefinitions
	 *            class definitions from file
	 */
	public void addImports(String fileName, Set<String> classDefinitions) {
		ClassReference reference;
		Set<ClassReference> classReferences = new HashSet<ClassReference>();
		for (String name : classDefinitions) {
			reference = new ClassReference();
			reference.setName(name);
			descriptionManager.putReference(name, reference);
			classReferences.add(reference);
		}
		imports.put(fileName, classReferences);
	}
	
	/**
	 * Returned IMPORTS classes
	 * 
	 * @return Imports map for {@link FileLexem}
	 */
	public Map<String, Set<ClassReference>> getImports() {
		return imports;
	}

	/**
	 * Returned list of class definitions
	 * @return {@link FileLexem} Class definitions
	 */
	public List<ClassDefinition> getFileClassDefinitions() {
		return fileClassDefinitions;
	}

	/**
	 * Add parsed class definition
	 * 
	 * @param classDefinition
	 *            parsed class definition
	 */
	public void addClassDefinition(ClassDefinition classDefinition) {
		fileClassDefinitions.add(classDefinition);
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