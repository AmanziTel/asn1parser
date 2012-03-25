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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.amanzi.asn1.parser.AbstractStream;
import org.amanzi.asn1.parser.token.impl.DefaultToken;
import org.amanzi.asn1.parser.token.impl.SimpleToken;
import org.apache.commons.lang3.ArrayUtils;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class TokenAnalyzer extends AbstractStream<IToken> {

    /** int END_OF_FILE_CHARACTER field */
    private static final int END_OF_FILE_CHARACTER = -1;

    /**
     * Default size of Stream Buffer
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 100;
    
    /**
     * Character for a Next Line
     */
    private static final int NEXT_LINE_CHARACTER = '\n';

    /**
     * Default Separations Characters
     */
    private static final int[] DEFAULT_SKIP_CHARACTERS = new int[] {' ', '\n', '\t'};

    /*
     * Cache for Tokens to prevent re-creation of similar tokens
     */
    private HashMap<String, IToken> tokenCache = new HashMap<>();

    /*
     * Input Stream for an Analyzer
     */
    private InputStream inputStream;

    /*
     * String to a Default Token that trails main Token
     */
    private String trailingToken;

    /*
     * Array of all characters that can be used in Default Tokens
     */
    private Set<Integer> possibleSeparators;

    /**
     * Creates a TokenAnalyzer based on InputStream
     * 
     * @param inputStream
     */
    public TokenAnalyzer(InputStream inputStream) {
        this.inputStream = new BufferedInputStream(inputStream, DEFAULT_BUFFER_SIZE);

        initialize();
    }

    /**
     * Initializes Possible Separators
     */
    private void initialize() {
        possibleSeparators = new HashSet<>();
        for (DefaultToken token : DefaultToken.values()) {
            for (int character : token.getTokenText().getBytes()) {
                possibleSeparators.add(character);
            }
        }
    }

    @Override
    protected IToken readNextElement() {
        try {
            String tokenText = readNextToken();

            if (tokenText.isEmpty()) {
                return null;
            }

            IToken token = tokenCache.get(tokenText);
            if (token == null) {
                token = convertToToken(tokenText);
                tokenCache.put(tokenText, token);
            }

            return token;
        } catch (IOException e) {
            // TODO: add logger, log exception here
        }

        return null;
    }

    /**
     * Converts read Text to a Token entity
     * 
     * @param tokenText
     * @return
     */
    private IToken convertToToken(String tokenText) {
        IToken result = null;
        
        result = DefaultToken.findByText(tokenText);
        
        if (result == null) {
            result = new SimpleToken(tokenText);
        }
        
        return result;
    }

    /**
     * Read next Token from an input stream
     * 
     * @return
     */
    private String readNextToken() throws IOException {
        if (trailingToken != null) {
            String toReturn = trailingToken;
            trailingToken = null;
            return toReturn;
        }

        StringBuffer token = new StringBuffer();

        while (true) {
            int read = inputStream.read();

            boolean process = (read != END_OF_FILE_CHARACTER) && !ArrayUtils.contains(DEFAULT_SKIP_CHARACTERS, read);

            if (process) {
                char readChar = (char)read;
                token.append(readChar);

                if (possibleSeparators.contains(read)) {
                    String tokenString = token.toString();

                    for (DefaultToken singleToken : DefaultToken.getPossibleTokens(read)) {
                        if (singleToken.checkText(tokenString)) {
                            trailingToken = singleToken.getTokenText();
                            String toReturn = singleToken.cut(tokenString);
                            
                            if (toReturn.isEmpty()) {
                                toReturn = trailingToken;
                                trailingToken = null;
                            }
                            
                            if (singleToken == DefaultToken.COMMENT) {
                                skipUntilNextLine();
                            }
                            
                            return toReturn;
                        }
                    }
                }
            } else {
                if ((token.length() > 0) || (read == END_OF_FILE_CHARACTER)) {
                    break;
                }
            }
        }

        return token.toString();
    }
    
    /**
     * In case of 
     */
    private void skipUntilNextLine() throws IOException {
        int read = END_OF_FILE_CHARACTER;
        
        do {
            read = inputStream.read();
        } while ((read != END_OF_FILE_CHARACTER) && (read != NEXT_LINE_CHARACTER));
    }

}
