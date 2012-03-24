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

package org.amanzi.asn1.parser;


/**
 * General implementation of IStream interface
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public abstract class AbstractStream<T extends Object> implements IStream<T> {
    
    /*
     * Next element of Stream
     */
    private T nextElement;
    
    /**
     * Read Next Element of Stream 
     *
     * @return null if stream empty, otherwise returns next element
     */
    protected abstract T readNextElement(); 

    @Override
    public boolean hasNext() {
        if (nextElement == null) {
            nextElement = readNextElement();
        }
        
        return nextElement != null;
    }

    @Override
    public T next() {
        if (nextElement == null) {
            nextElement = readNextElement();
        }
        
        return nextElement;
    }

    @Override
    public void remove() {
        //LN: 24.03.2012, Stream doesn't support remove operation
        throw new UnsupportedOperationException("Stream doesn't support remove operation");
    }
    
    

}
