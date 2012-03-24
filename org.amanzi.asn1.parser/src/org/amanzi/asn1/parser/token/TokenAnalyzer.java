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

package org.amanzi.asn1.parser.token;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.amanzi.asn1.parser.AbstractStream;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class TokenAnalyzer extends AbstractStream<IToken>{
    
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 100;
    
    /*
     * Input Stream for an Analyzer
     */
    private InputStream inputStream;
    
    /**
     * Creates a TokenAnalyzer based on InputStream
     * 
     * @param inputStream
     */
    public TokenAnalyzer(InputStream inputStream) {
        inputStream = new BufferedInputStream(inputStream, DEFAULT_BUFFER_SIZE);
    }

    @Override
    protected IToken readNextElement() {
        return convertToToken(readNextToken());
    }
    
    /**
     * Converts read Text to a Token entity
     *
     * @param tokenText
     * @return
     */
    private IToken convertToToken(String tokenText) {
        return null;
    }
    
    /**
     * Read next Token from an input stream
     *
     * @return
     */
    private String readNextToken() {
        return null;
    }

}
