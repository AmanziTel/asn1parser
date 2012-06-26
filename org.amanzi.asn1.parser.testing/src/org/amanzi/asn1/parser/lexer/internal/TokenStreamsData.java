/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse  License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.amanzi.asn1.parser.lexer.internal;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Utility Class TokenStreamsData for tests
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
final class TokenStreamsData {

	/**
	 * Utility Class Constructor
	 */
	private TokenStreamsData() {
	}

	private static final String INTEGER = "INTEGER";
	private static final String SIZE = "SIZE";
	private static final String ENUMERATED = "ENUMERATED";
	private static final String FIRST_ENUMERATED_VALUE = "enum1";
	private static final String SECOND_ENUMERATED_VALUE = "enum2";
	private static final String SEQUENCE_CONSTANT = "SEQUENCE";
	private static final String CLASS_NAME = "CLASSNAME";
	private static final String CLASS_NAME_DEFINITION_CONSTANT = "className";
	private static final String SECOND_CLASS_NAME = "SECONDCLASSNAME";
	private static final String SECOND_CLASS_NAME_DEFINITION_CONSTANT = "secondClassName";
	private static final String THIRD_CLASS_NAME = "THIRDCLASS";
	private static final String THIRD_CLASS_NAME_DEFINITION_CONSTANT = "thirdClass";
	private static final String OPTIONAL = "OPTIONAL";

	// file lexem
	static final String FILE_TEST_RESOURCES = "resources/test_schema/";
	static final String FILE_TEST_RESOURCES_FILTER = "*.asn";

	static final int CLASS_DEFINITIONS = 0;
	static final int CONSTANT_DEFINITIONS = 1;
	static final int INFORMATION_ELEMENTS = 2;
	static final int INTERNODE_DEFINITIONS = 3;
	static final int PDU_DEFINITIONS = 4;
	static final String CLASS_DEFINITIONS_NAME = "Class-definitions";
	static final String CONSTANT_DEFINITIONS_NAME = "Constant-definitions";
	static final String INFORMATION_ELEMENTS_NAME = "InformationElements";
	static final String INTERNODE_DEFINITIONS_NAME = "Internode-definitions";
	static final String PDU_DEFINITIONS_NAME = "PDU-definitions";

	// exceptions

	static final String LEXEM_CANNOT_BE_NULL = "parsed lexem cannot be null";
	static final String VALUE_CANNOT_BE_NULL = "value cannot be null";
	static final String UNEXPECTED_NAME = "unexpected name :";
	static final String UNEXPECTED_VALUE = "unexpected value :";
	static final String VALUE_CANNOT_BE_EMPTY = "value cannot be empty";
	static final String VALUES_DOESNT_EQUALS = "values doesn't equals";

	// class definition
	static final String CLASS_DEFINITION_ASSIGNMENT = "::=";
	static final String CLASS_DEFINITION = "ClassDefinition";

	// sequence's
	static final String[] SEQUENCE_INTEGER = new String[] { INTEGER, "(", "1",
			"..", "4", ")" };
	static final String[] SEQUENCE_BIT_STRING = new String[] { "BIT STRING",
			"{", "1", "(", "0", ")", ",", "2", "(", "1", ")", ",", "3", "(",
			"2", ")", ",", "4", "(", "3", ")", ",", "5", "(", "4", ")", ",",
			"6", "(", "5", ")", ",", "7", "(", "6", ")", ",", "8", "(", "7",
			")", "}", "(", SIZE, "(", "8", ")", ")" };

	static final String[] SEQUENCE_ENUMERATED = new String[] { ENUMERATED, "{",
			FIRST_ENUMERATED_VALUE, ",", SECOND_ENUMERATED_VALUE, "}" };

	static final String[] SEQUENCE_SIZE = new String[] { "(", SIZE, "(", "1",
			"..", "8", ")", ")", "OF" };

	static final String[] SEQUENCE_WRONG_ENUMERATED = new String[] {
			ENUMERATED, "{", FIRST_ENUMERATED_VALUE, ",",
			SECOND_ENUMERATED_VALUE };

	static final String[] SEQUENCE_OF_WITH_WRONG_ENUMERATED = ArrayUtils
			.addAll(SEQUENCE_SIZE, SEQUENCE_WRONG_ENUMERATED);

	static final String[] SEQUENCE_OF_WITH_ENUMERATED = ArrayUtils.addAll(
			SEQUENCE_SIZE, SEQUENCE_ENUMERATED);

	// SEQUENCE (SIZE()) OF SEQUENCE (SIZE()) OF ENUMERATED {}
	static final String[] SEQUENCE_OF_WITH_SEQUENCE_OF = ArrayUtils.addAll(
			SEQUENCE_SIZE, ArrayUtils.addAll(
					new String[] { SEQUENCE_CONSTANT },
					SEQUENCE_OF_WITH_ENUMERATED));

	static final String[] SEQUENCE_OF_WITH_INTEGER_DEFINITION = ArrayUtils
			.addAll(SEQUENCE_SIZE, SEQUENCE_INTEGER);

	static final String[] SEQUENCE_OF_WITH_CLASS_DEFINITION_NAME = ArrayUtils
			.addAll(SEQUENCE_SIZE, CLASS_NAME);

	static final String[] SEQUENCE_OF_WITH_BIT_STRING = ArrayUtils.addAll(
			SEQUENCE_SIZE, SEQUENCE_BIT_STRING);

	static final String[] CLASS_NAME_DEFINITION = new String[] { CLASS_NAME,
			CLASS_DEFINITION_ASSIGNMENT, INTEGER, "(", "1", "..", "4", ")" };

	static final String[] SECOND_CLASS_NAME_DEFINITION = new String[] {
			SECOND_CLASS_NAME, CLASS_DEFINITION_ASSIGNMENT, ENUMERATED, "{",
			FIRST_ENUMERATED_VALUE, ",", SECOND_ENUMERATED_VALUE, "}" };

	static final String[] THIRD_CLASS_NAME_DEFINITION = ArrayUtils.addAll(
			new String[] { THIRD_CLASS_NAME, CLASS_DEFINITION_ASSIGNMENT },
			SEQUENCE_BIT_STRING);

	static final String[] SEQUENCE = new String[] { "{",
			CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME, ",",
			SECOND_CLASS_NAME_DEFINITION_CONSTANT, SECOND_CLASS_NAME, ",",
			THIRD_CLASS_NAME_DEFINITION_CONSTANT, THIRD_CLASS_NAME, "}" };

	static final String[] SEQUENCE_WRONG_END_OF_STREAM = new String[] { "{",
			CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME, ",",
			SECOND_CLASS_NAME_DEFINITION_CONSTANT, SECOND_CLASS_NAME, ",",
			THIRD_CLASS_NAME_DEFINITION_CONSTANT, THIRD_CLASS_NAME, };

	static final String[] SEQUENCE_WRONG_START = new String[] {
			CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME, ",",
			SECOND_CLASS_NAME_DEFINITION_CONSTANT, SECOND_CLASS_NAME, ",",
			THIRD_CLASS_NAME_DEFINITION_CONSTANT, THIRD_CLASS_NAME, "}" };

	static final String[] SEQUENCE_WITHOUT_REFERENCE = new String[] { "{",
			CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME, ",", SECOND_CLASS_NAME,
			",", THIRD_CLASS_NAME_DEFINITION_CONSTANT, THIRD_CLASS_NAME, "}" };

	static final String[] SEQUENCE_WITH_DIFINITION = new String[] { "{",
			CLASS_NAME_DEFINITION_CONSTANT, "BIT STRING", "(", SIZE, "(", "1",
			"..", "4", ")", ")", ",", "integerClassName", INTEGER, "(", "1",
			"..", "4", ")", ",", "bitString", "OCTET STRING", "(", SIZE, "(",
			"1", "..", "4", ")", ")", "}" };

	static final String[] SEQUENCE_WITH_OPTIONAL_VALUES = new String[] { "{",
			CLASS_NAME_DEFINITION_CONSTANT, ENUMERATED, "{",
			FIRST_ENUMERATED_VALUE, ",", SECOND_ENUMERATED_VALUE, "}",
			OPTIONAL, ",", SECOND_CLASS_NAME_DEFINITION_CONSTANT, "CHOICE",
			"{", CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME, ",",
			SECOND_CLASS_NAME_DEFINITION_CONSTANT, SECOND_CLASS_NAME, ",",
			THIRD_CLASS_NAME_DEFINITION_CONSTANT, THIRD_CLASS_NAME, "}", ",",
			THIRD_CLASS_NAME_DEFINITION_CONSTANT, SEQUENCE_CONSTANT, "(", SIZE,
			"(", "1", "..", "8", ")", ")", "OF", "OCTET STRING", "(", SIZE,
			"(", "1", "..", "4", ")", ")", OPTIONAL, "}" };

	static final String[] SEQUENCE_WITH_SEQUENCES = new String[] { "{",
			CLASS_NAME_DEFINITION_CONSTANT, SEQUENCE_CONSTANT, "(", SIZE, "(",
			"4", "..", "20", ")", ")", "OF", INTEGER, "(", "2", "..", "8", ")",
			OPTIONAL, ",", SECOND_CLASS_NAME_DEFINITION_CONSTANT,
			SEQUENCE_CONSTANT, "(", SIZE, "(", "15", "..", "80", ")", ")",
			"OF", "CHOICE", "{", CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME,
			",", SECOND_CLASS_NAME_DEFINITION_CONSTANT, SECOND_CLASS_NAME, ",",
			THIRD_CLASS_NAME_DEFINITION_CONSTANT, THIRD_CLASS_NAME, "}",
			OPTIONAL, ",", THIRD_CLASS_NAME_DEFINITION_CONSTANT,
			SEQUENCE_CONSTANT, "{", CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME,
			",", SECOND_CLASS_NAME_DEFINITION_CONSTANT, SECOND_CLASS_NAME, ",",
			THIRD_CLASS_NAME_DEFINITION_CONSTANT, THIRD_CLASS_NAME, "}",
			OPTIONAL, "}" };

	// integer lexem

	static final String[] INTEGER_RANGE = new String[] { "(", "1", "..", "45",
			")" };

	static final String[] INTEGER_LEFT_CONST_RANGE = new String[] { "(",
			"hello", "..", "10", ")" };
	static final String[] INTEGER_RIGHT_CONST_RANGE = new String[] { "(", "23",
			"..", "hello", ")" };
	static final String[] INTEGER_CONSTS_RANGE = new String[] { "(", "left",
			"..", "right", ")" };
	static final String[] INTEGER_ASSIGNMENT = new String[] {
			CLASS_DEFINITION_ASSIGNMENT, "20" };
	static final String[] INTEGER_WITH_ONE_MEMBER = new String[] { "(", "7",
			")" };

	// bit string lexem

	static final String[] BIT_STRING_WITH_SIZE = new String[] { "(", SIZE, "(",
			"16", ")" };
	static final String[] BIT_STRING_WITH_RANGE_SIZE = new String[] { "(",
			SIZE, "(", "5", "..", "10", ")" };
	static final String[] BIT_STRING_WITH_FIRST_CONST_RANGE_SIZE = new String[] {
			"(", SIZE, "(", "hi", "..", "10", ")" };
	static final String[] BIT_STRING_WITH_SECOND_CONST_RANGE_SIZE = new String[] {
			"(", SIZE, "(", "14", "..", "hello", ")" };
	static final String[] BIT_STRING_WITH_MEMBERS = new String[] { "{", "1",
			"(", "0", ")", ",", "2", "(", "1", ")", ",", "3", "(", "2", ")",
			",", "4", "(", "3", ")", ",", "5", "(", "4", ")", ",", "6", "(",
			"5", ")", ",", "7", "(", "6", ")", ",", "8", "(", "7", ")", "}",
			"(", SIZE, "(", "8", ")", ")" };
	static final String[] BIT_STRING_WITH_MEMBERS_AND_SIZE = ArrayUtils.addAll(
			BIT_STRING_WITH_MEMBERS, BIT_STRING_WITH_SIZE);

	static final String[] BIT_STRING_WITH_CONTAINING_LEXEM = new String[] {
			"(", "CONTAINING", CLASS_NAME_DEFINITION_CONSTANT, ")" };

	// octet string lexem

	static final String[] OCTET_STRING_SIZE = ArrayUtils.add(
			BIT_STRING_WITH_SIZE, ")");

	static final String[] OCTET_STRING_WITH_MEMBERS = new String[] { "{", "1",
			"(", "0", ")", ",", "2", "(", "1", ")", ",", "3", "(", "2", ")",
			",", "4", "(", "3", ")", ",", "5", "(", "4", ")", ",", "6", "(",
			"5", ")", ",", "7", "(", "6", ")", ",", "8", "(", "7", ")", "}",
			"(", SIZE, "(", "8", ")", ")" };

	// Choice lexem

	static final String[] CHOICE_WITHOUT_END_TOKENS = new String[] { "{",
			CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME, ",",
			SECOND_CLASS_NAME_DEFINITION_CONSTANT };

	static final String[] CHOICE_WITH_UNEXPECTED_END_OF_LEXEM = new String[] {
			"{", CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME, ",", "}",
			SECOND_CLASS_NAME_DEFINITION_CONSTANT };

	static final String[] CHOICE_WITHOUT_START = new String[] {
			CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME, ",",
			SECOND_CLASS_NAME_DEFINITION_CONSTANT, CLASS_NAME, "}" };

	static final String[] CHOICE_WITHOUT_CLASS_DESCRIPTION = ArrayUtils.addAll(
			CHOICE_WITHOUT_END_TOKENS, new String[] { SECOND_CLASS_NAME, "}" });

	static final String[] CHOICE_WITH_BIT_STRING_SEQUNECE_OF_AND_INTEGER = ArrayUtils
			.addAll(CHOICE_WITHOUT_END_TOKENS, ArrayUtils.addAll(
					SEQUENCE_BIT_STRING, ArrayUtils.addAll(new String[] { ",",
							INTEGER }, ArrayUtils.addAll(SEQUENCE_INTEGER,
							ArrayUtils.addAll(new String[] { ",", "sequenceOf",
									SEQUENCE_CONSTANT }, ArrayUtils.addAll(
									SEQUENCE_OF_WITH_SEQUENCE_OF, "}"))))));

	static final String[] CHOICE_WITH_CLASS_DESCRIPTION = ArrayUtils
			.addAll(ArrayUtils.addAll(CHOICE_WITHOUT_END_TOKENS,
					SEQUENCE_ENUMERATED),
					ArrayUtils.addAll(
							new String[] { ",", "thirdClassName",
									THIRD_CLASS_NAME, ",", "sequence",
									SEQUENCE_CONSTANT },
							ArrayUtils
									.addAll(SEQUENCE_WITH_OPTIONAL_VALUES,
											ArrayUtils
													.addAll(new String[] { ",",
															"octetString",
															"OCTET STRING" },
															ArrayUtils
																	.addAll(OCTET_STRING_SIZE,
																			ArrayUtils
																					.addAll(new String[] {
																							",",
																							"innerChoice",
																							"CHOICE" },
																							ArrayUtils
																									.addAll(CHOICE_WITHOUT_CLASS_DESCRIPTION,
																											"}")))))));
}
