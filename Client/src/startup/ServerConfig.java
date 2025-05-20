package startup;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Handles server configuration for the application.
 * This allows server host and port settings to be configurable.
 */
public class ServerConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static String HOST = "localhost";
    private static int PORT = 8888;
    private static boolean isLoaded = false;

    /**
     * Loads server configuration from properties file.
     * If the file doesn't exist or can't be read, default values are used.
     */
    public static void loadConfiguration() {
        Properties props = new Properties();

        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);

            // Update configuration if properties are found
            HOST = props.getProperty("server.host", HOST);
            PORT = Integer.parseInt(props.getProperty("server.port", String.valueOf(PORT)));

            System.out.println("Server configuration loaded. HOST: " + HOST + ", PORT: " + PORT);
            isLoaded = true;
        } catch (IOException ex) {
            System.out.println("Using default server configuration. HOST: " + HOST + ", PORT: " + PORT);
            // Continue with default values if the file is not found
            isLoaded = true;
        } catch (NumberFormatException ex) {
            System.err.println("Invalid port number in configuration file. Using default: " + PORT);
            isLoaded = true;
        }
    }

    /**
     * Get the configured server host
     * @return Server hostname or IP address
     */
    public static String getHost() {
        ensureLoaded();
        return HOST;
    }

    /**
     * Get the configured server port
     * @return Server port number
     */
    public static int getPort() {
        ensureLoaded();
        return PORT;
    }

    private static void ensureLoaded() {
        if (!isLoaded) {
            loadConfiguration();
        }
    }
}