package org.eljaiek.proxy.select.services;

import java.util.prefs.Preferences;
import org.eljaiek.proxy.select.domain.DSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author eduardo.eljaiek
 */
@Service
public final class PreferencesService {

    private static final Logger LOG = LoggerFactory.getLogger(PreferencesService.class);

    private static final String URL_KEY = "connect.url";

    private static final String URL_DEFAULT = "http://www.google.com";

    private static final String TIMEOUT_KEY = "connect.timeout";

    public static final long TIMEOUT_DEFAULT = 30;

    private final Preferences prefs;

    public PreferencesService() {
        final Preferences prefsRoot = Preferences.userRoot();
//
//        try {
//
//            if (!prefsRoot.nodeExists(PreferencesService.class.getName())) {
//                init(prefsRoot);
//            }
//
//        } catch (BackingStoreException ex) {
//            LOG.error(ex.getMessage(), ex);
//        }

        prefs = prefsRoot.node(PreferencesService.class.getName());
    }

    public final DSettings load() {
        return new DSettings(prefs.get(URL_KEY, URL_DEFAULT),
                prefs.getLong(TIMEOUT_KEY, TIMEOUT_DEFAULT));
    }

    public final void save(DSettings settings) {
        prefs.put(URL_KEY, settings.getUrl());
        prefs.putLong(TIMEOUT_KEY, settings.getTimeout());
    }

//    private static void init(Preferences root) {
//        final Preferences pref = root.node(PreferencesService.class.getName());
//        pref.put(URL_KEY, URL_DEFAULT);
//        pref.put(TIMEOUT_KEY, TIMEOUT_DEFAULT);
//    }
}
