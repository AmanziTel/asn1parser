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
abstract class AbstractFabricLogic<T extends ILexem, V extends ILexem> extends AbstractLexemLogic<T> {
	
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
	protected V parseSubLogic(IToken identifier) throws SyntaxException {
		ILexemLogic<V> subLexem = Asn1LogicFactory.createLogic(identifier, tokenStream);	
		return subLexem.parse(createInitialSubLexem(identifier));
	}
	
	protected abstract V createInitialSubLexem(IToken identifier);

}
