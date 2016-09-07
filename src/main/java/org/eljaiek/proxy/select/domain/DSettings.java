
package org.eljaiek.proxy.select.domain;

import java.io.Serializable;

/**
 *
 * @author eduardo.eljaiek
 */
public class DSettings implements Serializable {
    
    private String url;
    
    private long timeout;

    public DSettings() {
    }

    public DSettings(String url, long timeout) {
        this.url = url;
        this.timeout = timeout;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "DSettings{" + "url=" + url + ", timeout=" + timeout + '}';
    } 
}
