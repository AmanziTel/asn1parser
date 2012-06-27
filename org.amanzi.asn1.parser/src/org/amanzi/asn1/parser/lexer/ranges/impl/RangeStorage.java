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

package org.amanzi.asn1.parser.lexer.ranges.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Storage for Ranges (that uses constants in one of bound)
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public final class RangeStorage {

	private Map<String, List<Range>> upperBoundCache = new HashMap<String, List<Range>>();

	private Map<String, List<Range>> lowerBoundCache = new HashMap<String, List<Range>>();

	private RangeStorage() {
		// do nothing
	}

	/**
	 * Hold initialized {@link RangeStorage} instance as a static final field
	 * 
	 * @author Bondoronok_p
	 * @since 1.0.0
	 */
	private static final class RangeStorageHolder {
		public static final RangeStorage INSTANCE = new RangeStorage();

		private RangeStorageHolder() {
		}
	}

	public static RangeStorage getStorage() {
		return RangeStorageHolder.INSTANCE;
	}

	public void addLowerBoundRange(Range range) {
		addToCache(lowerBoundCache, range, range.getLowerBound());
	}

	public void addUpperBoundRange(Range range) {
		addToCache(upperBoundCache, range, range.getUpperBound());
	}

	public void processConstant(String constantName, int value) {
		if (upperBoundCache.containsKey(constantName)) {
			for (Range range : upperBoundCache.get(constantName)) {
				range.setUpperBoundValue(value);
			}
			upperBoundCache.remove(constantName);
		}

		if (lowerBoundCache.containsKey(constantName)) {
			for (Range range : lowerBoundCache.get(constantName)) {
				range.setLowerBoundValue(value);
			}
			lowerBoundCache.remove(constantName);
		}
	}

	private void addToCache(Map<String, List<Range>> cache, Range value,
			String key) {
		List<Range> ranges = cache.get(key);

		if (ranges == null) {
			ranges = new ArrayList<Range>();
			cache.put(key, ranges);
		}

		ranges.add(value);
	}

	public Map<String, List<Range>> getUpperBoundCache() {
		return upperBoundCache;
	}

	public Map<String, List<Range>> getLowerBoundCache() {
		return lowerBoundCache;
	}
	
	

}
