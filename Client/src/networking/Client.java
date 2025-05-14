package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client
{
  public static void main(String[] args)
  {
    String hostname = "localhost";
    int port = 8888;

    try (Socket socket = new Socket(hostname, port)){
      BufferedReader input =  new BufferedReader(new InputStreamReader(System.in));
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      System.out.println("Connected to Server. Type messages");

      String userInput;
      while((userInput = input.readLine()) !=null) {
        out.println(userInput);
      String response = in.readLine();
        System.out.println("Server says: " + response);

      if ("exit".equalsIgnoreCase(userInput)) {break;}
    }
  }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
