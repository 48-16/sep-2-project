package ClientNetworking.communication;

import dtos.Response;
import ClientNetworking.ClientSocketHandler;

import java.io.IOException;

public class ClientCommunicationController implements ClientCommunication
{

  private final ClientSocketHandler socketHandler;

  public ClientCommunicationController(ClientSocketHandler socketHandler) {
    this.socketHandler = socketHandler;
  }

  @Override
  public Response sendRequest(Object request) throws IOException, ClassNotFoundException {
    return socketHandler.sendRequest(request);
  }
}
