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

package org.amanzi.asn1.parser.coders;

import org.amanzi.asn1.parser.coders.utils.BitArrayInputStream;

/**
 * General interface for ASN.1 decoders
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public interface IAsn1Decoder {

	/**
	 * Decode message from inputStream
	 * 
	 * @param inputStream
	 *            {@link BitArrayInputStream}
	 * @return Decoded message
	 */
	public <T> T decode(BitArrayInputStream inputStream);
}
