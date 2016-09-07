
package org.eljaiek.proxy.select.services;

/**
 *
 * @author eljaiek
 */
public class DuplicateProxyException extends RuntimeException {

    /**
     * Creates a new instance of <code>DuplicateProxyException</code> without
     * detail message.
     */
    public DuplicateProxyException() {
    }

    /**
     * Constructs an instance of <code>DuplicateProxyException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DuplicateProxyException(String msg) {
        super(msg);
    }
}
