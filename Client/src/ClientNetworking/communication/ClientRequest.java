package ClientNetworking.communication;

import dtos.Response;

import java.io.IOException;

public class ClientRequest {

  private final ClientCommunication communication;

  public ClientRequest(ClientCommunication communication) {
    this.communication = communication;
  }

  public Response send(Object request) throws IOException, ClassNotFoundException {
    return communication.sendRequest(request);
  }
}
