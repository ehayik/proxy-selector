package org.eljaiek.proxy.select.services;

import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import org.eljaiek.proxy.select.domain.DSettings;
import org.springframework.stereotype.Service;

/**
 *
 * @author eduardo.eljaiek
 */
@Service
public final class PreferenceService {
    
    private static final String URL_KEY = "page.url";

    private static final String URL_DEFAULT = "http://www.google.com";

    private static final String TIMEOUT_KEY = "connect.timeout";

    public static final long TIMEOUT_DEFAULT = 30;
    
    private static final String TIMEUNIT_KEY = "connect.timeout.unit";
    
    private static final String TIMEUNIT_DEFAULT = TimeUnit.SECONDS.toString();
    
    private static final String TITLE_KEY = "page.title";
    
    private static final String TITLE_DEFAULT = "Google";

    private final Preferences prefs;

    public PreferenceService() {
        final Preferences prefsRoot = Preferences.userRoot();
        prefs = prefsRoot.node(PreferenceService.class.getName());
    }

    public final DSettings load() {        
        return DSettings
                .builder()
                .url(prefs.get(URL_KEY, URL_DEFAULT))
                .timeout(prefs.getLong(TIMEOUT_KEY, TIMEOUT_DEFAULT))
                .timeoutUnit(TimeUnit.valueOf(prefs.get(TIMEUNIT_KEY, TIMEUNIT_DEFAULT)))
                .pageTitle(prefs.get(TITLE_KEY, TITLE_DEFAULT))
                .build();
    }

    public final void save(DSettings settings) {
        prefs.put(URL_KEY, settings.getUrl());
        prefs.putLong(TIMEOUT_KEY, settings.getTimeout());
        prefs.put(TIMEUNIT_KEY, settings.getTimeoutUnit().toString());
        prefs.put(TITLE_KEY, settings.getPageTitle());
    }
}
