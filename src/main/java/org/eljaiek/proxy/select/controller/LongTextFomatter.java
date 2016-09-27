
package org.eljaiek.proxy.select.controller;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

/**
 *
 * @author eduardo.eljaiek
 */
 final class LongTextFomatter extends TextFormatter<Long> {

    public LongTextFomatter() {
        super(new StringConverter<Long>() {
            @Override
            public String toString(Long number) {
                return String.valueOf(number);
            }

            @Override
            public Long fromString(String text) {
                return Long.parseLong(text);
            }
        });
    }
}
