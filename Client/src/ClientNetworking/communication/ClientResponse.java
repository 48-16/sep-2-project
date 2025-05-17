package ClientNetworking.communication;

import dtos.Response;

public class ClientResponse {

  private final Response response;

  public ClientResponse(Response response) {
    this.response = response;
  }

  public String getStatus() {
    return response.status();
  }

  public Object getPayload() {
    return response.payload();
  }

  public boolean isSuccess() {
    return !"error".equalsIgnoreCase(response.status());
  }
}
