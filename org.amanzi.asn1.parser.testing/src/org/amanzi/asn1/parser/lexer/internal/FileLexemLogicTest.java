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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.amanzi.asn1.parser.TestUtils;
import org.amanzi.asn1.parser.lexer.impl.ClassDefinition;
import org.amanzi.asn1.parser.lexer.impl.ClassReference;
import org.amanzi.asn1.parser.lexer.impl.ConstantDefinition;
import org.amanzi.asn1.parser.lexer.impl.ConstantFileLexem;
import org.amanzi.asn1.parser.lexer.impl.FileLexem;
import org.amanzi.asn1.parser.lexer.impl.IClassDescription.ClassDescriptionType;
import org.amanzi.asn1.parser.token.TokenAnalyzer;
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
	private static List<String> fileImports;

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
		TokenAnalyzer analyzer = getAnalyzedFileStream(TokenStreamsData.CONSTANT_DEFINITIONS);
		assertNotNull("analyzer cannot be null. Check File index.", analyzer);
		ConstantFileLexemLogic logic = new ConstantFileLexemLogic(analyzer);
		assertNotNull("logic object cannot be null", logic);

		ConstantFileLexem lexem = logic.parse(new ConstantFileLexem());
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

	// TODO CONTAINING Lexem doesn't exist
	// @Test
	// public void testExpectedInternodeDefinitionsResult() throws Exception {
	// fileImports = Arrays.asList("Constant-definitions");
	//
	// FileLexem lexem = parseFileLexem(TokenStreamsData.INTERNODE_DEFINITIONS);
	// assertNotNull("parsed file lexem cannot be null", lexem);
	// assertEquals("unexpected file name", "InformationElements",
	// lexem.getName());
	// assertNotNull("imports cannot be null", lexem.getImports());
	//
	// for (String importedFile : lexem.getImports().keySet()) {
	// assertTrue("unexpected imported file: " + importedFile,
	// fileImports.contains(importedFile));
	// }
	// verifyImportsReferences(lexem.getImports().get(fileImports.get(0)), 148);
	// verifyParsedFileClassDefinitions(lexem, 2699);
	// }

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
			assertNotNull("definition name cannot be null : " + definition,
					definition.getClassName());
			assertNotNull("definition description cannot be null",
					definition.getClassDescription());
		}
	}

	private FileLexem parseFileLexem(int fileIndex) throws Exception {
		TokenAnalyzer analyzer = getAnalyzedFileStream(fileIndex);
		assertNotNull("analyzer cannot be null. Check File index.", analyzer);
		FileLexemLogic logic = new FileLexemLogic(analyzer);
		assertNotNull("logic object cannot be null", logic);
		// while (analyzer.hasNext()) {
		// System.out.println(analyzer.next());
		// }
		return logic.parse(new FileLexem());
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
