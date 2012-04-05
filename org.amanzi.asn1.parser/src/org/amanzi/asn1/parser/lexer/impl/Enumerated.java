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
import java.util.List;

/**
 * Enumerated Lexem 
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class Enumerated implements ILexem {

    private List<String> enumMembers = new ArrayList<>();
    
    public void addMember(String member) { 
        enumMembers.add(member);
    }
    
    /**
     * Returns List of Members enumeration
     *
     * @return
     */
    public List<String> getMembers() {
        return enumMembers;
    }
    
}
