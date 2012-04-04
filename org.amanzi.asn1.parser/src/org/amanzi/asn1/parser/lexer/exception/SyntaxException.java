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
public class SyntaxException extends Exception {

    /** long serialVersionUID field */
    private static final long serialVersionUID = -6365529047191516766L;
    
    private ErrorReason errorReason;

    public SyntaxException(ErrorReason errorReason, String message) {
        super(message);
        this.errorReason = errorReason;
    }
    
    public ErrorReason getReason() {
        return errorReason;
    }
    
    @Override
    public String getMessage() {
        return "Error <" + (errorReason.ordinal() + 1) + ">: " + errorReason.getMessage() + ". " + super.getMessage();
    }
}
