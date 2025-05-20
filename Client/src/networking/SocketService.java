package networking;

import dtos.ErrorResponse;
import dtos.Request;
import dtos.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketService {

    private static final String HOST = "localhost";
    private static final int PORT = 8888;

    public static Object sendRequest(Request request) {
        try (
                Socket socket = new Socket(HOST, PORT);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            outputStream.writeObject(request);
            Response response = (Response) inputStream.readObject();

            switch (response.status()) {
                case "SUCCESS" -> {
                    return response.payload();
                }
                case "ERROR" -> {
                    if (response.payload() instanceof ErrorResponse err) {
                        throw new RuntimeException(err.errorMessage());
                    } else {
                        throw new RuntimeException("Server returned error status, but payload was not ErrorResponse");
                    }
                }
                default -> throw new RuntimeException("Unknown server status code: " + response.status());
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not connect to server: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid response from server.", e);
        }
    }
}
