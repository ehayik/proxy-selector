package org.eljaiek.proxy.select.components;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author eduardo.eljaiek
 */
public class ViewManager implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(ViewManager.class);

    private final String mainView;

    private final Image appIcon;

    private Stage primaryStage;

    private Stage currentStage;

    private ApplicationContext appContext;

    @Autowired
    private FXMLLoader loader;

    public ViewManager(String mainView, Image appIcon) {
        this.mainView = mainView;
        this.appIcon = appIcon;
    }

    public void init(Stage stage) {

        try {
            loader.setLocation(ViewManager.class.getResource(mainView));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.getIcons().add(appIcon);
            stage.setScene(scene);
            primaryStage = stage;
            currentStage = stage;
            primaryStage.show();
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public void modal(Window owner, String title, String view) {
        modal(owner, true, title, view);
    }

    public void modal(Window owner, boolean resizable, String title, String view) {
        Parent parent = load(view).get();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.getIcons().add(appIcon);
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.setOnCloseRequest(evt -> {
            currentStage = primaryStage;
        });
        currentStage = stage;
        stage.showAndWait();
    }

    public void close() {
        currentStage.close();
        currentStage = primaryStage;
    }

    public Optional<Stage> getView() {
        return Optional.ofNullable(currentStage);
    }

    private Optional<Parent> load(String view) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ViewManager.class.getResource(view));
            fxmlLoader.setControllerFactory(param -> appContext.getBean(param));
            return Optional.of(fxmlLoader.load());
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.appContext = ac;
    }
}
