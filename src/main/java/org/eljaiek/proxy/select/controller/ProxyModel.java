package org.eljaiek.proxy.select.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author eduardo.eljaiek
 */
public class ProxyModel {

    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty host = new SimpleStringProperty();

    private final IntegerProperty port = new SimpleIntegerProperty();

    private final BooleanProperty connected = new SimpleBooleanProperty();

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

    public boolean isConnected() {
        return connected.get();
    }

    public void setConnected(boolean value) {
        connected.set(value);
    }

    public BooleanProperty connectedProperty() {
        return connected;
    }
}
