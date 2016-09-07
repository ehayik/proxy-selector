package org.eljaiek.proxy.select.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author eljaiek
 */
public class DProxy implements Serializable {

    private String name;

    private String host;

    private int port;

    public DProxy() {
    }

    public DProxy(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @JsonIgnore
    public String getHostPort() {
        return host.concat(":").concat(String.valueOf(port));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final DProxy other = (DProxy) obj;

        if (this.port != other.port) {
            return false;
        }

        return Objects.equals(this.host, other.host);
    }

    @Override
    public String toString() {
        return "DProxy{" + "name=" + name + ", host=" + host + ", port=" + port + '}';
    }
}
