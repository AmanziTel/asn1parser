/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse protected License
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
 * Token streams data for tests
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class TokenStreamsData {

	protected static final String[] INTEGER = new String[] { "INTEGER", "(",
			"1", "..", "4", ")" };
	protected static final String[] BIT_STRING = new String[] { "BIT STRING",
			"{", "1", "(", "0", ")", ",", "2", "(", "1", ")", ",", "3", "(",
			"2", ")", ",", "4", "(", "3", ")", ",", "5", "(", "4", ")", ",",
			"6", "(", "5", ")", ",", "7", "(", "6", ")", ",", "8", "(", "7",
			")", "}", "(", "SIZE", "(", "8", ")", ")" };

	protected static final String[] ENUMERATED = new String[] { "ENUMERATED",
			"{", "enum1", ",", "enum2", "}" };

	protected static final String[] SIZE_FOR_SEQUENCE = new String[] { "(",
			"SIZE", "(", "1", "..", "8", ")", ")", "OF" };

	protected static final String[] WRONG_ENUMERATED = new String[] {
			"ENUMERATED", "{", "enum1", ",", "enum2" };

	protected static final String[] SEQUENCE_OF_WITH_WRONG_ENUMERATED = ArrayUtils
			.addAll(SIZE_FOR_SEQUENCE, WRONG_ENUMERATED);

	protected static final String[] SEQUENCE_OF_WITH_ENUMERATED = ArrayUtils
			.addAll(SIZE_FOR_SEQUENCE, ENUMERATED);

	// SEQUENCE (SIZE()) OF SEQUENCE (SIZE()) OF ENUMERATED {}
	protected static final String[] SEQUENCE_OF_WITH_SEQUENCE_OF = ArrayUtils
			.addAll(SIZE_FOR_SEQUENCE, ArrayUtils.addAll(
					new String[] { "SEQUENCE" }, SEQUENCE_OF_WITH_ENUMERATED));

	protected static final String[] SEQUENCE_OF_WITH_INTEGER_DEFINITION = ArrayUtils
			.addAll(SIZE_FOR_SEQUENCE, INTEGER);

	protected static final String[] SEQUENCE_OF_WITH_CLASS_DEFINITION_NAME = ArrayUtils
			.addAll(SIZE_FOR_SEQUENCE, "CLASSNAME");

	protected static final String[] SEQUENCE_OF_WITH_BIT_STRING = ArrayUtils
			.addAll(SIZE_FOR_SEQUENCE, BIT_STRING);

	protected static final String[] CLASS_NAME_DEFINITION = new String[] {
			"CLASSNAME", "::=", "INTEGER", "(", "1", "..", "4", ")" };

	protected static final String[] SECOND_CLASS_NAME_DEFINITION = new String[] {
			"SECONDCLASSNAME", "::=", "ENUMERATED", "{", "enum1", ",", "enum2",
			"}" };

	protected static final String[] THIRD_CLASS_NAME_DEFINITION = ArrayUtils
			.addAll(new String[] { "THIRDCLASS", "::=" }, BIT_STRING);

	protected static final String[] SEQUENCE = new String[] { "{", "className",
			"CLASSNAME", ",", "secondClassName", "SECONDCLASSNAME", ",",
			"thirdClass", "THIRDCLASS", "}" };

	protected static final String[] SEQUENCE_WRONG_END_OF_STREAM = new String[] {
			"{", "className", "CLASSNAME", ",", "secondClassName",
			"SECONDCLASSNAME", ",", "thirdClass", "THIRDCLASS", };

	protected static final String[] SEQUENCE_WRONG_START = new String[] {
			"className", "CLASSNAME", ",", "secondClassName",
			"SECONDCLASSNAME", ",", "thirdClass", "THIRDCLASS", "}" };

	protected static final String[] SEQUENCE_WITHOUT_REFERENCE = new String[] {
			"{", "className", "CLASSNAME", ",", "SECONDCLASSNAME", ",",
			"thirdClass", "THIRDCLASS", "}" };

	protected static final String[] SEQUENCE_WITH_DIFINITION = new String[] {
			"{", "className", "BIT STRING", "(", "SIZE", "(", "1", "..", "4",
			")", ")", ",", "integerClassName", "INTEGER", "(", "1", "..", "4",
			")", ",", "bitString", "OCTET STRING", "(", "SIZE", "(", "1", "..",
			"4", ")", ")", "}" };

	protected static final String[] SEQUENCE_WITH_OPTIONAL_VALUES = new String[] {
			"{", "className", "ENUMERATED", "{", "enum1", ",", "enum2", "}",
			"OPTIONAL", ",", "secondClassName", "CHOICE", "{", "className",
			"CLASSNAME", ",", "secondClassName", "SECONDCLASSNAME", ",",
			"thirdClass", "THIRDCLASS", "}", ",", "thirdClass", "SEQUENCE",
			"(", "SIZE", "(", "1", "..", "8", ")", ")", "OF", "OCTET STRING",
			"(", "SIZE", "(", "1", "..", "4", ")", ")", "OPTIONAL", "}" };

	protected static final String[] SEQUENCE_WITH_SEQUENCES = new String[] {
			"{", "className", "SEQUENCE", "(", "SIZE", "(", "4", "..", "20",
			")", ")", "OF", "INTEGER", "(", "2", "..", "8", ")", "OPTIONAL",
			",", "secondClassName", "SEQUENCE", "(", "SIZE", "(", "15", "..",
			"80", ")", ")", "OF", "CHOICE", "{", "className", "CLASSNAME", ",",
			"secondClassName", "SECONDCLASSNAME", ",", "thirdClass",
			"THIRDCLASS", "}", "OPTIONAL", ",", "thirdClass", "SEQUENCE", "{",
			"className", "CLASSNAME", ",", "secondClassName",
			"SECONDCLASSNAME", ",", "thirdClass", "THIRDCLASS", "}",
			"OPTIONAL", "}" };

	// integer lexem

	protected static final String[] INTEGER_RANGE = new String[] { "(", "1",
			"..", "45", ")" };

	protected static final String[] INTEGER_LEFT_CONST_RANGE = new String[] {
			"(", "hello", "..", "10", ")" };
	protected static final String[] INTEGER_RIGHT_CONST_RANGE = new String[] {
			"(", "23", "..", "hello", ")" };
	protected static final String[] INTEGER_CONSTS_RANGE = new String[] { "(",
			"left", "..", "right", ")" };
	protected static final String[] INTEGER_ASSIGNMENT = new String[] { "::=",
			"20" };
	protected static final String[] INTEGER_WITH_ONE_MEMBER = new String[] {
			"(", "7", ")" };

	// bit string lexem

	protected static final String[] SIZE = new String[] { "SIZE", "(", "16",
			")" };
	protected static final String[] RANGE_SIZE = new String[] { "SIZE", "(",
			"5", "..", "10", ")" };
	protected static final String[] FIRST_CONST_RANGE_SIZE = new String[] {
			"SIZE", "(", "hi", "..", "10", ")" };
	protected static final String[] SECOND_CONST_RANGE_SIZE = new String[] {
			"SIZE", "(", "14", "..", "hello", ")" };
	protected static final String[] BIT_STRING_WITH_MEMBERS = new String[] {
			"{", "1", "(", "0", ")", ",", "2", "(", "1", ")", ",", "3", "(",
			"2", ")", ",", "4", "(", "3", ")", ",", "5", "(", "4", ")", ",",
			"6", "(", "5", ")", ",", "7", "(", "6", ")", ",", "8", "(", "7",
			")", "}", "(", "SIZE", "(", "8", ")", ")" };
	protected static final String[] BIT_STRING_WITH_MEMBERS_AND_SIZE = ArrayUtils
			.addAll(BIT_STRING_WITH_MEMBERS, SIZE);

	// octet string lexem

	protected static final String[] OCTET_STRING_SIZE = ArrayUtils.addAll(
			new String[] { "(" }, ArrayUtils.add(SIZE, ")"));

	protected static final String[] OCTET_STRING_WITH_MEMBERS = new String[] {
			"{", "1", "(", "0", ")", ",", "2", "(", "1", ")", ",", "3", "(",
			"2", ")", ",", "4", "(", "3", ")", ",", "5", "(", "4", ")", ",",
			"6", "(", "5", ")", ",", "7", "(", "6", ")", ",", "8", "(", "7",
			")", "}", "(", "SIZE", "(", "8", ")", ")" };

	// Choice lexem

	protected static final String[] CHOICE_WITHOUT_END_TOKENS = new String[] {
			"{", "className", "CLASSNAME", ",", "secondClassName" };

	protected static final String[] CHOICE_WITH_UNEXPECTED_END_OF_LEXEM = new String[] {
			"{", "className", "CLASSNAME", ",", "}", "secondClassName" };

	protected static final String[] CHOICE_WITHOUT_START = new String[] {
			"className", "CLASSNAME", ",", "secondClassName", "CLASSNAME", "}" };

	protected static final String[] CHOICE_WITHOUT_CLASS_DESCRIPTION = ArrayUtils
			.addAll(CHOICE_WITHOUT_END_TOKENS, new String[] {
					"SECONDCLASSNAME", "}" });

	protected static final String[] CHOICE_WITH_BIT_STRING_SEQUNECE_OF_AND_INTEGER = ArrayUtils
			.addAll(CHOICE_WITHOUT_END_TOKENS, ArrayUtils
					.addAll(BIT_STRING, ArrayUtils.addAll(new String[] { ",",
							"integer" }, ArrayUtils.addAll(INTEGER, ArrayUtils
							.addAll(new String[] { ",", "sequenceOf",
									"SEQUENCE" }, ArrayUtils.addAll(
									SEQUENCE_OF_WITH_SEQUENCE_OF, "}"))))));

	protected static final String[] CHOICE_WITH_CLASS_DESCRIPTION = ArrayUtils
			.addAll(ArrayUtils.addAll(CHOICE_WITHOUT_END_TOKENS, ENUMERATED),
					ArrayUtils
							.addAll(new String[] { ",", "thirdClassName",
									"THIRDCLASS", ",", "sequence", "SEQUENCE" },
									ArrayUtils
											.addAll(SEQUENCE_WITH_OPTIONAL_VALUES,
													ArrayUtils
															.addAll(new String[] {
																	",",
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

	// file lexem
	protected static final String TEST_RESOURCES = "resources/test_schema/";
	protected static final String TEST_RESOURCES_FILTER = "*.asn";

	protected static final int CLASS_DEFINITIONS = 0;
	protected static final int CONSTANT_DEFINITIONS = 1;
	protected static final int INFORMATION_ELEMENTS = 2;
	protected static final int INTERNODE_DEFINITIONS = 3;
	protected static final int PDU_DEFINITIONS = 4;
}
