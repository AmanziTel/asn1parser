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
 * SEQUENCE OF definition
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class SequenceOfLexem extends AbstractSequenceLexem {

	/**
	 * SequenceOf size value
	 */
	private Size size;

	/**
	 * We need used {@link ClassReference}, because in some cases we may take
	 * {@link ClassDefinition} or name {@link ClassDefinition}
	 */
	private ClassReference classReference;

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public ClassReference getClassReference() {
		return classReference;
	}

	public void setClassReference(ClassReference classReference) {
		this.classReference = classReference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.amanzi.asn1.parser.lexer.impl.IClassDescription#getType()
	 */
	@Override
	public ClassDescriptionType getType() {
		return ClassDescriptionType.SEQUENCE_OF;
	}

}
