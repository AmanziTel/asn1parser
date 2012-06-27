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

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.amanzi.asn1.parser.TestUtils;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
import org.amanzi.asn1.parser.lexer.exception.SyntaxException;
import org.amanzi.asn1.parser.lexer.impl.AbstractFileLexem;
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.ClassReference;
import org.amanzi.asn1.parser.lexer.impl.ConstantDefinition;
import org.amanzi.asn1.parser.lexer.impl.ConstantFileLexem;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.FileLexem;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.Schema;
import org.amanzi.asn1.parser.lexer.impl.SequenceLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceOfLexem;
import org.amanzi.asn1.parser.token.TokenAnalyzer;
import org.amanzi.asn1.parser.token.impl.ReservedWord;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.Platform;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.osgi.framework.Bundle;

/**
 * Tests for {@link FileLexemLogic}
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class FileLexemLogicTest {

	private static final int SKIPING_SYMBOLS_LENGTH = 4;
	private static final int CLASS_DEFINITIONS_IMPORTS_PD_LENGTH = 80;
	private static final int CLASS_DEFINITIONS_IMPORTS_IE_LENGTH = 1;
	private static final int CLASS_DEFINITIONS_CLASSES_LENGTH = 21;
	private static final int INFORMATION_ELEMENTS_IMPORTS_LENGTH = 148;
	private static final int INFORMATION_ELEMENTS_CLASSES_LENGTH = 2699;
	private static final int CONSTANT_DEFINITIONS_CONST_COUNT = 151;
	private static final int INTERNODE_DEFINITIONS_IMPORTS_CD_LENGTH = 10;
	private static final int INTERNODE_DEFINITIONS_IMPORTS_IE_LENGTH = 140;
	private static final int INTERNODE_DEFINITIONS_IMPORTS_PD_LENGTH = 10;
	private static final int INTERNODE_DEFINITIONS_CLASSES_LENGTH = 127;
	private static final int PDU_DEFINITIONS_IMPORTS_IE_LENGTH = 450;
	private static final int PDU_DEFINITIONS_IMPORTS_CD_LENGTH = 2;
	private static final int PDU_DEFINITIONS_CLASSES_LENGTH = 426;
	private static final int SCHEMA_FILES_SIZE = 5;

	private static Map<String, URL> resources = new HashMap<String, URL>(0);

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setupBeforeClass() {
		Bundle testPluginBundle = Platform
				.getBundle(TestUtils.TEST_PLUGIN_NAME);
		Enumeration<URL> testResources = testPluginBundle.findEntries(
				TokenStreamsData.FILE_TEST_RESOURCES,
				TokenStreamsData.FILE_TEST_RESOURCES_FILTER, false);
		Pattern pattern = Pattern.compile("([a-z]|[A-Z]|[0-9]|-|_)+.asn");
		while (testResources.hasMoreElements()) {
			URL resource = testResources.nextElement();
			Matcher matcher = pattern.matcher(resource.getFile());
			if (matcher.find()) {
				String fileName = matcher.group();
				resources.put(
						fileName.substring(0, fileName.length()
								- SKIPING_SYMBOLS_LENGTH), resource);
			}
		}
	}

	@Test
	public void testExpectedClassDefinitionsResult() throws Exception {
		List<String> fileImports = Arrays.asList(
				TokenStreamsData.PDU_DEFINITIONS_NAME,
				TokenStreamsData.INFORMATION_ELEMENTS_NAME);

		FileLexem lexem = parseFileLexem(TokenStreamsData.CLASS_DEFINITIONS_NAME);
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_NAME + lexem.getName(),
				TokenStreamsData.CLASS_DEFINITIONS_NAME, lexem.getName());
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, lexem.getImports());

		for (String importedFile : lexem.getImports().keySet()) {
			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + importedFile,
					fileImports.contains(importedFile));
		}
		verifyImportsReferences(lexem.getImports().get(fileImports.get(0)),
				CLASS_DEFINITIONS_IMPORTS_PD_LENGTH);
		verifyImportsReferences(lexem.getImports().get(fileImports.get(1)),
				CLASS_DEFINITIONS_IMPORTS_IE_LENGTH);
		verifyParsedFileClassDefinitions(lexem,
				CLASS_DEFINITIONS_CLASSES_LENGTH);
	}

	@Test
	public void testExpectedInformationElementsResult() throws Exception {
		List<String> fileImports = Arrays
				.asList(TokenStreamsData.CONSTANT_DEFINITIONS_NAME);

		FileLexem lexem = parseFileLexem(TokenStreamsData.INFORMATION_ELEMENTS_NAME);
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_NAME + lexem.getName(),
				TokenStreamsData.INFORMATION_ELEMENTS_NAME, lexem.getName());
		assertNotNull("imports cannot be null", lexem.getImports());

		for (String importedFile : lexem.getImports().keySet()) {
			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + importedFile,
					fileImports.contains(importedFile));
		}
		verifyImportsReferences(lexem.getImports().get(fileImports.get(0)),
				INFORMATION_ELEMENTS_IMPORTS_LENGTH);
		verifyParsedFileClassDefinitions(lexem,
				INFORMATION_ELEMENTS_CLASSES_LENGTH);
	}

	@Test
	public void testExpectedConstantDefinitionsResult() throws Exception {
		ConstantFileLexem lexem = parseConstantFileLexem();
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_NAME + lexem.getName(),
				TokenStreamsData.CONSTANT_DEFINITIONS_NAME, lexem.getName());

		assertFalse(TokenStreamsData.VALUE_CANNOT_BE_EMPTY, lexem
				.getConstants().isEmpty());
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE
				+ lexem.getConstants().size(),
				CONSTANT_DEFINITIONS_CONST_COUNT, lexem.getConstants().size());
		for (ConstantDefinition definition : lexem.getConstants()) {
			assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, definition);
			assertEquals(TokenStreamsData.UNEXPECTED_VALUE
					+ definition.getDescription().getType(),
					ClassDescriptionType.INTEGER, definition.getDescription()
							.getType());
			assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL,
					definition.getName());
		}
	}

	@Test
	public void testExpectedInternodeDefinitionsResult() throws Exception {
		List<String> fileImports = Arrays.asList(
				TokenStreamsData.CONSTANT_DEFINITIONS_NAME,
				TokenStreamsData.INFORMATION_ELEMENTS_NAME,
				TokenStreamsData.PDU_DEFINITIONS_NAME);
		FileLexem lexem = parseFileLexem(TokenStreamsData.INTERNODE_DEFINITIONS_NAME);
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_NAME + lexem.getName(),
				TokenStreamsData.INTERNODE_DEFINITIONS_NAME, lexem.getName());
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, lexem.getImports());

		for (String importedFile : lexem.getImports().keySet()) {
			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + importedFile,
					fileImports.contains(importedFile));
		}
		verifyDescriptions(lexem.getFileClassDefinitions());
		verifyImportsReferences(lexem.getImports().get(fileImports.get(0)),
				INTERNODE_DEFINITIONS_IMPORTS_CD_LENGTH);
		verifyImportsReferences(lexem.getImports().get(fileImports.get(1)),
				INTERNODE_DEFINITIONS_IMPORTS_IE_LENGTH);
		verifyImportsReferences(lexem.getImports().get(fileImports.get(2)),
				INTERNODE_DEFINITIONS_IMPORTS_PD_LENGTH);
		verifyParsedFileClassDefinitions(lexem,
				INTERNODE_DEFINITIONS_CLASSES_LENGTH);
	}

	@Test
	public void testExpectedPduDefinitionsResult() throws Exception {
		List<String> fileImports = Arrays.asList(
				TokenStreamsData.INFORMATION_ELEMENTS_NAME,
				TokenStreamsData.CONSTANT_DEFINITIONS_NAME);
		FileLexem lexem = parseFileLexem(TokenStreamsData.PDU_DEFINITIONS_NAME);
		assertNotNull(TokenStreamsData.LEXEM_CANNOT_BE_NULL, lexem);
		assertEquals(TokenStreamsData.UNEXPECTED_NAME + lexem.getName(),
				TokenStreamsData.PDU_DEFINITIONS_NAME, lexem.getName());
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, lexem.getImports());

		for (String importedFile : lexem.getImports().keySet()) {
			assertTrue(importedFile != null && !importedFile.isEmpty());
			assertTrue(TokenStreamsData.UNEXPECTED_VALUE + importedFile,
					fileImports.contains(importedFile));
		}
		verifyDescriptions(lexem.getFileClassDefinitions());
		verifyImportsReferences(lexem.getImports().get(fileImports.get(0)),
				PDU_DEFINITIONS_IMPORTS_IE_LENGTH);
		verifyImportsReferences(lexem.getImports().get(fileImports.get(1)),
				PDU_DEFINITIONS_IMPORTS_CD_LENGTH);
		verifyParsedFileClassDefinitions(lexem, PDU_DEFINITIONS_CLASSES_LENGTH);
	}

	@Test
	public void testExpectedSchemaParsedFiles() throws Exception {
		Schema schema = new Schema();
		assertNotNull(schema);
		assertTrue(schema.getFiles().isEmpty());
		schema.addFile(parseFileLexem(TokenStreamsData.CLASS_DEFINITIONS_NAME));
		schema.addFile(parseConstantFileLexem());
		schema.addFile(parseFileLexem(TokenStreamsData.INFORMATION_ELEMENTS_NAME));
		schema.addFile(parseFileLexem(TokenStreamsData.INTERNODE_DEFINITIONS_NAME));
		schema.addFile(parseFileLexem(TokenStreamsData.PDU_DEFINITIONS_NAME));
		assertTrue(!schema.getFiles().isEmpty());
		assertEquals(SCHEMA_FILES_SIZE, schema.getFiles().size());
		String[] fileNames = new String[] {
				TokenStreamsData.CLASS_DEFINITIONS_NAME,
				TokenStreamsData.CONSTANT_DEFINITIONS_NAME,
				TokenStreamsData.INFORMATION_ELEMENTS_NAME,
				TokenStreamsData.INTERNODE_DEFINITIONS_NAME,
				TokenStreamsData.PDU_DEFINITIONS_NAME };
		byte counter = 0;
		for (AbstractFileLexem file : schema.getFiles()) {
			assertEquals(fileNames[counter++], file.getName());
		}

	}

	@Test
	public void checkThrowingExceptionWhenRemoveTokenFromStream() {
		exception.expect(UnsupportedOperationException.class);
		exception
				.expectMessage(containsString(ErrorReason.STREAM_DOESNT_SUPPORT_REMOVE_OPERATION
						.getMessage()));

		TokenAnalyzer tokenAnalyzer = new TokenAnalyzer(
				IOUtils.toInputStream(ReservedWord.BEGIN.getTokenText()));
		tokenAnalyzer.remove();
	}

	private void verifyDescriptions(Iterable<ClassDefinition> definitions) {
		for (ClassDefinition definition : definitions) {
			IClassDescription description = definition.getClassDescription();
			assertNotNull(description);
			switch (description.getType()) {
			case BIT_STRING:
				BitStringLexem bitString = (BitStringLexem) description;
				assertNotNull(bitString.getMembers());
				assertNotNull(bitString.getSize());
				break;
			case CHOICE:
				ChoiceLexem choiceLexem = (ChoiceLexem) description;
				assertNotNull(choiceLexem.getMembers());
				break;
			case ENUMERATED:
				Enumerated enumerated = (Enumerated) description;
				assertNotNull(enumerated.getMembers());
				break;
			case INTEGER:
				IntegerLexem integer = (IntegerLexem) description;
				assertNotNull(integer.getRange());
				assertNotNull(integer.getSize());
				break;
			case OCTET_STRING:
				OctetStringLexem octetStringLexem = (OctetStringLexem) description;
				assertNotNull(octetStringLexem.getMembers());
				assertNotNull(octetStringLexem.getSize());
				break;
			case SEQUENCE:
				SequenceLexem sequenceLexem = (SequenceLexem) description;
				assertNotNull(sequenceLexem.getMembers());
				break;
			case SEQUENCE_OF:
				SequenceOfLexem sequenceOfLexem = (SequenceOfLexem) description;
				assertNotNull(sequenceOfLexem.getClassReference());
				assertNotNull(sequenceOfLexem.getSize());
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Verify references size and name's
	 * 
	 * @param references
	 * @param count
	 */
	private void verifyImportsReferences(Set<ClassReference> references,
			int count) {
		assertFalse(TokenStreamsData.VALUE_CANNOT_BE_EMPTY,
				references.isEmpty());
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE + references.size(),
				count, references.size());
		for (ClassReference reference : references) {
			assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL,
					reference.getName());
		}
	}

	/**
	 * Verify file class definitions
	 * 
	 * @param lexemo
	 * @param count
	 */
	private void verifyParsedFileClassDefinitions(FileLexem lexem, int count) {
		assertFalse(TokenStreamsData.VALUE_CANNOT_BE_EMPTY, lexem
				.getFileClassDefinitions().isEmpty());
		assertEquals(TokenStreamsData.UNEXPECTED_VALUE
				+ lexem.getFileClassDefinitions().size(), count, lexem
				.getFileClassDefinitions().size());
		for (ClassDefinition definition : lexem.getFileClassDefinitions()) {
			assertTrue(TokenStreamsData.VALUE_CANNOT_BE_NULL,
					definition.getClassName() != null
							&& !definition.getClassName().isEmpty());
			assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL,
					definition.getClassDescription());
		}
	}

	private FileLexem parseFileLexem(String name) throws SyntaxException,
			IOException {
		TokenAnalyzer analyzer = getAnalyzedFileStream(name);
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL + " Check index.",
				analyzer);
		FileLexemLogic logic = new FileLexemLogic(analyzer);
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, logic);
		return logic.parse(new FileLexem());
	}

	private ConstantFileLexem parseConstantFileLexem() throws SyntaxException,
			IOException {
		TokenAnalyzer analyzer = getAnalyzedFileStream(TokenStreamsData.CONSTANT_DEFINITIONS_NAME);
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL
				+ " Check File index.", analyzer);
		ConstantFileLexemLogic logic = new ConstantFileLexemLogic(analyzer);
		assertNotNull(TokenStreamsData.VALUE_CANNOT_BE_NULL, logic);
		return logic.parse(new ConstantFileLexem());
	}

	/**
	 * Get file stream
	 * 
	 * @param file
	 *            file index in resources list
	 * @return {@link TokenAnalyzer}
	 * @throws IOException
	 */
	private TokenAnalyzer getAnalyzedFileStream(String name) throws IOException {
		URL testResource = resources.get(name);
		return new TokenAnalyzer(testResource.openStream());
	}
}
