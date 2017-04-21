package org.eljaiek.proxy.select.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author eduardo.eljaiek
 */
@Service
public final class ConnectionService {

    private static final int OK_CODE = 200;

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionService.class);

    private final PreferencesService preferenceService;

    public ConnectionService(PreferencesService preferenceService) {
        this.preferenceService = preferenceService;
    }

    public final boolean ping() {
        return ping(null);
    }

    public final boolean ping(ProxyDetails proxy) {
        final SettingsDetails settings = preferenceService.load();

        try {
            final Future<Boolean> pingTask = preparePing(proxy, settings);
            return pingTask.get(settings.getTimeout(), settings.getTimeoutUnit());
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            LOG.error(ex.getMessage(), ex);
            return false;
        }
    }

    private Future<Boolean> preparePing(ProxyDetails proxy, SettingsDetails settings) {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(() -> {
            try {
                URLConnection connection = new URL(settings.getUrl())
                        .openConnection(createProxySocket(proxy));

                if (((HttpURLConnection) connection).getResponseCode() != OK_CODE) {
                    return false;
                } else {
                    return assertPageTitle(readPage(connection), settings.getPageTitle());
                }

            } catch (IOException ex) {
                LOG.error(ex.getMessage(), ex);
                return false;
            }
        });
    }

    private String readPage(URLConnection connection) throws IOException {
        String line = null;
        final StringBuffer htmlPage = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        while ((line = in.readLine()) != null) {
            htmlPage.append(line);
        }

        return htmlPage.toString();
    }

    private boolean assertPageTitle(String page, String title) {
        Document document = Jsoup.parse(page);
        return title.equals(document.head().getElementsByTag("title").text());
    }

    private Proxy createProxySocket(ProxyDetails proxy) {
        return proxy != null ? new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress(proxy.getHost(), proxy.getPort()))
                : Proxy.NO_PROXY;
    }
}
