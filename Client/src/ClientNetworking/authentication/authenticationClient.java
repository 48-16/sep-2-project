package ClientNetworking.authentication;

import dtos.Response;
import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import ClientNetworking.communication.ClientCommunicationController;
import ClientNetworking.communication.ClientRequest;

import java.io.IOException;

public class authenticationClient {

  private final ClientRequest clientRequest;

  public authenticationClient(ClientCommunicationController communication) {
    this.clientRequest = new ClientRequest(communication);
  }

  public Response login(String username, String password) throws IOException, ClassNotFoundException {
    LoginRequest request = new LoginRequest(username, password);
    return clientRequest.send(request);
  }

  public Response register(String username, String password, String email, String fullName) throws IOException, ClassNotFoundException {
    RegisterUserRequest request = new RegisterUserRequest(username, password, email, fullName);
    return clientRequest.send(request);
  }
}
