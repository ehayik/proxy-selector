package org.eljaiek.proxy.select.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.eljaiek.proxy.select.util.ValidationUtils;
import org.eljaiek.proxy.select.components.MessageResolver;
import org.eljaiek.proxy.select.components.ViewManager;
import org.eljaiek.proxy.select.domain.DSettings;
import org.eljaiek.proxy.select.services.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author eduardo.eljaiek
 */
@Controller
public class SettingsController implements Initializable {
    
    private final PreferenceService prefService;

    private final ViewManager viewManager;

    @Autowired
    private MessageResolver messages;

    @FXML
    private TextField urlField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField timeoutField;

    @FXML
    private ComboBox<TimeUnitModel> timeUnitBox;

    @FXML
    private Button okButton;

    private final ValidationSupport validationSupport = new ValidationSupport();

    public SettingsController(PreferenceService prefService, ViewManager viewManager) {
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
        
        timeoutField.setTextFormatter(new LongTextFomatter());
        timeUnitBox.setItems(FXCollections.observableArrayList(
                new TimeUnitModel(messages.getMessage("timeUnit.MILLISECONDS"), TimeUnit.MILLISECONDS),
                new TimeUnitModel(messages.getMessage("timeUnit.SECONDS"), TimeUnit.SECONDS),
                new TimeUnitModel(messages.getMessage("timeUnit.MINUTES"), TimeUnit.MINUTES)               
        ));      
        
        DSettings settings = prefService.load();
        timeoutField.setText(String.valueOf(settings.getTimeout()));
        urlField.setText(settings.getUrl());
        titleField.setText(settings.getPageTitle());
        setSelectedTimeUnit(settings.getTimeoutUnit());
    }

    @FXML
    void cancel(ActionEvent event) {
        viewManager.close();
    }

    @FXML
    void save(ActionEvent event) {        
        prefService.save(DSettings
                .builder()
                .url(urlField.getText())
                .pageTitle(titleField.getText())
                .timeout(Long.parseLong(timeoutField.getText())) 
                .timeoutUnit(timeUnitBox.getSelectionModel().getSelectedItem().timeUnit)
                .build());
        viewManager.close();
    }
    
    private void setSelectedTimeUnit(TimeUnit timeUnit) {
        final TimeUnitModel selectedTimeUnit = timeUnitBox
                .getItems()
                .stream()
                .filter(tm -> tm.timeUnit == timeUnit)
                .findFirst()
                .get();
        timeUnitBox.getSelectionModel().select(selectedTimeUnit);
    }
    
    private class TimeUnitModel {
        
        private final String label;
                
        private final TimeUnit timeUnit;

        public TimeUnitModel(String label, TimeUnit timeUnit) {
            this.label = label;
            this.timeUnit = timeUnit;
        }    

        @Override
        public String toString() {
            return label;
        } 
    }    
}
