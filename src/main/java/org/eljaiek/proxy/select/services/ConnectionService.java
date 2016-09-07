package org.eljaiek.proxy.select.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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

    public final boolean ping(String url, long timeout, Proxy proxy) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> task = executor.submit(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection(proxy);
                return conn.getResponseCode() == OK_CODE;
            } catch (IOException ex) {
                LOG.error(ex.getMessage(), ex);
                return false;
            }
        });

        try {
            return task.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            LOG.error(ex.getMessage(), ex);
            return false;
        }
    }

    public final boolean ping(String url, long timeout) {
        return ping(url, timeout, Proxy.NO_PROXY);
    }
}
