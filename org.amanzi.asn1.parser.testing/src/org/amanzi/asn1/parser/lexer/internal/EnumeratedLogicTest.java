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
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.token.IToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests on EnumeratedLogic Tests
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class EnumeratedLogicTest {
    
    @Rule
    public ExpectedException syntaxException = ExpectedException.none();
    
    @Test
    public void testCheckResult() throws Exception {
        IStream<IToken> tokenStream = new TestTokenStream("{", "enum1", ",", "enum2", "}");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        Enumerated result = enumeratedLogic.parse(new Enumerated());
        
        assertNotNull("Result of Processing cannot be null", result);
        assertEquals("Unexpected size of Enums", 2, result.getMembers().size());
        
        assertEquals("unexpected first member of enum", "enum1", result.getMembers().get(0));
        assertEquals("unexpected first member of enum", "enum2", result.getMembers().get(1));
    }
    
    @Test
    public void testUnexpectedFirstToken() throws Exception {
        syntaxException.expect(SyntaxException.class);
        syntaxException.expectMessage(containsString(ErrorReason.NO_START_TOKEN.getMessage()));
        
        IStream<IToken> tokenStream = new TestTokenStream("enum1", ",", "enum2", "}");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }
    
    @Test
    public void testUnexpectedToken() throws Exception {
        syntaxException.expect(SyntaxException.class);
        syntaxException.expectMessage(containsString(ErrorReason.TOKEN_NOT_SUPPORTED.getMessage()));
        
        IStream<IToken> tokenStream = new TestTokenStream("{", "..");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }
    
    @Test
    public void testNoComma() throws Exception {
        syntaxException.expect(SyntaxException.class);
        syntaxException.expectMessage(containsString(ErrorReason.NO_SEPARATOR.getMessage()));
        
        IStream<IToken> tokenStream = new TestTokenStream("{", "enum1", "enum2", "}");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }
    
    @Test
    public void testNoTrailingSymbol() throws Exception {
        syntaxException.expect(SyntaxException.class);
        syntaxException.expectMessage(containsString(ErrorReason.UNEXPECTED_END_OF_STREAM.getMessage()));
        
        IStream<IToken> tokenStream = new TestTokenStream("{", "enum1", ",", "enum2");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }
    
    @Test
    public void testUnexpectedEndOfLexem() throws Exception {
        syntaxException.expect(SyntaxException.class);
        syntaxException.expectMessage(containsString(ErrorReason.UNEXPECTED_END_OF_LEXEM.getMessage()));
        
        IStream<IToken> tokenStream = new TestTokenStream("{", "enum1", ",", "}");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }

}
