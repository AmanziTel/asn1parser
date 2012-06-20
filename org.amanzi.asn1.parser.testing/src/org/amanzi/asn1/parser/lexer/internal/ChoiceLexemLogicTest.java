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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.Arrays;
import java.util.List;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.ClassReference;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.token.IToken;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link ChoiceLexemLogic}
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class ChoiceLexemLogicTest {

	@Rule
	public ExpectedException syntaxException = ExpectedException.none();

	@Before
	public void prepareTests() {
		try {
			parseClassDefinition();
		} catch (Exception e) {
		}
	}

	@Test
	public void testExpectedChoiceWithoutDescriptionsResult() throws Exception {
		ChoiceLexem lexem = parseChoiceLexem(TokenStreamsData.CHOICE_WITHOUT_CLASS_DESCRIPTION);

		verifyChoiceMembers(lexem, Arrays.asList(ClassDescriptionType.INTEGER,
				ClassDescriptionType.ENUMERATED), Arrays.asList("className",
				"secondClassName"));
	}

	@Test
	public void testExpectedChoiceWithDescriptionsResult() throws Exception {
		ChoiceLexem lexem = parseChoiceLexem(TokenStreamsData.CHOICE_WITH_CLASS_DESCRIPTION);

		verifyChoiceMembers(lexem,
				Arrays.asList(ClassDescriptionType.INTEGER,
						ClassDescriptionType.BIT_STRING,
						ClassDescriptionType.ENUMERATED,
						ClassDescriptionType.CHOICE,
						ClassDescriptionType.SEQUENCE,
						ClassDescriptionType.OCTET_STRING), Arrays.asList(
						"className", "secondClassName", "thirdClassName",
						"innerChoice", "octetString", "sequence"));
	}

	@Test
	public void testExpectedChoiceWithBitStringSequenceOfAndIntegerResult()
			throws Exception {
		ChoiceLexem lexem = parseChoiceLexem(TokenStreamsData.CHOICE_WITH_BIT_STRING_SEQUNECE_OF_AND_INTEGER);

		verifyChoiceMembers(lexem, Arrays.asList(ClassDescriptionType.INTEGER,
				ClassDescriptionType.BIT_STRING,
				ClassDescriptionType.SEQUENCE_OF), Arrays.asList("className",
				"secondClassName", "sequenceOf", "integer"));
	}

	@Test
	public void testUnexpectedSequenceEndOfStream() throws Exception {
		syntaxException.expect(SyntaxException.class);
		syntaxException
				.expectMessage(containsString(ErrorReason.UNEXPECTED_END_OF_STREAM
						.getMessage()));
		parseChoiceLexem(TokenStreamsData.CHOICE_WITHOUT_END_TOKENS);
	}

	@Test
	public void testUnexpectedSequenceStartToken() throws Exception {
		syntaxException.expect(SyntaxException.class);
		syntaxException.expectMessage(containsString(ErrorReason.NO_START_TOKEN
				.getMessage()));
		parseChoiceLexem(TokenStreamsData.CHOICE_WITHOUT_START);
	}	

	private void verifyChoiceMembers(ChoiceLexem lexem,
			List<ClassDescriptionType> expectedTypes, List<String> expectedNames) {
		assertNotNull("parsed data cannot be null", lexem);
		assertEquals("unexpected lexem type", ClassDescriptionType.CHOICE,
				lexem.getType());
		for (ClassReference reference : lexem.getMembers()) {
			assertNotNull("reference class description cannot be null",
					reference.getClassDescription());
			assertTrue("unexpected class description type : "
					+ reference.getClassDescription().getType(),
					expectedTypes.contains(reference.getClassDescription()
							.getType()));
			assertTrue("unexpected reference name :" + reference.getName(),
					expectedNames.contains(reference.getName()));
			assertFalse("choice members connot be optional!",
					reference.isOptional());
		}
	}

	/**
	 * Parse tokens
	 * 
	 * @param tokens
	 *            tokens string array
	 * @return parsed choice lexem
	 * @throws Exception
	 */
	private ChoiceLexem parseChoiceLexem(String[] tokens) throws Exception {
		ChoiceLexemLogic logic = new ChoiceLexemLogic(new TestTokenStream(
				tokens));
		return logic.parse(new ChoiceLexem());
	}

	private void parseClassDefinition() throws Exception {
		IStream<IToken> firstTokenStream = new TestTokenStream(
				TokenStreamsData.CLASS_NAME_DEFINITION);
		IStream<IToken> secondTokenStream = new TestTokenStream(
				TokenStreamsData.SECOND_CLASS_NAME_DEFINITION);
		IStream<IToken> thirdTokenStream = new TestTokenStream(
				TokenStreamsData.THIRD_CLASS_NAME_DEFINITION);

		ClassDefinition definition = new ClassDefinition();
		ClassDefinitionLogic logic = new ClassDefinitionLogic(firstTokenStream);
		logic.parse(definition);
		logic = new ClassDefinitionLogic(secondTokenStream);
		logic.parse(definition);
		logic = new ClassDefinitionLogic(thirdTokenStream);
		logic.parse(definition);
	}
}
