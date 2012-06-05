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

import java.util.HashSet;
import java.util.Set;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;

/**
 * Logic of parsing for ClassDefinition element
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class ClassDefinitionLogic extends AbstractFabricLogic<ClassDefinition> {
	
	private static HashSet<IToken> SUPPORTED_TOKENS;
	
	/**
     * @param tokenStream
     */
    public ClassDefinitionLogic(IStream<IToken> tokenStream) {
        super(tokenStream);
    }

    @Override
    protected boolean canFinish() {
        return false;
    }

    @Override
    protected IToken getStartToken() {
        return null;
    }

    @Override
    protected ClassDefinition parseToken(ClassDefinition blankLexem, IToken token) throws SyntaxException {
        return null;
    }

    @Override
    protected IToken getTrailingToken() {
        return null;
    }

    @Override
    protected Set<IToken> getSupportedTokens() {
    	if (SUPPORTED_TOKENS == null) {
    		SUPPORTED_TOKENS = new HashSet<>();
    		
    		SUPPORTED_TOKENS.add(ControlSymbol.ASSIGNMENT);
    		
    		for (ClassDescriptionType type : ClassDescriptionType.values()) {
    			SUPPORTED_TOKENS.add(type.getToken());
    		}
    	}
    	
        return SUPPORTED_TOKENS;
    }

    @Override
    protected String getLexemName() {
        return null;
    }

    @Override
    protected ClassDefinition finishUp(ClassDefinition lexem) throws SyntaxException {
        return null;
    }

}
