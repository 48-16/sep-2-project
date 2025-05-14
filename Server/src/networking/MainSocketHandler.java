package networking;

import dtos.Response;
import networking.requestHandler.*;
import startup.ServiceProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainSocketHandler implements Runnable {

    private final List<RequestHandler> handlers = new ArrayList<>();

    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;



    public MainSocketHandler(Socket socket, ServiceProvider provider) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        handlers.add(new ProductRequestHandler(provider.getProductService()));
        handlers.add(new UserRequestHandler(provider.getUserService()));
        handlers.add(new RevenueRequestHandler(provider.getRevenueService()));
        handlers.add(new AppointmentRequestHandler(provider.getAppointmentService()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object request = in.readObject();
                handleRequest(request);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected or error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private void handleRequest(Object request) throws IOException {
        for (RequestHandler handler : handlers) {
            if (handler.canHandle(request)) {
                out.writeObject(handler.handle(request));
                return;
            }
        }
        out.writeObject(new Response("error", "No handler found for request type: " + request.getClass().getSimpleName()));
    }
}