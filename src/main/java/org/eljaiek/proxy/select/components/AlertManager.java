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
import javafx.util.Duration;
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
    
    private static final String AUDIO_ERROR = "/org/proxy/select/assets/error.wav";

    public void traySuccess(String title, String message) {
        showTrayAlert(new TrayAlertCommand(
                title, 
                message, 
                AUDIO_ALERT, 
                NotificationType.SUCCESS
        ));      
    }

    public void trayError(String title, String message) {
        showTrayAlert(new TrayAlertCommand(
                title,
                message,
                AUDIO_ERROR,
                NotificationType.ERROR
        ));
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

    private void showTrayAlert(TrayAlertCommand alertCommand) {
        Media media = new Media(AlertManager.class.getResource(alertCommand.audioFilePath).toString());
        MediaPlayer mp = new MediaPlayer(media);
        TrayNotification tray = new TrayNotification(
                alertCommand.title,
                alertCommand.message,
                alertCommand.notificationType
        );
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.millis(3000));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> mp.play());
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

    private class TrayAlertCommand {

        private final String title;

        private final String message;

        private final String audioFilePath;

        private final NotificationType notificationType;

        public TrayAlertCommand(String title, String message, String audioFilePath, NotificationType notificationType) {
            this.title = title;
            this.message = message;
            this.audioFilePath = audioFilePath;
            this.notificationType = notificationType;
        }

        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }

        public String getAudioFilePath() {
            return audioFilePath;
        }

        public NotificationType getNotificationType() {
            return notificationType;
        }
    }
}
