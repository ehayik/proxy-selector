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
import org.controlsfx.validation.Validator;
import org.eljaiek.proxy.select.util.ValidationUtils;
import org.eljaiek.proxy.select.components.AlertManager;
import org.eljaiek.proxy.select.components.MessageResolver;
import org.eljaiek.proxy.select.components.ViewManager;
import org.eljaiek.proxy.select.domain.DProxy;
import org.eljaiek.proxy.select.services.DuplicateProxyException;
import org.eljaiek.proxy.select.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author eduardo.eljaiek
 */
@Controller
public class ProxyController implements Initializable {

    private static final String DEF_PORT = "80";

    @FXML
    private TextField nameField;

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private Button okButton;

    @Autowired
    private MessageResolver messages;

    private final ProxyService proxyService;

    private final ViewManager viewManager;

    @Autowired
    private AlertManager alertManager;

    private final ValidationSupport validationSupport = new ValidationSupport();

    public ProxyController(ProxyService proxyService, ViewManager viewManager) {
        this.proxyService = proxyService;
        this.viewManager = viewManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        portField.setTextFormatter(new LongTextFomatter());
        portField.setText(DEF_PORT);

        validationSupport.registerValidator(nameField, true,
                Validator.createEmptyValidator(messages.getMessage("field.required")));

        validationSupport.registerValidator(portField, true,
                Validator.createEmptyValidator(messages.getMessage("field.required")));

        validationSupport.registerValidator(hostField, true, (Control t, String value) -> {
            return ValidationResult.fromMessageIf(t, messages.getMessage("proxy.host.invalid"),
                    Severity.ERROR,
                    !ValidationUtils.isValidHost(value));
        });

        validationSupport.invalidProperty()
                .addListener((obs, oldValue, newValue) -> okButton.setDisable(newValue));

    }

    @FXML
    void save(ActionEvent event) {

        try {
            proxyService.add(new DProxy(
                    nameField.getText(),
                    hostField.getText(),
                    Integer.parseInt(portField.getText())
            ));
            viewManager.close();
        } catch (DuplicateProxyException ex) {
            alertManager.error(viewManager.getView().get(),
                    messages.getMessage("proxy.error.alert.header"),
                    messages.getMessage("proxy.error.alert.message"));
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        viewManager.close();
    }
}
