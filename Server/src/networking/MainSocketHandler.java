package networking;

import dtos.Request;
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
        handlers.add(new AuthenticationRequestHandler(provider.getUserService()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object request = in.readObject();
                handleRequest(request);
            }
        }
            catch (Exception e) {
                System.out.println("Client disconnected or server error:");
                e.printStackTrace();
            }
        finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private void handleRequest(Object request) throws IOException {
        if (!(request instanceof Request wrapper)) {
            out.writeObject(Response.error("Invalid request format: expected Request object"));
            return;
        }

        for (RequestHandler handler : handlers) {
            if (handler.canHandle(wrapper.payload())) {
                out.writeObject(handler.handle(wrapper.payload()));
                return;
            }
        }
        out.writeObject(Response.error("No handler found for request type: " + wrapper.payload().getClass().getSimpleName()));
    }
}