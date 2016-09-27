package org.eljaiek.proxy.select.domain;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author eduardo.eljaiek
 */
public class DSettings implements Serializable {

    private String url;

    private String pageTitle;

    private long timeout;

    private TimeUnit timeoutUnit;

    public DSettings() {
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

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public TimeUnit getTimeoutUnit() {
        return timeoutUnit;
    }

    public void setTimeoutUnit(TimeUnit timeoutUnit) {
        this.timeoutUnit = timeoutUnit;
    }

    @Override
    public String toString() {
        return "DSettings{" + "url=" + url + ", timeout=" + timeout + '}';
    }

    public static DSettingsBuilder build() {
        return new DSettingsBuilder();
    }

    public static class DSettingsBuilder {

        final DSettings settings = new DSettings();

        private DSettingsBuilder() {
        }

        public DSettingsBuilder url(String url) {
            settings.url = url;
            return DSettingsBuilder.this;
        }

        public DSettingsBuilder pageTitle(String pageTitle) {
            settings.pageTitle = pageTitle;
            return DSettingsBuilder.this;
        }

        public DSettingsBuilder timeout(long timeout) {
            settings.timeout = timeout;
            return DSettingsBuilder.this;
        }

        public DSettingsBuilder timeoutUnit(TimeUnit timeUnit) {
            settings.timeoutUnit = timeUnit;
            return DSettingsBuilder.this;
        }

        public DSettings get() {
            return settings;
        }
    }
}
