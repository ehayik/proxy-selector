package org.eljaiek.proxy.select.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.eljaiek.proxy.select.components.ViewManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

/**
 * @author eduardo.eljaiek
 */
@Controller
public class AboutBoxController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(AboutBoxController.class);

    private final ViewManager viewManager;

    @Autowired
    private Environment env;

    public AboutBoxController(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void close(ActionEvent event) {
        viewManager.close();
    }

    @FXML
    void openEmail(ActionEvent event) {
        new FeedbackService().start();
    }

    @FXML
    void openWeb(ActionEvent event) {
        new GotoWebsiteService().start();
    }

    public class FeedbackService extends Service {

        @Override
        protected Task createTask() {
            return new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    try {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.mail(new URI(String.join(":", "mailto", env.getProperty("app.vendor.email"))));
                    } catch (IOException | URISyntaxException ex) {
                        LOG.error(ex.getMessage(), ex);
                    }

                    return null;
                }
            };
        }
    }

    public class GotoWebsiteService extends Service {

        @Override
        protected final Task createTask() {
            return new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    try {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.browse(new URI(env.getProperty("app.website")));
                    } catch (IOException | URISyntaxException ex) {
                        LOG.error(ex.getMessage(), ex);
                    }

                    return null;
                }
            };
        }
    }
}
