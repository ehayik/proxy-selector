package org.eljaiek.proxy.select.model;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.eljaiek.proxy.select.domain.DProxy;

/**
 *
 * @author eduardo.eljaiek
 */
public class ProxyModel {

    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty host = new SimpleStringProperty();

    private final IntegerProperty port = new SimpleIntegerProperty();

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public String getHost() {
        return host.get();
    }

    public void setHost(String value) {
        host.set(value);
    }

    public StringProperty hostProperty() {
        return host;
    }

    public int getPort() {
        return port.get();
    }

    public void setPort(int value) {
        port.set(value);
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public String getHostPort() {
        return host.get().concat(":").concat(String.valueOf(port.get()));
    }

    public Proxy getProxy() {        
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host.get(), port.get()));
    }

    public static List<ProxyModel> toList(List<DProxy> proxies) {
        return proxies.stream().map(p -> {
            ProxyModel m = new ProxyModel();
            m.setName(p.getName());
            m.setHost(p.getHost());
            m.setPort(p.getPort());
            return m;
        }).collect(Collectors.toList());
    }
}
