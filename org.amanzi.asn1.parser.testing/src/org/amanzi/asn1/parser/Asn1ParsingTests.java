package org.amanzi.asn1.parser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite to run all tests on ASN1 parsing
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * 
 */

@RunWith(Suite.class)
@SuiteClasses({
		org.amanzi.asn1.parser.lexer.internal.BitStringLexemLogicTest.class,
		org.amanzi.asn1.parser.lexer.internal.ClassDefinitionLogicTest.class,
		org.amanzi.asn1.parser.lexer.internal.ChoiceLexemLogicTest.class,
		org.amanzi.asn1.parser.lexer.internal.EnumeratedLogicTest.class,
		org.amanzi.asn1.parser.lexer.internal.IntegerLexemLogicTest.class,
		org.amanzi.asn1.parser.lexer.internal.OctetStringLexemLogicTest.class,
		org.amanzi.asn1.parser.lexer.internal.SizeLexemLogicTest.class,
		org.amanzi.asn1.parser.lexer.internal.SequenceLexemLogicTest.class,
		org.amanzi.asn1.parser.lexer.ranges.impl.RangeStorageTest.class,
		org.amanzi.asn1.parser.lexer.ranges.impl.RangeTest.class,
		org.amanzi.asn1.parser.token.TokenAnalyzerTest.class })
public class Asn1ParsingTests {

}
