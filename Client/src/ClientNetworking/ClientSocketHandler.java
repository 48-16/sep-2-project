package ClientNetworking;

import dtos.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketHandler {

  private final Socket socket;
  private final ObjectOutputStream out;
  private final ObjectInputStream in;

  public ClientSocketHandler(String host, int port) throws IOException {
    this.socket = new Socket(host, port);
    this.out = new ObjectOutputStream(socket.getOutputStream());
    this.in = new ObjectInputStream(socket.getInputStream());
  }

  public Response sendRequest(Object request) throws IOException, ClassNotFoundException {
    out.writeObject(request);
    out.flush();
    return (Response) in.readObject();
  }

  public void close() {
    try {
      socket.close();
    } catch (IOException e) {
      System.err.println("Error closing socket: " + e.getMessage());
    }
  }
}
