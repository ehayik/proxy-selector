package org.eljaiek.proxy.select.components;

import java.util.Locale;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 *
 * @author eduardo.eljaiek
 */
@Component
public final class MessageResolver {

    private final ResourceBundleMessageSource messageSource;

    public MessageResolver(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Object... args) {
        final Locale locale = new Locale("es");
        Locale.setDefault(locale);
        return messageSource.getMessage(code, args, locale);
    }

    public String getMessage(String code) {
        return getMessage(code, new Object[]{});
    }

}
