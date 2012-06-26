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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.Size;
import org.amanzi.asn1.parser.token.IToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class SizeLexemLogicTest {

	@Rule
	public ExpectedException syntaxException = ExpectedException.none();

	@Test
	public void testGetSimpleSize() throws Exception {
		IStream<IToken> tokenStream = new TestTokenStream("(", "15", ")");

		SizeLexemLogic logic = new SizeLexemLogic(tokenStream);
		Size size = logic.parse(new Size());

		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, size);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, 15, size.getSize());
	}

	@Test
	public void testGetSimpleRangeSize() throws Exception {
		IStream<IToken> tokenStream = new TestTokenStream("(", "1", "..", "15",
				")");

		SizeLexemLogic logic = new SizeLexemLogic(tokenStream);
		Size size = logic.parse(new Size());

		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, size);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, 15, size.getSize());
	}

	@Test
	public void testGetLowerBoundRangeSize() throws Exception {
		IStream<IToken> tokenStream = new TestTokenStream("(", "hallo", "..",
				"15", ")");

		SizeLexemLogic logic = new SizeLexemLogic(tokenStream);
		Size size = logic.parse(new Size());

		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, size);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, "hallo", size
				.getRange().getLowerBound());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, "15", size
				.getRange().getUpperBound());
	}

	@Test
	public void testGetUpperBoundRangeSize() throws Exception {
		IStream<IToken> tokenStream = new TestTokenStream("(", "10", "..",
				"twenty", ")");

		SizeLexemLogic logic = new SizeLexemLogic(tokenStream);
		Size size = logic.parse(new Size());

		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, size);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, "10", size
				.getRange().getLowerBound());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, "twenty", size
				.getRange().getUpperBound());
	}

	@Test
	public void testNoStartBrake() throws Exception {
		syntaxException.expect(SyntaxException.class);
		syntaxException.expectMessage(containsString(ErrorReason.NO_START_TOKEN
				.getMessage()));

		IStream<IToken> tokenStream = new TestTokenStream("10", "..", "twenty",
				")");

		SizeLexemLogic logic = new SizeLexemLogic(tokenStream);
		logic.parse(new Size());
	}

	@Test
	public void testNoTrailingBrake() throws Exception {
		syntaxException.expect(SyntaxException.class);
		syntaxException
				.expectMessage(containsString(ErrorReason.UNEXPECTED_END_OF_STREAM
						.getMessage()));

		IStream<IToken> tokenStream = new TestTokenStream("(", "10", "..",
				"twenty");

		SizeLexemLogic logic = new SizeLexemLogic(tokenStream);
		logic.parse(new Size());
	}

	@Test
	public void testUnsupportedToken() throws Exception {
		syntaxException.expect(SyntaxException.class);
		syntaxException
				.expectMessage(containsString(ErrorReason.TOKEN_NOT_SUPPORTED
						.getMessage()));

		IStream<IToken> tokenStream = new TestTokenStream("(", "BEGIN", ")");

		SizeLexemLogic logic = new SizeLexemLogic(tokenStream);
		logic.parse(new Size());
	}
}
