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

import java.util.Arrays;
import java.util.Iterator;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.SimpleToken;

/**
 * TokenStream implementation for tests
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class TestTokenStream implements IStream<IToken> {
    
    private Iterator<String> tokenIterator;
    
    public TestTokenStream(String... tokens) {
        this.tokenIterator = Arrays.asList(tokens).iterator();
    }

    @Override
    public boolean hasNext() {
        return tokenIterator.hasNext();
    }

    @Override
    public IToken next() {
        return new SimpleToken(tokenIterator.next());
    }

    @Override
    public void remove() {
    }

}
