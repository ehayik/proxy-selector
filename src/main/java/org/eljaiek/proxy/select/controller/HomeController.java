package org.eljaiek.proxy.select.controller;

import java.net.URL;
import javafx.concurrent.Service;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import org.eljaiek.proxy.select.domain.DSettings;
import org.eljaiek.proxy.select.services.ConnectionService;
import org.eljaiek.proxy.select.services.PreferencesService;
import org.eljaiek.proxy.select.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController implements Initializable {
    
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
    private ConnectionService connService;
    
    @Autowired
    private PreferencesService prefService;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private TableView<ProxyModel> proxiesTableView;
    
    @FXML
    private StatusBar statusBar;
    
    private final ImageView startIcon = new ImageView("/org/proxy/select/assets/find24.png");
    
    private final ImageView cancelIcon = new ImageView("/org/proxy/select/assets/stop24.png");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        proxiesTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        refreshList();
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
        refreshList();
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
                    () -> {
                        items.forEach(pm -> {
                            proxyService.remove(pm.getHost(), pm.getPort());
                        });
                        
                        refreshList();
                    });
        }
    }
    
    @FXML
    void search(ActionEvent event) {
        statusBar.setText(messages.getMessage("home.statusBar.text"));
        statusBar.setProgress(-1);
        viewManager.getView().get().setIconified(true);
        final SearchService service = new SearchService();
        service.setOnCancelled(evt -> searchCancelled());
        service.setOnSucceeded(evt -> {
            searchCancelled();
            alertManager.traySuccess(messages.getMessage("home.conn.success.header"),
                    messages.getMessage("home.conn.success.message", evt.getSource().getValue()));
            
        });
        
        searchButton.setGraphic(cancelIcon);
        searchButton.setOnAction(evt -> service.cancel());
        service.messageProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            statusBar.setText(newValue);
        });
        
        service.start();
    }
    
    private void searchCancelled() {
        statusBar.setText("");
        statusBar.setProgress(0);
        searchButton.setGraphic(startIcon);
        searchButton.setOnAction(this::search);
    }
    
    private void refreshList() {
        final List<ProxyModel> list = ProxyModel.toList(proxyService.list());
        proxiesTableView.setItems(FXCollections.observableArrayList(list));
    }
    
    class SearchService extends Service<String> {
        
        private final AtomicBoolean found;
        
        public SearchService() {
            this.found = new AtomicBoolean();
        }
        
        @Override
        public boolean cancel() {
            found.set(true);
            return super.cancel();
        }
        
        @Override
        protected Task<String> createTask() {
            return new Task<String>() {
                @Override
                protected String call() throws Exception {
                    
                    while (!found.get()) {
                        final DSettings settings = prefService.load();
                        
                        for (ProxyModel pm : proxiesTableView.getItems()) {
                            updateMessage(messages.getMessage("home.conn.progress.text", pm.getHostPort()));
                            
                            if (connService.ping(settings.getUrl(), settings.getTimeout(), pm.getProxy())) {
                                found.set(true);
                                return pm.getHostPort();
                            }
                        }
                    }
                    
                    return null;
                }
            };
        }
    }
}
