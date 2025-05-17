package ClientNetworking.communication;

import dtos.Response;
import java.io.IOException;

public interface ClientCommunication
{
  Response sendRequest(Object request) throws IOException, ClassNotFoundException;
}
