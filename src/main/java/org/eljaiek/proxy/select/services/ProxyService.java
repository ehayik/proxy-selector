package org.eljaiek.proxy.select.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eljaiek.proxy.select.domain.DProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author eljaiek
 */
@Service
public final class ProxyService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProxyService.class);

    private final List<DProxy> proxies = new ArrayList<>();

    private final ObjectMapper jsonMapper;

    public ProxyService(ObjectMapper mapper) {
        this.jsonMapper = mapper;
    }

    public final List<DProxy> list() {
        return proxies;
    }

    public final DProxy add(DProxy proxy) {       

        if (exists(proxy)) {
            throw new DuplicateProxyException();
        }

        proxies.add(proxy);
        return proxy;
    }

    public final void remove(String host, int port) {
        proxies.removeIf(p -> {
            return p.getHost().equals(host) && p.getPort() == port;
        });
    }

    private boolean exists(DProxy proxy) {
        return proxies.stream()
                .findFirst()
                .filter(p -> {
                    return p.equals(proxy);
                }).isPresent();
    }

    public final void exportToJson(String file) {
        try {
            jsonMapper.writeValue(new File(file), proxies);
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public final List<DProxy> importFromJson(String file) {
       
        try {
            DProxy[] arr = jsonMapper.readValue(new File(file), DProxy[].class);
            proxies.clear();
            proxies.addAll(Arrays.asList(arr));
        } catch (IOException ex) {
           LOG.error(ex.getMessage(), ex);
        }  
        
        return proxies;
    }
}
