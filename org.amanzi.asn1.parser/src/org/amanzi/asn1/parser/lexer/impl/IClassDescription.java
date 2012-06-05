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

import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ReservedWord;

/**
 * Interface that represents element that can be a Definition
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public interface IClassDescription extends ILexem {

	/**
	 * Enum with all possible types of Class Descriptions
	 * 
	 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
	 */
    public enum ClassDescriptionType {
        ENUMERATED(ReservedWord.ENUMERATED),
        SEQUENCE(ReservedWord.SEQUENCE),
        CHOISE(ReservedWord.CHOISE),
        INTEGER(ReservedWord.INTEGER),
        SEQUENCE_OF(ReservedWord.SEQUENCE),
        BIT_STRING(ReservedWord.BIT_STRING),
        OCTET_STRING(ReservedWord.OCTET_STRING);
        
        private IToken token;
        
        private ClassDescriptionType(IToken token) {
        	this.token = token;
        }
        
        public IToken getToken() {
        	return token;
        }
        
    }
    
    public ClassDescriptionType getType();
    
}
