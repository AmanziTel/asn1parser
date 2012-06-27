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
import java.util.Set;

import org.amanzi.asn1.parser.IStream;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.ClassReference;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceOfLexem;
import org.amanzi.asn1.parser.lexer.impl.Size;
import org.amanzi.asn1.parser.lexer.ranges.impl.Range;
import org.amanzi.asn1.parser.token.IToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for SEQUENCE and SEQUENCE OF definitions
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class SequenceLexemLogicTest {

	private static final int EXPECTED_SIZE = 8;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testExpectedSequenceOfIntegerDefinitionResult()
			throws SyntaxException {
		SequenceOfLexem lexem = parseSequenceOfLexem(TokenStreamsData.SEQUENCE_OF_WITH_INTEGER_DEFINITION);

		verifySize("1", "8", EXPECTED_SIZE, lexem.getSize());

		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE + lexem.getType(),
				ClassDescriptionType.SEQUENCE_OF, lexem.getType());
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE
				+ lexem.getClassReference().getClassDescription().getType(),
				ClassDescriptionType.INTEGER, lexem.getClassReference()
						.getClassDescription().getType());
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE, IntegerLexem.class,
				lexem.getClassReference().getClassDescription().getClass());
		assertEquals(
				TokenStreamsData.UNEXPECTED_VALUE,
				0,
				((IntegerLexem) lexem.getClassReference().getClassDescription())
						.getSize());
	}

	@Test
	public void testExpectedSequenceOfWithBitStringResult()
			throws SyntaxException {
		SequenceOfLexem lexem = parseSequenceOfLexem(TokenStreamsData.SEQUENCE_OF_WITH_BIT_STRING);
		verifySize("1", "8", EXPECTED_SIZE, lexem.getSize());

		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE + lexem.getType(),
				ClassDescriptionType.SEQUENCE_OF, lexem.getType());
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE
				+ lexem.getClassReference().getClassDescription().getType(),
				ClassDescriptionType.BIT_STRING, lexem.getClassReference()
						.getClassDescription().getType());
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE, BitStringLexem.class,
				lexem.getClassReference().getClassDescription().getClass());
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE, EXPECTED_SIZE,
				((BitStringLexem) lexem.getClassReference()
						.getClassDescription()).getSize().getSize());

		int expectedMemberValue = 0;
		for (String value : ((BitStringLexem) lexem.getClassReference()
				.getClassDescription()).getMembers().values()) {
			assertEquals(TokenStreamsData.UNEXPECTED_VALUE
					+ expectedMemberValue++,
					String.valueOf(expectedMemberValue), value);
		}
	}

	@Test
	public void testExpectedSequenceOfDefinitionNameResult()
			throws SyntaxException {
		SequenceOfLexem lexem = parseSequenceOfLexem(TokenStreamsData.SEQUENCE_OF_WITH_CLASS_DEFINITION_NAME);
		verifySize("1", "8", EXPECTED_SIZE, lexem.getSize());

		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE + lexem.getType(),
				ClassDescriptionType.SEQUENCE_OF, lexem.getType());
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE
				+ lexem.getClassReference().getName(), "CLASSNAME", lexem
				.getClassReference().getName());
		// Define class description for CLASSNAME type
		ClassDefinition classNameDefinitionLexem = parseClassDefinition(TokenStreamsData.CLASS_NAME_DEFINITION);

		assertEquals(TokenStreamsData.UNEXPECTED_VALUE,
				ClassDescriptionType.INTEGER, lexem.getClassReference()
						.getClassDescription().getType());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				classNameDefinitionLexem.getClassDescription().getType(), lexem
						.getClassReference().getClassDescription().getType());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				classNameDefinitionLexem.getClassDescription(), lexem
						.getClassReference().getClassDescription());
	}

	@Test
	public void testExpectedSequenceMembersResult() throws SyntaxException {
		ClassDefinition thirdClassNameDefinition = parseClassDefinition(TokenStreamsData.THIRD_CLASS_NAME_DEFINITION);
		ClassDefinition classNameDefinitionLexem = parseClassDefinition(TokenStreamsData.CLASS_NAME_DEFINITION);

		SequenceLexem lexem = parseSequenceLexem(TokenStreamsData.SEQUENCE);
		ClassDefinition secondClassDefinitionLexem = parseClassDefinition(TokenStreamsData.SECOND_CLASS_NAME_DEFINITION);

		Set<ClassReference> lexemMembers = lexem.getMembers();

		boolean isOptional = false;
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE + lexem.getType(),
				ClassDescriptionType.SEQUENCE, lexem.getType());

		List<ClassDescriptionType> expectedTypes = Arrays.asList(
				ClassDescriptionType.BIT_STRING,
				ClassDescriptionType.ENUMERATED, ClassDescriptionType.INTEGER);

		for (ClassReference actualMemberReference : lexemMembers) {
			ClassDescriptionType currentType = actualMemberReference
					.getClassDescription().getType();
			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + currentType,
					expectedTypes.contains(currentType));
			if (ClassDescriptionType.INTEGER.equals(currentType)) {
				compareClassDefinitionWithLexemMember(classNameDefinitionLexem,
						actualMemberReference, isOptional);
			} else if (ClassDescriptionType.ENUMERATED.equals(currentType)) {
				compareClassDefinitionWithLexemMember(
						secondClassDefinitionLexem, actualMemberReference,
						isOptional);
			} else if (ClassDescriptionType.BIT_STRING.equals(currentType)) {
				compareClassDefinitionWithLexemMember(thirdClassNameDefinition,
						actualMemberReference, isOptional);
			}
		}
	}

	@Test
	public void testExpectedSequenceWithDefinitionMembersResult()
			throws SyntaxException {
		SequenceLexem lexem = parseSequenceLexem(TokenStreamsData.SEQUENCE_WITH_DIFINITION);
		ClassDefinition thirdClassNameDefinition = parseClassDefinition(TokenStreamsData.THIRD_CLASS_NAME_DEFINITION);
		ClassDefinition classNameDefinitionLexem = parseClassDefinition(TokenStreamsData.CLASS_NAME_DEFINITION);

		boolean isOptional = false;
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE + lexem.getType(),
				ClassDescriptionType.SEQUENCE, lexem.getType());

		List<ClassDescriptionType> expectedTypes = Arrays
				.asList(ClassDescriptionType.BIT_STRING,
						ClassDescriptionType.OCTET_STRING,
						ClassDescriptionType.INTEGER);

		Set<ClassReference> lexemMembers = lexem.getMembers();

		List<String> integerReferencesName = Arrays.asList("className",
				"integerClassName", "bitString");

		for (ClassReference actualMemberReference : lexemMembers) {
			ClassDescriptionType currentType = actualMemberReference
					.getClassDescription().getType();
			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + currentType,
					expectedTypes.contains(currentType));

			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + currentType,
					expectedTypes.contains(currentType));
			assertTrue(integerReferencesName.contains(actualMemberReference
					.getName()));
			if (ClassDescriptionType.INTEGER.equals(currentType)) {
				compareClassDefinitionWithLexemMember(classNameDefinitionLexem,
						actualMemberReference, isOptional);
			} else if (ClassDescriptionType.BIT_STRING.equals(currentType)) {
				compareClassDefinitionWithLexemMember(thirdClassNameDefinition,
						actualMemberReference, isOptional);
			}
		}
	}

	@Test
	public void testExpectedSequenceOfSequenceOfEnumeratedResult()
			throws SyntaxException {
		SequenceOfLexem lexem = parseSequenceOfLexem(TokenStreamsData.SEQUENCE_OF_WITH_SEQUENCE_OF);
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE + lexem.getType(),
				ClassDescriptionType.SEQUENCE_OF, lexem.getType());
		ClassReference actualReference = lexem.getClassReference();
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, actualReference);
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL,
				actualReference.getClassDescription());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				ClassDescriptionType.SEQUENCE_OF, actualReference
						.getClassDescription().getType());
		ClassReference actualInnerSequenceClassReference = ((SequenceOfLexem) actualReference
				.getClassDescription()).getClassReference();
		assertEquals(ClassDescriptionType.ENUMERATED,
				actualInnerSequenceClassReference.getClassDescription()
						.getType());
		verifyEnumerationMembers((Enumerated) actualInnerSequenceClassReference
				.getClassDescription());

	}

	@Test
	public void testExpectedSequenceWithOptionalMembersResult()
			throws SyntaxException {
		SequenceLexem lexem = parseSequenceLexem(TokenStreamsData.SEQUENCE_WITH_OPTIONAL_VALUES);

		parseClassDefinition(TokenStreamsData.THIRD_CLASS_NAME_DEFINITION);
		parseClassDefinition(TokenStreamsData.CLASS_NAME_DEFINITION);
		parseClassDefinition(TokenStreamsData.SECOND_CLASS_NAME_DEFINITION);

		List<ClassDescriptionType> expectedTypes = Arrays.asList(
				ClassDescriptionType.ENUMERATED, ClassDescriptionType.CHOICE,
				ClassDescriptionType.SEQUENCE_OF);

		Set<ClassReference> lexemMembers = lexem.getMembers();

		for (ClassReference actualMemberReference : lexemMembers) {
			ClassDescriptionType currentType = actualMemberReference
					.getClassDescription().getType();
			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + currentType,
					expectedTypes.contains(currentType));
			if (ClassDescriptionType.ENUMERATED.equals(currentType)) {
				assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
						ClassDescriptionType.ENUMERATED, actualMemberReference
								.getClassDescription().getType());
				verifyEnumerationMembers((Enumerated) actualMemberReference
						.getClassDescription());
				assertTrue(TokenStreamsData.UNEXPECTED_VALUE,
						actualMemberReference.isOptional());
				assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
						"className", actualMemberReference.getName());

			} else if (ClassDescriptionType.CHOICE.equals(currentType)) {
				assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
						ClassDescriptionType.CHOICE, actualMemberReference
								.getClassDescription().getType());
				List<ClassDescriptionType> expectedSequenceMembers = Arrays
						.asList(ClassDescriptionType.BIT_STRING,
								ClassDescriptionType.INTEGER,
								ClassDescriptionType.ENUMERATED);
				ChoiceLexem choice = (ChoiceLexem) actualMemberReference
						.getClassDescription();
				for (ClassReference member : choice.getMembers()) {
					ClassDescriptionType sequenceMemberType = member
							.getClassDescription().getType();
					assertTrue(TokenStreamsData.UNEXPECTED_VALUE
							+ sequenceMemberType,
							expectedSequenceMembers
									.contains(sequenceMemberType));
				}

				assertFalse(TokenStreamsData.UNEXPECTED_VALUE,
						actualMemberReference.isOptional());

				assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
						"secondClassName", actualMemberReference.getName());
			} else if (ClassDescriptionType.SEQUENCE_OF.equals(currentType)) {
				assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
						ClassDescriptionType.SEQUENCE_OF, actualMemberReference
								.getClassDescription().getType());
				SequenceOfLexem sequenceOf = (SequenceOfLexem) actualMemberReference
						.getClassDescription();
				verifySize("1", "8", 8, sequenceOf.getSize());
				assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
						ClassDescriptionType.OCTET_STRING, sequenceOf
								.getClassReference().getClassDescription()
								.getType());
				assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
						"thirdClass", actualMemberReference.getName());

				assertTrue(TokenStreamsData.UNEXPECTED_VALUE,
						actualMemberReference.isOptional());
			}
		}
	}

	@Test
	public void testExpectedSequenceWithSequencesResult()
			throws SyntaxException {
		SequenceLexem lexem = parseSequenceLexem(TokenStreamsData.SEQUENCE_WITH_SEQUENCES);
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS,
				ClassDescriptionType.SEQUENCE, lexem.getType());

		List<ClassDescriptionType> expectedSequenceMembers = Arrays
				.asList(ClassDescriptionType.SEQUENCE,
						ClassDescriptionType.SEQUENCE_OF);
		List<String> referencesNames = Arrays.asList("className",
				"secondClassName", "thirdClass");
		for (ClassReference sequenceMember : lexem.getMembers()) {
			ClassDescriptionType sequenceMemberType = sequenceMember
					.getClassDescription().getType();
			String name = sequenceMember.getName();
			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + sequenceMemberType,
					expectedSequenceMembers.contains(sequenceMemberType));

			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + name,
					referencesNames.contains(name));

			assertTrue(TokenStreamsData.UNEXPECTED_VALUE,
					sequenceMember.isOptional());
		}
	}

	@Test
	public void testUnexpectedSequenceEndOfStream() throws SyntaxException {
		exception.expect(SyntaxException.class);
		exception
				.expectMessage(containsString(ErrorReason.UNEXPECTED_END_OF_STREAM
						.getMessage()));
		parseSequenceLexem(TokenStreamsData.SEQUENCE_WRONG_END_OF_STREAM);
	}

	@Test
	public void testUnexpectedSequenceStartToken() throws SyntaxException {
		exception.expect(SyntaxException.class);
		exception.expectMessage(containsString(ErrorReason.NO_START_TOKEN
				.getMessage()));
		parseSequenceLexem(TokenStreamsData.SEQUENCE_WRONG_START);
	}

	private void verifyEnumerationMembers(Enumerated actualEnumerated) {
		List<String> expectedEnumeratedValues = Arrays.asList("enum1", "enum2");

		for (String member : actualEnumerated.getMembers()) {
			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + member,
					expectedEnumeratedValues.contains(member));
		}
	}

	private void verifySize(String lowerBound, String upperBound,
			int expectedSize, Size actualSize) throws SyntaxException {
		Size expected = getExpectedSize(lowerBound, upperBound, expectedSize);
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, expected.getSize(),
				actualSize.getSize());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, expected.getRange()
				.getLowerBound(), actualSize.getRange().getLowerBound());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, expected.getRange()
				.getUpperBound(), actualSize.getRange().getUpperBound());
	}

	/**
	 * Compare ClassDefinition with lexem ClassReference member
	 * 
	 * @param definition
	 *            ClassDefinition
	 * @param referenceMember
	 *            member
	 * @param referenceName
	 *            ClassReference name
	 * @param isOptional
	 *            optional or not
	 */
	private void compareClassDefinitionWithLexemMember(
			ClassDefinition definition, ClassReference referenceMember,
			boolean isOptional) {
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, definition
				.getClassDescription().getType(), referenceMember
				.getClassDescription().getType());
		assertEquals(TokenStreamsData.VALUES_DOESNT_EQUALS, isOptional,
				referenceMember.isOptional());
	}

	private Size getExpectedSize(String lowerBound, String upperBound, int size) {
		Size result = new Size();
		Range range = new Range();

		range.setLowerBound(lowerBound);
		range.setUpperBound(upperBound);
		result.setRange(range);
		result.setSize(size);

		return result;
	}

	private ClassDefinition parseClassDefinition(String[] tokens)
			throws SyntaxException {
		IStream<IToken> tokenStream = new TestTokenStream(tokens);
		ClassDefinitionLogic logic = new ClassDefinitionLogic(tokenStream);
		return logic.parse(new ClassDefinition());
	}

	private SequenceOfLexem parseSequenceOfLexem(String[] tokens)
			throws SyntaxException {
		return (SequenceOfLexem) getLogic(tokens).parse(new SequenceOfLexem());
	}

	private SequenceLexem parseSequenceLexem(String[] tokens)
			throws SyntaxException {
		return (SequenceLexem) getLogic(tokens).parse(new SequenceLexem());
	}

	private SequenceLexemLogic getLogic(String[] tokens) {
		IStream<IToken> tokenStream = new TestTokenStream(tokens);
		return new SequenceLexemLogic(tokenStream);
	}
}
