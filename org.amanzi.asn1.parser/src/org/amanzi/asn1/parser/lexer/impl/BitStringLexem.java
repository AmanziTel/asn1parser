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
 * Lexem for BIT STRING definition
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class BitStringLexem extends AbstractStringDefinition {
	
	@Override
	public ClassDescriptionType getType() {
		return ClassDescriptionType.BIT_STRING;
	}
}
