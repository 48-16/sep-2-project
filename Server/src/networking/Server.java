package networking;

import startup.ServiceProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 8888;
    private final ServiceProvider serviceProvider;

    public Server(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                Thread thread = new Thread(new MainSocketHandler(clientSocket, serviceProvider));
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}
