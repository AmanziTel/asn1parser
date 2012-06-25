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

package org.amanzi.asn1.parser.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amanzi.asn1.parser.lexer.impl.ClassReference;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription;

/**
 * Description manager instance
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public final class DescriptionManager {

	private static final byte FIRST_ELEMENT_INDEX = 0;

	private static DescriptionManager managerInstance;
	private Map<String, List<ClassReference>> references;
	private ClassReference previousReference;

	private DescriptionManager() {
		references = new HashMap<String, List<ClassReference>>(0);
	}

	public static DescriptionManager getInstance() {
		if (managerInstance == null) {
			managerInstance = new DescriptionManager();
		}
		return managerInstance;
	}

	/**
	 * Add new reference for CLASS_DEFINITION_NAME
	 * 
	 * @param classDefinitionName
	 * @param reference
	 */
	public void putReference(String classDefinitionName,
			ClassReference reference) {
		List<ClassReference> currentReferences = references
				.remove(classDefinitionName);

		if (currentReferences == null) {
			currentReferences = new ArrayList<ClassReference>(0);
		}
		if (reference.getClassDescription() == null) {
			if (!currentReferences.isEmpty()) {
				IClassDescription classDescription = currentReferences.get(
						FIRST_ELEMENT_INDEX).getClassDescription();
				reference.setClassDescription(classDescription);
			}
		}

		currentReferences.add(reference);
		this.previousReference = reference;
		references.put(classDefinitionName, currentReferences);
	}

	/**
	 * Add new class definition
	 * 
	 * @param classDefinitionName
	 *            defined class name
	 * @param description
	 */
	public void putClassDefinition(String classDefinitionName,
			IClassDescription description) {
		List<ClassReference> currentReferences = references
				.remove(classDefinitionName);
		if (currentReferences == null) {
			currentReferences = new ArrayList<ClassReference>(0);
			ClassReference classReferenceWithDifinition = new ClassReference();
			classReferenceWithDifinition.setClassDescription(description);
			currentReferences.add(classReferenceWithDifinition);
		} else {
			for (ClassReference reference : currentReferences) {
				reference.setClassDescription(description);
			}
		}
		references.put(classDefinitionName, currentReferences);
	}

	/**
	 * Get references list for current CLASS_DEFINITION_NAME
	 * 
	 * @param classDefinitionName
	 * 
	 * @return list of references or null
	 */
	public List<ClassReference> getReferences(String classDefinitionName) {
		return references.get(classDefinitionName);
	}

	/**
	 * Get previous reference for update
	 * 
	 * @return previous added reference
	 */
	public ClassReference getPreviousReference() {
		return previousReference;
	}

	public void setPreviousReference(ClassReference reference) {
		this.previousReference = reference;
	}

	/**
	 * Update current reference for current class definition name
	 * 
	 * @param classDefinitionName
	 * @param reference
	 */
	public void updateReference(String classDefinitionName,
			ClassReference reference) {
		List<ClassReference> currentReferences = references
				.remove(classDefinitionName);
		if (currentReferences != null) {
			for (ClassReference currentReference : currentReferences) {
				if (reference.equals(currentReference)) {
					currentReference.setOptional(reference.isOptional());
				}
			}
		} else {
			ClassReference classReference = new ClassReference();
			classReference.setName(reference.getName());
			classReference.setClassDescription(reference.getClassDescription());
			classReference.setOptional(reference.isOptional());
			currentReferences = new ArrayList<ClassReference>(0);
			currentReferences.add(classReference);
		}

		references.put(classDefinitionName, currentReferences);
	}
}
