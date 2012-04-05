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

package org.amanzi.asn1.parser.lexer.exception;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public enum ErrorReason {
    
    UNEXPECTED_END_OF_STREAM("Stream was finished without End Element for a Lexem"),
    NO_START_TOKEN("Start token doesn't found"),
    UNEXPECTED_ESCAPE_TOKEN("Escape token can't be used in this Lexem"),
    NO_SEPARATOR("No separator between tokens"),
    TOKEN_NOT_SUPPORTED("Token not supported by Lexem"),
    UNEXPECTED_END_OF_LEXEM("Trailing Token of Lexem was found before finishing of this Lexem"),
    UNEXPECTED_TOKEN_IN_LEXEM("Unexpected token at position in current Lexem state"),
    CONSTANT_NOT_FOUND("Constant not found"),
    INCORRECT_RANGE_BOUND("Incorrect range bounds");
    
    private String message;
    
    private ErrorReason(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

}
