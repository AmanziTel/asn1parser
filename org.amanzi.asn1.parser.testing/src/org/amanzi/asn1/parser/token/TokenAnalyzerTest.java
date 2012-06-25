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

package org.amanzi.asn1.parser.token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.amanzi.asn1.parser.TestUtils;
import org.amanzi.asn1.parser.token.impl.ControlSymbol;
import org.amanzi.asn1.parser.token.impl.ReservedWord;
import org.amanzi.asn1.parser.token.impl.SimpleToken;
import org.amanzi.asn1.parser.utils.Pair;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.Platform;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

/**
 * Tests for Token Analyser
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class TokenAnalyzerTest {

	private static final String TEST_RESOURCES = "resources/token_analyzer/";

	private static final String TEST_RESOURCES_FILTER = "*.*";

	private static final HashMap<String, Pair<TestResource, TestResource>> RESOURCE_MAP = new HashMap<String, Pair<TestResource, TestResource>>();

	public enum ResourceType {
		INPUT(".input"), OUTPUT(".output");

		private String fileExtension;

		private ResourceType(String fileExtension) {
			this.fileExtension = fileExtension;
		}

		public String cut(String filename) {
			return filename.substring(0,
					filename.length() - fileExtension.length()).substring(
					filename.lastIndexOf("\\") + 1);
		}

		public static ResourceType getByFileName(String fileName) {
			for (ResourceType singleType : values()) {
				if (fileName.endsWith(singleType.fileExtension)) {
					return singleType;
				}
			}

			return null;
		}
	}

	private static class TestResource {

		private URL resource;

		private String name;

		/**
		 * @param resource
		 * @param name
		 */
		public TestResource(String name, URL resource) {
			super();
			this.name = name;
			this.resource = resource;
		}

		/**
		 * @return Returns the resource.
		 */
		public URL getResource() {
			return resource;
		}

		/**
		 * @return Returns the name
		 */
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}

	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Bundle testPluginBundle = Platform
				.getBundle(TestUtils.TEST_PLUGIN_NAME);

		Enumeration<URL> testResources = testPluginBundle.findEntries(
				TEST_RESOURCES, TEST_RESOURCES_FILTER, false);

		while (testResources.hasMoreElements()) {
			URL testResourceUrl = testResources.nextElement();

			ResourceType type = ResourceType.getByFileName(testResourceUrl
					.getPath());

			addToMap(type, type.cut(testResourceUrl.getFile()), testResourceUrl);
		}
	}

	private static void addToMap(ResourceType type, String name, URL url) {
		Pair<TestResource, TestResource> testResource = RESOURCE_MAP.get(name);

		if (testResource == null) {
			testResource = new Pair<TokenAnalyzerTest.TestResource, TokenAnalyzerTest.TestResource>();
			RESOURCE_MAP.put(name, testResource);
		}

		TestResource resource = new TestResource(name, url);
		switch (type) {
		case INPUT:
			testResource.setLeft(resource);
			break;
		case OUTPUT:
			testResource.setRight(resource);
			break;
		}
	}

	@Test
	public void testCheckTokenPatterns() throws Exception {
		for (Pair<TestResource, TestResource> singlePair : RESOURCE_MAP.values()) {
			List<IToken> parsedTokens = getParsedListOfTokens(singlePair
					.getLeft().getResource());
			List<IToken> etalonTokens = getEtalonListOfTokens(singlePair
					.getRight().getResource());

			assertEquals("Unexpected size of Parsed Tokens for Resource <"
					+ singlePair.getRight().getName() + ">",
					etalonTokens.size(), parsedTokens.size());

			for (int i = 0; i < etalonTokens.size(); i++) {
				assertEquals("Unexpected token at position <" + i
						+ "> in Resource <" + singlePair.getRight().getName()
						+ ">", etalonTokens.get(i).getTokenText(), parsedTokens
						.get(i).getTokenText());
			}
		}
	}

	private List<IToken> getParsedListOfTokens(URL inputResource)
			throws IOException {
		List<IToken> tokens = new ArrayList<IToken>();

		TokenAnalyzer analyzer = new TokenAnalyzer(inputResource.openStream());
		while (analyzer.hasNext()) {
			tokens.add(analyzer.next());
		}

		return tokens;
	}

	private List<IToken> getEtalonListOfTokens(URL outputResource)
			throws IOException {
		List<IToken> tokens = new ArrayList<IToken>();

		Scanner scanner = new Scanner(outputResource.openStream());

		while (scanner.hasNextLine()) {
			tokens.add(new SimpleToken(scanner.nextLine()));
		}

		return tokens;
	}

	@Test
	public void checkDynamicToken() {
		TokenAnalyzer tokenAnalyzer = new TokenAnalyzer(
				IOUtils.toInputStream("hallo"));

		assertTrue("This token should be dynamic", tokenAnalyzer.next()
				.isDynamic());
	}

	@Test
	public void checkStaticControlSymbolToken() {
		TokenAnalyzer tokenAnalyzer = new TokenAnalyzer(
				IOUtils.toInputStream(ControlSymbol.ADD.getTokenText()));

		assertFalse("This token should be static", tokenAnalyzer.next()
				.isDynamic());
	}

	@Test
	public void checkStaticReservedWordToken() {
		TokenAnalyzer tokenAnalyzer = new TokenAnalyzer(
				IOUtils.toInputStream(ReservedWord.BEGIN.getTokenText()));

		assertFalse("This token should be static", tokenAnalyzer.next()
				.isDynamic());
	}	
}
