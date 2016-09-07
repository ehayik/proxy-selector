package org.eljaiek.proxy.select.util;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.UrlValidator;

/**
 *
 * @author eduardo.eljaiek
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isValidHost(String host) { 
        return "localhost".equals(host) ||
                DomainValidator.getInstance().isValid(host) ||
                InetAddressValidator.getInstance().isValid(host);
    }

    public static boolean isValidUrl(String url) {
        return UrlValidator.getInstance().isValid(url);
    }  
}
