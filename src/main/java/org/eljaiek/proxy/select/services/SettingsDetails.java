package org.eljaiek.proxy.select.services;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author eduardo.eljaiek
 */
@Data
@Builder
public class SettingsDetails implements Serializable {

    private String url;

    private String pageTitle;

    private long timeout;

    private TimeUnit timeoutUnit;
}
