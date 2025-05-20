package startup;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point for the Barbershop Management System application.
 */
public class BarbershopApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize ViewHandler with the primary stage
            ViewHandler viewHandler = ViewHandler.init(primaryStage);

            // Start with the login view
            viewHandler.openView(ViewType.LOGIN);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Main method to launch the JavaFX application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Make sure server connection is properly configured
        ServerConfig.loadConfiguration();

        // Launch the JavaFX application
        launch(args);
    }
}