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
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.token.IToken;
import org.junit.Test;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class EnumeratedLogicTest {
    
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
    
    @Test(expected = SyntaxException.class)
    public void testUnexpectedFirstToken() throws Exception {
        IStream<IToken> tokenStream = new TestTokenStream("enum1", ",", "enum2", "}");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }
    
    @Test(expected = SyntaxException.class)
    public void testUnexpectedToken() throws Exception {
        IStream<IToken> tokenStream = new TestTokenStream("..");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }
    
    @Test(expected = SyntaxException.class)
    public void testUnexpectedReservedWord() throws Exception {
        IStream<IToken> tokenStream = new TestTokenStream("SEQUENCE");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }
    
    @Test(expected = SyntaxException.class)
    public void testNoComma() throws Exception {
        IStream<IToken> tokenStream = new TestTokenStream("enum1", "enum2", "}");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }
    
    @Test(expected = SyntaxException.class)
    public void testNoTrailingSymbol() throws Exception {
        IStream<IToken> tokenStream = new TestTokenStream("enum1", "enum2");
        
        EnumeratedLogic enumeratedLogic = new EnumeratedLogic(tokenStream);
        enumeratedLogic.parse(new Enumerated());
    }

}
