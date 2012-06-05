/**
 * 
 */
package org.amanzi.asn1.parser.lexer.internal;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.ILexem;
import org.amanzi.asn1.parser.token.IToken;

/**
 * Abstract Logic class that can create sub-logics
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 *
 */
public abstract class AbstractFabricLogic<T extends ILexem> extends AbstractLexemLogic<T> {
	
	/**
	 * @param tokenStream
	 */
	public AbstractFabricLogic(IStream<IToken> tokenStream) {
		super(tokenStream);
	}
	
	/**
	 * Runs parsing of SubLexem
	 * 
	 * @param identifier
	 * @param initialLexem
	 * @return
	 * @throws SyntaxException
	 */
	protected <V extends ILexem> V parseSubLogic(IToken identifier, V initialLexem) throws SyntaxException {
		ILexemLogic<V> subLexem = Asn1LogicFactory.createLogic(identifier, tokenStream);
		
		return subLexem.parse(initialLexem);
	}

}
