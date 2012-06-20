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
import java.util.regex.Pattern;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription;
import org.amanzi.asn1.parser.lexer.impl.Sequence;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.token.IToken;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;

/**
 * Logic of parsing for ClassDefinition element
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class ClassDefinitionLogic extends AbstractFabricLogic<ClassDefinition, IClassDescription> {
    
    private static final Pattern CLASS_DEFINITION_NAME_PATTERN = Pattern.compile("[a-zA-Z\\d0-9-]+");
	
	private static HashSet<IToken> SUPPORTED_TOKENS;
	
	private enum State implements IState {
	    STARTED,
	    ASSIGNMENT,
	    DEFINITION;
	}
	
	private ClassDescriptionType currentType;
	
	/**
     * @param tokenStream
     */
    public ClassDefinitionLogic(IStream<IToken> tokenStream) {
        super(tokenStream);
    }

    @Override
    protected boolean canFinish() {
        return currentState == State.DEFINITION;
    }

    @Override
    protected boolean isStartToken(IToken token) {
        return token.isDynamic() && CLASS_DEFINITION_NAME_PATTERN.matcher(token.getTokenText()).matches();
    }
    
    @Override
    protected boolean isTrailingToken(IToken token) { 
        for (ClassDescriptionType type : ClassDescriptionType.values()) {
            if (token.equals(type.getToken())) {
                currentType = type;
                return true;
            }
        }
        
        return false;
    }

    @Override
    protected ClassDefinition parseToken(ClassDefinition blankLexem, IToken token) throws SyntaxException {
        switch ((State)currentState) {
        case STARTED:
            blankLexem.setClassName(token.getTokenText());
            break;
        case ASSIGNMENT:
            if (!token.equals(ControlSymbol.ASSIGNMENT)) {
                throw new SyntaxException(ErrorReason.TOKEN_NOT_SUPPORTED, "Only <::=> token can be placed after ClassDefinition name");
            }
            break;
        }
        
        currentState = nextState(currentState);
        
        return blankLexem;
    }

    @Override
    protected IToken getTrailingToken() {
        return null;
    }

    @Override
    protected Set<IToken> getSupportedTokens() {
    	if (SUPPORTED_TOKENS == null) {
    		SUPPORTED_TOKENS = new HashSet<IToken>();
    		
    		SUPPORTED_TOKENS.add(ControlSymbol.ASSIGNMENT);
    		
    		for (ClassDescriptionType type : ClassDescriptionType.values()) {
    			SUPPORTED_TOKENS.add(type.getToken());
    		}
    	}
    	
        return SUPPORTED_TOKENS;
    }

    @Override
    protected String getLexemName() {
        return "Class Definition";
    }

    @Override
    protected ClassDefinition finishUp(ClassDefinition lexem, IToken token) throws SyntaxException {
        lexem.setClassDescription(parseSubLogic(token));
        
        return super.finishUp(lexem, token);
    }

    @Override
    protected IClassDescription createInitialSubLexem(IToken identifier) {
        switch (currentType) {
        case BIT_STRING:
            return null;
        case CHOISE:
            return null;
        case ENUMERATED:
            return new Enumerated();
        case INTEGER:
            return null;
        case OCTET_STRING:
            return null;
        case SEQUENCE:
            return new Sequence();
        case SEQUENCE_OF:
            return new Sequence();
        }
        
        return null;
    }
    
    @Override
    protected IToken getStartToken() {
        //no start token
        return null;
    }

    @Override
    protected IState nextState(IState currentState) {
        switch ((State)currentState) {
        case STARTED:
            return State.ASSIGNMENT;
        case ASSIGNMENT:
            return State.DEFINITION;
        }
        return null;
    }

    @Override
    protected IState getInitialState() {
        return State.STARTED;
    }
    
    @Override
    protected boolean skipFirstToken() {
        return false;
    }
}
