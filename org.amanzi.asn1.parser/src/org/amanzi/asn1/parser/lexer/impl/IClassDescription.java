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
 * Interface that represents element that can be a Definition
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public interface IClassDescription extends ILexem {

    public enum ClassDescriptionType {
        ENUMERATED,
        SEQUENCE,
        CHOISE,
        INTEGER,
        SEQUENCE_OF,
        BIT_STRING,
        OCTET_STRING;
    }
    
    public ClassDescriptionType getType();
    
}
