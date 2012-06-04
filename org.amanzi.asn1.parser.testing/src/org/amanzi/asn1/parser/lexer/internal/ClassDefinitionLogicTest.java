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

package org.amanzi.asn1.parser.lexer.internal;

import static org.junit.Assert.*;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.Sequence;
import org.amanzi.asn1.parser.token.IToken;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class ClassDefinitionLogicTest {
    
    private static final String[] ENUMERATED = new String[] { "{", "enum1", ",", "enum2", "}" };
    
    private static final String[] SEQUENCE = new String[] {"{", "class", "Class", "class2", "Class2", "}"};
    
    private IStream<IToken> getSequenceTokenStream(String ... firstElements) {
        return getTokenStream(ArrayUtils.add(firstElements, "::="), SEQUENCE);
    }
    
    private IStream<IToken> getEnumaredTokenStream(String ... firstElements) {
        return getTokenStream(ArrayUtils.add(firstElements, "::="), ENUMERATED);
    }
    
    private IStream<IToken> getTokenStream(String[] firstElements, String... lastElements) {
        return new TestTokenStream(ArrayUtils.addAll(firstElements, lastElements));
    }

    @Test
    public void testCheckCorrectClassName() throws Exception {
        IStream<IToken> tokenStream = getEnumaredTokenStream("ClassDefinition");
        
        ClassDefinitionLogic logic = new ClassDefinitionLogic(tokenStream);
        ClassDefinition result = logic.parse(new ClassDefinition());
        
        assertEquals("unexpected name of Class Definition", result.getClassName(), "ClassDefinition");
    }
    
    @Test
    public void testCheckDescriptionNotNull() throws Exception {
        IStream<IToken> tokenStream = getEnumaredTokenStream("ClassDefinition");
        
        ClassDefinitionLogic logic = new ClassDefinitionLogic(tokenStream);
        ClassDefinition result = logic.parse(new ClassDefinition());
        
        assertNotNull("ClassDescription should exist", result.getClassDescription());
    }
    
    @Test
    public void testCheckCorrectEnumeratedDescriptionType() throws Exception {
        IStream<IToken> tokenStream = getEnumaredTokenStream("ClassDefinition");
        
        ClassDefinitionLogic logic = new ClassDefinitionLogic(tokenStream);
        ClassDefinition result = logic.parse(new ClassDefinition());
        
        assertEquals("unexpected type of Class Description", result.getClassDescription().getType(), ClassDescriptionType.ENUMERATED);
        assertEquals("unexpected class of Class Description", result.getClassDescription(), Enumerated.class);
    }
    
    @Test
    public void testCheckCorrectSequenceDescriptionType() throws Exception {
        IStream<IToken> tokenStream = getSequenceTokenStream("ClassDefinition");
        
        ClassDefinitionLogic logic = new ClassDefinitionLogic(tokenStream);
        ClassDefinition result = logic.parse(new ClassDefinition());
        
        assertEquals("unexpected type of Class Description", result.getClassDescription().getType(), ClassDescriptionType.SEQUENCE);
        assertEquals("unexpected class of Class Description", result.getClassDescription(), Sequence.class);
    }

}
