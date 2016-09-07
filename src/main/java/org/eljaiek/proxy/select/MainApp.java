package org.eljaiek.proxy.select;

import com.gluonhq.ignite.spring.SpringContext;
import java.util.Arrays;
import javafx.application.Application;
import javafx.stage.Stage;
import org.eljaiek.proxy.select.components.ViewManager;
import org.springframework.beans.factory.annotation.Autowired;
import static javafx.application.Application.launch;
import javafx.stage.WindowEvent;
import org.eljaiek.proxy.select.services.ProxyService;
import org.springframework.core.env.Environment;


public class MainApp extends Application {
    
    private final SpringContext context = new SpringContext(this, () -> Arrays.asList("org.eljaiek.proxy.select"));
   
    @Autowired
    private ViewManager viewManager;
    
    @Autowired
    ProxyService proxyService;
    
    @Autowired
    private Environment env;
    
    @Override
    public void start(Stage stage) throws Exception {      
        context.init();   
        String dataFile = env.getProperty("app.data.file");
        proxyService.importFromJson(dataFile);
        stage.setTitle(getTitle());   
        stage.setOnCloseRequest((WindowEvent evt) -> {
            proxyService.exportToJson(dataFile);
        });
        
        viewManager.init(stage);
    }
    
    private String getTitle() {
        return env.getProperty("app.title").
                concat(" ").
                concat(env.getProperty("app.version"));
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {      
        launch(args);
    }

}
