package startup;

import startup.BarbershopApp;

/**
 * Main launcher class for the application.
 * This class delegates to BarbershopApp for the actual application startup.
 */
public class MainApplication {

    /**
     * Main entry point of the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Launch the application through BarbershopApp
        BarbershopApp.main(args);
    }
}