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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.amanzi.asn1.parser.TestUtils;
import org.amanzi.asn1.parser.lexer.exception.ErrorReason;
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

	private static List<URL> resources = new ArrayList<URL>(0);
	private List<String> fileImports;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setupBeforeClass() {
		Bundle testPluginBundle = Platform
				.getBundle(TestUtils.TEST_PLUGIN_NAME);
		Enumeration<URL> testResources = testPluginBundle.findEntries(
				TokenStreamsData.FILE_TEST_RESOURCES,
				TokenStreamsData.FILE_TEST_RESOURCES_FILTER, false);
		while (testResources.hasMoreElements()) {
			resources.add(testResources.nextElement());
		}
	}

	@Test
	public void testExpectedClassDefinitionsResult() throws Exception {
		fileImports = Arrays.asList("PDU-definitions", "InformationElements");

		FileLexem lexem = parseFileLexem(TokenStreamsData.CLASS_DEFINITIONS);
		assertNotNull("parsed file lexem cannot be null", lexem);
		assertEquals("unexpected file name", "Class-definitions",
				lexem.getName());
		assertNotNull("imports cannot be null", lexem.getImports());

		for (String importedFile : lexem.getImports().keySet()) {
			assertTrue("unexpected imported file: " + importedFile,
					fileImports.contains(importedFile));
		}		
		verifyImportsReferences(lexem.getImports().get(fileImports.get(0)), 80);
		verifyImportsReferences(lexem.getImports().get(fileImports.get(1)), 1);
		verifyParsedFileClassDefinitions(lexem, 21);
	}

	@Test
	public void testExpectedInformationElementsResult() throws Exception {
		fileImports = Arrays.asList("Constant-definitions");

		FileLexem lexem = parseFileLexem(TokenStreamsData.INFORMATION_ELEMENTS);
		assertNotNull("parsed file lexem cannot be null", lexem);
		assertEquals("unexpected file name", "InformationElements",
				lexem.getName());
		assertNotNull("imports cannot be null", lexem.getImports());

		for (String importedFile : lexem.getImports().keySet()) {
			assertTrue("unexpected imported file: " + importedFile,
					fileImports.contains(importedFile));
		}		
		verifyImportsReferences(lexem.getImports().get(fileImports.get(0)), 148);
		verifyParsedFileClassDefinitions(lexem, 2699);
	}

	@Test
	public void testExpectedConstantDefinitionsResult() throws Exception {
		ConstantFileLexem lexem = parseConstantFileLexem();
		assertNotNull("parsed file lexem cannot be null", lexem);
		assertEquals("unexpected file name", "Constant-definitions",
				lexem.getName());

		assertFalse("parsed constants cannot be empty", lexem.getConstants()
				.isEmpty());
		assertEquals("unexpected size", 151, lexem.getConstants().size());
		for (ConstantDefinition definition : lexem.getConstants()) {
			assertNotNull("definition cannot be null ", definition);
			assertEquals("definition description cannot be null",
					ClassDescriptionType.INTEGER, definition.getDescription()
							.getType());
			assertNotNull("definition name cannot be null",
					definition.getName());
		}
	}

	@Test
	public void testExpectedInternodeDefinitionsResult() throws Exception {
		fileImports = Arrays.asList("Constant-definitions",
				"InformationElements", "PDU-definitions");
		FileLexem lexem = parseFileLexem(TokenStreamsData.INTERNODE_DEFINITIONS);
		assertNotNull("parsed file lexem cannot be null", lexem);
		assertEquals("unexpected file name", "Internode-definitions",
				lexem.getName());
		assertNotNull("imports cannot be null", lexem.getImports());

		for (String importedFile : lexem.getImports().keySet()) {
			assertTrue("unexpected imported file: " + importedFile,
					fileImports.contains(importedFile));
		}
		verifyDescriptions(lexem.getFileClassDefinitions());
		verifyImportsReferences(lexem.getImports().get(fileImports.get(0)), 10);
		verifyImportsReferences(lexem.getImports().get(fileImports.get(1)), 140);
		verifyImportsReferences(lexem.getImports().get(fileImports.get(2)), 10);
		verifyParsedFileClassDefinitions(lexem, 127);
	}

	@Test
	public void testExpectedPduDefinitionsResult() throws Exception {
		fileImports = Arrays.asList("InformationElements",
				"Constant-definitions");
		FileLexem lexem = parseFileLexem(TokenStreamsData.PDU_DEFINITIONS);
		assertNotNull("parsed lexem cannot be null", lexem);
		assertEquals("PDU-definitions", lexem.getName());
		assertNotNull(lexem.getImports());

		for (String importedFile : lexem.getImports().keySet()) {
			assertTrue(importedFile != null && !importedFile.isEmpty());
			assertTrue("unexpected imported file:" + importedFile,
					fileImports.contains(importedFile));
		}
		verifyDescriptions(lexem.getFileClassDefinitions());
		verifyImportsReferences(lexem.getImports().get(fileImports.get(0)), 450);
		verifyImportsReferences(lexem.getImports().get(fileImports.get(1)), 2);
		verifyParsedFileClassDefinitions(lexem, 426);
	}

	@Test
	public void testExpectedSchemaParsedFiles() throws Exception {
		Schema schema = new Schema();
		assertNotNull(schema);
		assertTrue(schema.getFiles().isEmpty());
		schema.addFile(parseFileLexem(TokenStreamsData.CLASS_DEFINITIONS));
		schema.addFile(parseConstantFileLexem());
		schema.addFile(parseFileLexem(TokenStreamsData.INFORMATION_ELEMENTS));
		schema.addFile(parseFileLexem(TokenStreamsData.INTERNODE_DEFINITIONS));
		schema.addFile(parseFileLexem(TokenStreamsData.PDU_DEFINITIONS));
		assertTrue(!schema.getFiles().isEmpty());
		assertEquals(5, schema.getFiles().size());
		String[] fileNames = new String[] { "Class-definitions",
				"Constant-definitions", "InformationElements",
				"Internode-definitions", "PDU-definitions" };
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
				System.out.println("bitString");
				break;
			case CHOICE:
				ChoiceLexem choiceLexem = (ChoiceLexem) description;
				assertNotNull(choiceLexem.getMembers());
				System.out.println("choice");
				break;
			case ENUMERATED:
				Enumerated enumerated = (Enumerated) description;
				assertNotNull(enumerated.getMembers());
				System.out.println("enumerated");
				break;
			case INTEGER:
				IntegerLexem integer = (IntegerLexem) description;
				assertNotNull(integer.getRange());
				assertNotNull(integer.getSize());
				System.out.println("integer");
				break;
			case OCTET_STRING:
				OctetStringLexem octetStringLexem = (OctetStringLexem) description;
				assertNotNull(octetStringLexem.getMembers());
				assertNotNull(octetStringLexem.getSize());
				System.out.println("octetString");
				break;
			case SEQUENCE:
				SequenceLexem sequenceLexem = (SequenceLexem) description;
				assertNotNull(sequenceLexem.getMembers());
				System.out.println("sequence");
				break;
			case SEQUENCE_OF:
				SequenceOfLexem sequenceOfLexem = (SequenceOfLexem) description;
				assertNotNull(sequenceOfLexem.getClassReference());
				assertNotNull(sequenceOfLexem.getSize());
				System.out.println("sequenceof");
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
		assertFalse("imported classes cannot be empty", references.isEmpty());
		assertEquals("unexpected size", count, references.size());
		for (ClassReference reference : references) {
			assertNotNull("reference name cannot be null", reference.getName());
		}
	}

	/**
	 * Verify file class definitions
	 * 
	 * @param lexemo
	 * @param count
	 */
	private void verifyParsedFileClassDefinitions(FileLexem lexem, int count) {
		assertFalse("parsed classes cannot be empty", lexem
				.getFileClassDefinitions().isEmpty());
		assertEquals("unexpected size", count, lexem.getFileClassDefinitions()
				.size());
		for (ClassDefinition definition : lexem.getFileClassDefinitions()) {
			assertTrue("definition name cannot be null",
					definition.getClassName() != null
							&& !definition.getClassName().isEmpty());
			assertNotNull("definition description cannot be null",
					definition.getClassDescription());
		}
	}

	private FileLexem parseFileLexem(int fileIndex) throws Exception {
		TokenAnalyzer analyzer = getAnalyzedFileStream(fileIndex);
		assertNotNull("analyzer cannot be null. Check File index.", analyzer);
		FileLexemLogic logic = new FileLexemLogic(analyzer);
		assertNotNull("logic object cannot be null", logic);
		return logic.parse(new FileLexem());
	}

	private ConstantFileLexem parseConstantFileLexem() throws Exception {
		TokenAnalyzer analyzer = getAnalyzedFileStream(TokenStreamsData.CONSTANT_DEFINITIONS);
		assertNotNull("analyzer cannot be null. Check File index.", analyzer);
		ConstantFileLexemLogic logic = new ConstantFileLexemLogic(analyzer);
		assertNotNull("logic object cannot be null", logic);
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
	private TokenAnalyzer getAnalyzedFileStream(int file) throws IOException {
		URL testResource = resources.get(file);
		return new TokenAnalyzer(testResource.openStream());
	}
}
