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
import static org.junit.Assert.assertNull;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceOfLexem;
import org.amanzi.asn1.parser.token.IToken;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests on ClassDefinition Logic
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class ClassDefinitionLogicTest {

	@Rule
	public ExpectedException syntaxException = ExpectedException.none();

	private IStream<IToken> getSequenceTokenStream(String... firstElements) {
		return getTokenStream(
				ArrayUtils.addAll(firstElements, new String[] {
						TokenStreamsData.CLASS_DEFINITION_ASSIGNMENT,
						"SEQUENCE" }), TokenStreamsData.SEQUENCE);
	}

	private IStream<IToken> getEnumaredTokenStream(String... firstElements) {
		return getTokenStream(ArrayUtils.add(firstElements,
				TokenStreamsData.CLASS_DEFINITION_ASSIGNMENT),
				TokenStreamsData.SEQUENCE_ENUMERATED);
	}

	private IStream<IToken> getBitStringTokenStream(String... firstElements) {
		return getTokenStream(ArrayUtils.add(firstElements,
				TokenStreamsData.CLASS_DEFINITION_ASSIGNMENT),
				TokenStreamsData.SEQUENCE_BIT_STRING);
	}

	private IStream<IToken> getChoiceTokenStream(String... firstElements) {
		return getTokenStream(ArrayUtils.addAll(firstElements,
				TokenStreamsData.CLASS_DEFINITION_ASSIGNMENT, "CHOICE"),
				TokenStreamsData.CHOICE_WITHOUT_CLASS_DESCRIPTION);
	}

	private IStream<IToken> getOctetStringTokenStream(String... firstElements) {
		return getTokenStream(ArrayUtils.addAll(firstElements,
				TokenStreamsData.CLASS_DEFINITION_ASSIGNMENT, "OCTET STRING"),
				TokenStreamsData.OCTET_STRING_WITH_MEMBERS);
	}

	private IStream<IToken> getIntegerStream(String... firstElements) {
		return getTokenStream(ArrayUtils.add(firstElements,
				TokenStreamsData.CLASS_DEFINITION_ASSIGNMENT),
				TokenStreamsData.SEQUENCE_INTEGER);
	}

	private IStream<IToken> getSequenceOfStream(String... firstElements) {
		return getTokenStream(ArrayUtils.addAll(firstElements,
				TokenStreamsData.CLASS_DEFINITION_ASSIGNMENT, "SEQUENCE"),
				TokenStreamsData.SEQUENCE_OF_WITH_ENUMERATED);
	}

	private IStream<IToken> getTokenStream(String[] firstElements,
			String... lastElements) {
		return new TestTokenStream(ArrayUtils.addAll(firstElements,
				lastElements));
	}

	private void prepareException(ErrorReason errorReason) {
		syntaxException.expect(SyntaxException.class);
		syntaxException.expectMessage(containsString(errorReason.getMessage()));
	}

	private ClassDefinition run(IStream<IToken> tokenStream)
			throws SyntaxException {
		ClassDefinitionLogic logic = new ClassDefinitionLogic(tokenStream);

		return logic.parse(new ClassDefinition());
	}

	@Test
	public void testCheckCorrectClassName() throws Exception {
		IStream<IToken> tokenStream = getEnumaredTokenStream(TokenStreamsData.CLASS_DEFINITION);

		ClassDefinition result = run(tokenStream);

		assertEquals("unexpected name of Class Definition",
				result.getClassName(), TokenStreamsData.CLASS_DEFINITION);
	}

	@Test
	public void testCheckDescriptionNotNull() throws Exception {
		IStream<IToken> tokenStream = getEnumaredTokenStream(TokenStreamsData.CLASS_DEFINITION);

		ClassDefinition result = run(tokenStream);

		assertNotNull("ClassDescription should exist",
				result.getClassDescription());
	}

	@Test
	public void testCheckCorrectEnumeratedDescriptionType() throws Exception {
		IStream<IToken> tokenStream = getEnumaredTokenStream(TokenStreamsData.CLASS_DEFINITION);

		ClassDefinition result = run(tokenStream);

		assertEquals("unexpected type of Class Description", result
				.getClassDescription().getType(),
				ClassDescriptionType.ENUMERATED);
		assertEquals("unexpected class of Class Description", result
				.getClassDescription().getClass(), Enumerated.class);
	}

	@Test
	public void testCheckCorrectSequenceDescriptionType() throws Exception {
		IStream<IToken> tokenStream = getSequenceTokenStream(TokenStreamsData.CLASS_DEFINITION);

		ClassDefinition result = run(tokenStream);

		assertEquals("unexpected type of Class Description", result
				.getClassDescription().getType(), ClassDescriptionType.SEQUENCE);
		assertEquals("unexpected class of Class Description", result
				.getClassDescription().getClass(), SequenceLexem.class);
	}

	@Test
	public void testCheckCorrectDescriptionType() throws Exception {
		for (ClassDescriptionType descriptionType : ClassDescriptionType
				.values()) {
			IStream<IToken> tokenStream = null;
			Class<? extends IClassDescription> descriptionClass = null;

			String definitionName = TokenStreamsData.CLASS_DEFINITION;

			switch (descriptionType) {
			case ENUMERATED:
				descriptionClass = Enumerated.class;
				tokenStream = getEnumaredTokenStream(definitionName);
				break;
			case SEQUENCE:
				descriptionClass = SequenceLexem.class;
				tokenStream = getSequenceTokenStream(definitionName);
				break;

			case BIT_STRING:
				descriptionClass = BitStringLexem.class;
				tokenStream = getBitStringTokenStream(definitionName);
				break;
			case CHOICE:
				descriptionClass = ChoiceLexem.class;
				tokenStream = getChoiceTokenStream(definitionName);
				break;
			case INTEGER:
				descriptionClass = IntegerLexem.class;
				tokenStream = getIntegerStream(definitionName);
				break;
			case OCTET_STRING:
				descriptionClass = OctetStringLexem.class;
				tokenStream = getOctetStringTokenStream(definitionName);
				break;
			case SEQUENCE_OF:
				descriptionClass = SequenceOfLexem.class;
				tokenStream = getSequenceOfStream(definitionName);
				break;
			default:
				return;
			}

			ClassDefinition definition = run(tokenStream);

			assertNotNull("class description should not be null",
					definition.getClassDescription());
			assertEquals("unexpected Class Description type", descriptionType,
					definition.getClassDescription().getType());
			assertEquals("unexpected Class Description class",
					descriptionClass, definition.getClassDescription()
							.getClass());
		}
	}


	@Test
	public void testCheckUnexpectedEndOfStreamException() throws Exception {
		prepareException(ErrorReason.UNEXPECTED_END_OF_STREAM);

		IStream<IToken> tokenStream = new TestTokenStream("WrongClass",
				TokenStreamsData.CLASS_DEFINITION_ASSIGNMENT);

		run(tokenStream);
	}

	@Test
	public void testCheckNotSupportedTokenException() throws Exception {
		prepareException(ErrorReason.NO_START_TOKEN);

		IStream<IToken> tokenStream = new TestTokenStream(",",
				TokenStreamsData.CLASS_DEFINITION_ASSIGNMENT);

		run(tokenStream);
	}

	@Test
	public void testCheckUnexpectedTokenInLexem() throws Exception {
		prepareException(ErrorReason.UNEXPECTED_END_OF_LEXEM);

		IStream<IToken> tokenStream = new TestTokenStream(
				"ClassDefinitionName", "SEQUENCE");

		run(tokenStream);
	}

	@Test
	public void testSupportedClassDefinitionName() throws Exception {
		IStream<IToken> tokenStream = getEnumaredTokenStream("ClassDefinition-TDD-8");

		run(tokenStream);
	}

	@Test
	public void testNotSupportedClassDefinitionName1() throws Exception {
		prepareException(ErrorReason.NO_START_TOKEN);

		IStream<IToken> tokenStream = getEnumaredTokenStream("ClassDefinition-TDD-8_");

		run(tokenStream);
	}

	@Test
	public void testNotSupportedClassDefinitionName2() throws Exception {
		prepareException(ErrorReason.NO_START_TOKEN);

		IStream<IToken> tokenStream = getEnumaredTokenStream("ClassDefinition-TDD-8{");

		run(tokenStream);
	}
}
