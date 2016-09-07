package org.eljaiek.proxy.select.components;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.springframework.stereotype.Component;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author eduardo.eljaiek
 */
@Component
public class AlertManager {

    private static final String AUDIO_ALERT = "/org/proxy/select/assets/alert.mp3";

    public void traySuccess(String title, String message) {
        Media media = new Media(AlertManager.class.getResource(AUDIO_ALERT).toString());
        MediaPlayer mp = new MediaPlayer(media);
        TrayNotification tray = new TrayNotification(title, message, NotificationType.SUCCESS);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndWait();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> mp.play());
    }

    public final void error(Window owner, String header, String message) {
        Alert alert = create(AlertType.ERROR, owner, header, message);
        alert.showAndWait();
    }

    public final void confirmation(Window owner, String header, String message, Action action) {
        Alert alert = create(AlertType.CONFIRMATION, owner, header, message);
        Optional<ButtonType> confirm = alert.showAndWait();

        if (ButtonType.OK == confirm.get()) {
            action.call();
        }
    }

    private Alert create(AlertType type, Window owner, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("");
        alert.initOwner(owner);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert;
    }

    @FunctionalInterface
    public interface Action {

        void call();
    }
}
