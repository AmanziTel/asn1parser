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

package org.amanzi.asn1.parser.coders.decoders;

import org.amanzi.asn1.parser.coders.AbstractAsn1Decoder;
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceOfLexem;
import org.amanzi.asn1.parser.lexer.impl.Size;

/**
 * Basic Encoding Rules (BER) decoder instance
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public class BerDecoder extends AbstractAsn1Decoder {

	@Override
	public BitStringLexem decodeBitString() {
		return null;
	}

	@Override
	public OctetStringLexem decodeOctetString() {
		return null;
	}

	@Override
	public IntegerLexem decodeInteger() {
		return null;
	}

	@Override
	public ChoiceLexem decodeChoice() {
		return null;
	}

	@Override
	public SequenceLexem decodeSequence() {

		return null;
	}

	@Override
	public SequenceOfLexem decodeSequenceOf() {

		return null;
	}

	@Override
	public Enumerated decodeEnumerated() {

		return null;
	}

	@Override
	public Size decodeSize() {

		return null;
	}

}
