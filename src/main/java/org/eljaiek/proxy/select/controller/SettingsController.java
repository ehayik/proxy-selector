package org.eljaiek.proxy.select.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.eljaiek.proxy.select.util.ValidationUtils;
import org.eljaiek.proxy.select.components.MessageResolver;
import org.eljaiek.proxy.select.components.ViewManager;
import org.eljaiek.proxy.select.domain.DSettings;
import org.eljaiek.proxy.select.services.PreferencesService;
import static org.eljaiek.proxy.select.services.PreferencesService.TIMEOUT_DEFAULT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author eduardo.eljaiek
 */
@Controller
public class SettingsController implements Initializable {

    private final PreferencesService prefService;

    private final ViewManager viewManager;

    @Autowired
    private MessageResolver messages;

    @FXML
    private TextField urlField;

    @FXML
    private Spinner<Long> timeoutField;

    @FXML
    private Button okButton;

    private final ValidationSupport validationSupport = new ValidationSupport();

    public SettingsController(PreferencesService prefService, ViewManager viewManager) {
        this.prefService = prefService;
        this.viewManager = viewManager;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        validationSupport.registerValidator(urlField, true, (Control t, String value) -> {
            return ValidationResult.fromMessageIf(t, messages.getMessage("settings.url.invalid"),
                    Severity.ERROR,
                    !ValidationUtils.isValidUrl(value));
        });
        validationSupport.invalidProperty()
                .addListener((obs, oldValue, newValue) -> okButton.setDisable(newValue));

        timeoutField.setValueFactory(new SpinnerValueFactory<Long>() {
            @Override
            public void decrement(int steps) {

                if (getValue() == 1) {
                    setValue(TIMEOUT_DEFAULT);
                }

                setValue(timeoutField.getValue() - steps);
            }

            @Override
            public void increment(int steps) {
                setValue(timeoutField.getValue() + steps);
            }
        });

        timeoutField.getValueFactory().setConverter(new StringConverter<Long>() {
            @Override
            public String toString(Long object) {
                return String.valueOf(object);
            }

            @Override
            public Long fromString(String string) {

                try {
                    return Long.parseLong(string);
                } catch (NumberFormatException e) {
                    return TIMEOUT_DEFAULT;
                }
            }
        });

        DSettings prefs = prefService.load();
        timeoutField.getValueFactory().setValue(prefs.getTimeout());
        urlField.setText(prefs.getUrl());
    }

    @FXML
    void cancel(ActionEvent event) {
        viewManager.close();
    }

    @FXML
    void save(ActionEvent event) {
        DSettings prefs = new DSettings(urlField.getText(),
                timeoutField.getValueFactory().getValue());
        prefService.save(prefs);
        viewManager.close();
    }
}
