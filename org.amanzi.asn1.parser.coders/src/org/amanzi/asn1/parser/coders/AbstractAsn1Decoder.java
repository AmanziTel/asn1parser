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
import org.amanzi.asn1.parser.lexer.impl.BitStringLexem;
import org.amanzi.asn1.parser.lexer.impl.ChoiceLexem;
import org.amanzi.asn1.parser.lexer.impl.Enumerated;
import org.amanzi.asn1.parser.lexer.impl.IntegerLexem;
import org.amanzi.asn1.parser.lexer.impl.OctetStringLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceLexem;
import org.amanzi.asn1.parser.lexer.impl.SequenceOfLexem;
import org.amanzi.asn1.parser.lexer.impl.Size;

/**
 * Abstract ASN.1 decoder. Can be inherited by BER, DER and PER-decoders
 * 
 * @author Bondoronok_p
 * @since 1.0.0
 */
public abstract class AbstractAsn1Decoder implements IAsn1Decoder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.amanzi.asn1.parser.coders.IASNDecoder#decode(org.amanzi.asn1.parser
	 * .coders.utils.BitArrayInputStream)
	 */
	@Override
	public <T> T decode(BitArrayInputStream inputStream) {
		return null;
	}

	public abstract BitStringLexem decodeBitString();

	public abstract OctetStringLexem decodeOctetString();

	public abstract IntegerLexem decodeInteger();

	public abstract ChoiceLexem decodeChoice();

	public abstract SequenceLexem decodeSequence();

	public abstract SequenceOfLexem decodeSequenceOf();

	public abstract Enumerated decodeEnumerated();

	public abstract Size decodeSize();
}
