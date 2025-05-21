package networking;

import dtos.Request;
import dtos.Response;
import networking.requestHandler.*;
import startup.ServiceProvider;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainSocketHandler implements Runnable {

    private final Socket socket;
    private final ServiceProvider serviceProvider;

    public MainSocketHandler(Socket socket, ServiceProvider serviceProvider) {
        this.socket = socket;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void run() {
        System.out.println("Client connected: " + socket.getInetAddress());

        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            while (true) {
                try {
                    Object received = in.readObject();
                    if (!(received instanceof Request request)) {
                        out.writeObject(Response.error("Invalid request format"));
                        continue;
                    }

                    RequestHandler handler = serviceProvider.getHandler(request.handler());

                    if (handler == null) {
                        out.writeObject(Response.error("No handler found for: " + request.handler()));
                        continue;
                    }

                    if (!handler.canHandle(request.payload())) {
                        out.writeObject(Response.error("Handler cannot handle payload type: " + request.payload().getClass().getSimpleName()));
                        continue;
                    }

                    Object result = handler.handle(request.payload());
                    if (!(result instanceof Response response)) {
                        out.writeObject(Response.error("Handler returned invalid result"));
                    } else {
                        System.out.println("Sending response to client: " + result);
                        out.writeObject(response);
                    }

                } catch (EOFException eof) {
                    System.out.println("Client disconnected gracefully.");
                    break; // client closed connection
                } catch (Exception e) {
                    System.out.println("Client error:");
                    e.printStackTrace();
                    out.writeObject(Response.error("Server error: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName())));
                }
            }

        } catch (IOException e) {
            System.out.println("Could not open streams or client disconnected abruptly: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Connection closed: " + socket.getInetAddress());
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}