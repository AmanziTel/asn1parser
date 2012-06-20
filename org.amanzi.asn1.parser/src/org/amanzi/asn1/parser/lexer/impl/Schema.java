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

package org.amanzi.asn1.parser.lexer.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema lexem
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class Schema implements ILexem {

	private List<FileLexem> files;
	private String name;

	public Schema() {
		files = new ArrayList<FileLexem>();
	}

	/**
	 * Get {@link FileLexem} list for current schema
	 * 
	 * @return {@link FileLexem} list
	 */
	public List<FileLexem> getFiles() {
		return files;
	}

	/**
	 * Add parsed {@link FileLexem} list to current schema
	 * 
	 * @param files
	 *            {@link FileLexem} list
	 */
	public void setFiles(List<FileLexem> files) {
		this.files = files;
	}

	/**
	 * Current schema name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set Schema name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
