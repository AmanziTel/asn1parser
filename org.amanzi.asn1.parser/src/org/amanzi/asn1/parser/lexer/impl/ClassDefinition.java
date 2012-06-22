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
 * Definition of any ASN.1 Instance
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class ClassDefinition implements ILexem {

	private String className;
	private ClassReference classReference;
	private IClassDescription description;

	public void setClassName(String className) {
		this.className = className;
	}

	public void setClassDescription(IClassDescription classDescription) {
		this.description = classDescription;
	}

	public String getClassName() {
		return className;
	}

	public IClassDescription getClassDescription() {
		return description;
	}

	public ClassReference getClassReference() {
		return classReference;
	}

	public void setClassReference(ClassReference classReference) {
		this.classReference = classReference;
	}

}
