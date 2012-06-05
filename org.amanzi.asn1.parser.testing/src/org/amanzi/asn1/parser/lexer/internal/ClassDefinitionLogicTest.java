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
import static org.junit.matchers.JUnitMatchers.*;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.lexer.impl.Sequence;
import org.amanzi.asn1.parser.token.IToken;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests on ClassDefinition Logic
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class ClassDefinitionLogicTest {
    
    @Rule
    public ExpectedException syntaxException = ExpectedException.none();
    
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
    
    private void prepareException(ErrorReason errorReason) {
        syntaxException.expect(SyntaxException.class);
        syntaxException.expectMessage(containsString(errorReason.getMessage()));
    }
    
    private ClassDefinition run(IStream<IToken> tokenStream) throws SyntaxException {
        ClassDefinitionLogic logic = new ClassDefinitionLogic(tokenStream);
        
        return logic.parse(new ClassDefinition());
    }
    
    @Test
    public void testCheckCorrectClassName() throws Exception {
        IStream<IToken> tokenStream = getEnumaredTokenStream("ClassDefinition");
        
        ClassDefinition result = run(tokenStream);
        
        assertEquals("unexpected name of Class Definition", result.getClassName(), "ClassDefinition");
    }
    
    @Test
    public void testCheckDescriptionNotNull() throws Exception {
        IStream<IToken> tokenStream = getEnumaredTokenStream("ClassDefinition");
        
        ClassDefinition result = run(tokenStream);
        
        assertNotNull("ClassDescription should exist", result.getClassDescription());
    }
    
    @Test
    public void testCheckCorrectEnumeratedDescriptionType() throws Exception {
        IStream<IToken> tokenStream = getEnumaredTokenStream("ClassDefinition");
        
        ClassDefinition result = run(tokenStream);
        
        assertEquals("unexpected type of Class Description", result.getClassDescription().getType(), ClassDescriptionType.ENUMERATED);
        assertEquals("unexpected class of Class Description", result.getClassDescription(), Enumerated.class);
    }
    
    @Test
    public void testCheckCorrectSequenceDescriptionType() throws Exception {
        IStream<IToken> tokenStream = getSequenceTokenStream("ClassDefinition");
        
        ClassDefinition result = run(tokenStream);
        
        assertEquals("unexpected type of Class Description", result.getClassDescription().getType(), ClassDescriptionType.SEQUENCE);
        assertEquals("unexpected class of Class Description", result.getClassDescription(), Sequence.class);
    }
    
    @Test
    public void testCheckCorrectDescriptionType() throws Exception {
        for (ClassDescriptionType descriptionType : ClassDescriptionType.values()) {
            IStream<IToken> tokenStream = null;
            Class<? extends IClassDescription> descriptionClass = null;
            
            String definitionName = "ClassDefinition";
            
            switch (descriptionType) {
            case ENUMERATED:
                descriptionClass = Enumerated.class;
                tokenStream = getEnumaredTokenStream(definitionName);
                break;
            case SEQUENCE:
                descriptionClass = Sequence.class;
                tokenStream = getSequenceTokenStream(definitionName);
                break;
            }
            
            ClassDefinition definition = run(tokenStream);
            
            assertNotNull("class description should not be null", definition.getClassDescription());
            assertEquals("unexpected Class Description type", descriptionType, definition.getClassDescription().getType());
            assertEquals("unexpected Class Description class", descriptionClass, definition.getClassDescription().getClass());
        }
    }
    
    @Test
    public void testCheckUnexpectedEndOfStreamException() throws Exception {
        prepareException(ErrorReason.UNEXPECTED_END_OF_STREAM);
        
        IStream<IToken> tokenStream = new TestTokenStream("WrongClass", "::=");
        
        run(tokenStream);
    }
    
    @Test
    public void testCheckNotSupportedTokenException() throws Exception {
        prepareException(ErrorReason.TOKEN_NOT_SUPPORTED);
        
        IStream<IToken> tokenStream = new TestTokenStream(",", "::=");
        
        run(tokenStream);
    }
    
    @Test
    public void testCheckUnexpectedTokenInLexem() throws Exception {
        prepareException(ErrorReason.UNEXPECTED_TOKEN_IN_LEXEM);
        
        IStream<IToken> tokenStream = new TestTokenStream("ClassDefinitionName", "SEQUENCE");
        
        run(tokenStream);
    }
}
