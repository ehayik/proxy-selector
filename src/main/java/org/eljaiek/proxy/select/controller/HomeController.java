package org.eljaiek.proxy.select.controller;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import org.controlsfx.control.StatusBar;
import org.eljaiek.proxy.select.model.ProxyModel;
import org.eljaiek.proxy.select.components.AlertManager;
import org.eljaiek.proxy.select.components.MessageResolver;
import org.eljaiek.proxy.select.components.ViewManager;
import org.eljaiek.proxy.select.domain.DProxy;
import org.eljaiek.proxy.select.model.ProxyModelMapper;
import org.eljaiek.proxy.select.services.ConnectionService;
import org.eljaiek.proxy.select.services.ProxyService;
import org.reactfx.util.FxTimer;
import org.reactfx.util.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

@Controller
public final class HomeController implements Initializable {

    private static final String PROXY_VIEW = "/org/proxy/select/views/ProxyView.fxml";

    private static final String SETTINGS_VIEW = "/org/proxy/select/views/Settings.fxml";

    private static final String ABOUT_VIEW = "/org/proxy/select/views/AboutBox.fxml";

    @Autowired
    private AlertManager alertManager;

    @Autowired
    private ViewManager viewManager;

    @Autowired
    private MessageResolver messages;

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private Environment env;

    @Autowired
    private ProxyModelMapper proxyModelMapper;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<ProxyModel> proxiesTableView;

    @FXML
    private StatusBar statusBar;

    private final Timer pingTimer;

    private final ImageView startIcon = new ImageView("/org/proxy/select/assets/find24.png");

    private final ImageView cancelIcon = new ImageView("/org/proxy/select/assets/stop24.png");
    
    private final AtomicBoolean isPingTimerBusy = new AtomicBoolean(false);

    public HomeController() {
        this.pingTimer = FxTimer.createPeriodic(Duration.ofMillis(1000), this::startPing);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        proxiesTableView
                .getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);
        refreshProxiesList();
    }

    @FXML
    void aboutBox(ActionEvent event) {
        viewManager.modal(viewManager.getView().get(),
                true,
                messages.getMessage("about.view.title"),
                ABOUT_VIEW);
    }

    @FXML
    void close(ActionEvent event) {
        pingTimer.stop();
        viewManager.close();
    }

    @FXML
    void settings(ActionEvent event) {
        viewManager.modal(viewManager.getView().get(),
                messages.getMessage("settings.view.title"),
                SETTINGS_VIEW);
    }

    @FXML
    void newProxy(ActionEvent event) {
        viewManager.modal(viewManager.getView().get(),
                messages.getMessage("proxy.view.title"),
                PROXY_VIEW);
        refreshProxiesList();
        saveChanges();
    }

    @FXML
    void removeProxy(ActionEvent event) {
        final ObservableList<ProxyModel> items = proxiesTableView.
                getSelectionModel().
                getSelectedItems();

        if (!items.isEmpty()) {
            alertManager.confirmation(viewManager.getView().get(),
                    messages.getMessage("proxy.confirm.alert.header"),
                    messages.getMessage("proxy.confirm.alert.message"),
                    () -> removeProxies(items));
        }
    }

    @FXML
    void search(ActionEvent event) {
        searchButton.setGraphic(cancelIcon);
        searchButton.setOnAction(evt -> stopPing());
        statusBar.setText(messages.getMessage("home.statusBar.text"));
        statusBar.setProgress(-1);
        viewManager.getView().get().setIconified(true);
        pingTimer.restart();
    }

    private void stopPing() {
        pingTimer.stop();
        isPingTimerBusy.set(false);
        onSearchCancelled();
    }

    private void startPing() {
        
        if (isPingTimerBusy.get()) {
            return;
        }
        
        isPingTimerBusy.set(true);
        proxiesTableView.getItems().forEach(proxyModel -> {
            updateProgressMessage(proxyModel.getHostPort());
            final DProxy proxy = proxyModelMapper.asDProxy(proxyModel);
            final boolean isConnected = connectionService.ping(proxy);

            if (isConnected && !proxyModel.isConnected()) {
                alertOnConnectionSucceeded(proxyModel.getName());
            } else if (!isConnected && proxyModel.isConnected()) {
                alertOnConnectionFailed(proxyModel.getName());
            }
            
            proxyModel.setConnected(isConnected);
        });
    }

    private void updateProgressMessage(String proxyName) {
        final String message = messages.getMessage(
                "home.conn.progress.text", proxyName
        );
        statusBar.setText(message);
    }

    private void alertOnConnectionSucceeded(String proxyName) {
        final String header = messages.getMessage("home.conn.success.header");
        final String message = messages.getMessage("home.conn.success.message",
                proxyName); 
        alertManager.traySuccess(header, message);            
    }

    private void alertOnConnectionFailed(String proxyName) {
        final String header = messages.getMessage("home.conn.fail.header");
        final String message = messages.getMessage("home.conn.fail.message",
                proxyName);
        alertManager.trayError(header, message);             
    }

    private void onSearchCancelled() {
        statusBar.setText("");
        statusBar.setProgress(0);
        searchButton.setGraphic(startIcon);
        searchButton.setOnAction(this::search);
    }

    private void removeProxies(List<ProxyModel> proxyModels) {
        proxyModels.forEach(pm -> {
            proxyService.remove(pm.getHost(), pm.getPort());
        });

        refreshProxiesList();
        saveChanges();
    }

    private void refreshProxiesList() {
        List<ProxyModel> proxyModels = proxyService
                .list()
                .stream()
                .map(proxyModelMapper::asProxyModel)
                .collect(Collectors.toList());
        proxiesTableView.setItems(FXCollections.observableArrayList(proxyModels));
    }

    private void saveChanges() {
        String dataFile = env.getProperty("app.data.file");
        proxyService.exportToJson(dataFile);
    }
}
