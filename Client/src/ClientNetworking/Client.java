package ClientNetworking;



import dtos.Response;
import dtos.model.User;
import dtos.appointment.AppointmentRequest;
import dtos.appointment.DeleteAppointmentRequest;
import ClientNetworking.appointment.appointmentClient;
import ClientNetworking.authentication.authenticationClient;
import ClientNetworking.communication.ClientCommunicationController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Client {
  public static void main(String[] args) {
    String hostname = "localhost";
    int port = 8888;

    try {
      ClientSocketHandler socketHandler = new ClientSocketHandler(hostname, port);
      ClientCommunicationController communication = new ClientCommunicationController(socketHandler);

      authenticationClient authClient = new authenticationClient(communication);
      appointmentClient apptClient = new appointmentClient(communication);

      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Connected to server. Type a command (login, register, view-appointments, create-appointment, delete-appointment, exit):");

      while (true) {
        System.out.print("> ");
        String command = input.readLine();

        if ("exit".equalsIgnoreCase(command)) {
          System.out.println("Exiting client.");
          break;
        }

        switch (command.toLowerCase()) {
          case "login" -> {
            System.out.print("Username: ");
            String username = input.readLine();
            System.out.print("Password: ");
            String password = input.readLine();
            Response response = authClient.login(username, password);
            System.out.println("Status: " + response.status());
            System.out.println("Payload: " + response.payload());
          }
          case "register" -> {
            System.out.print("Username: ");
            String username = input.readLine();
            System.out.print("Password: ");
            String password = input.readLine();
            System.out.print("Email: ");
            String email = input.readLine();
            System.out.print("Full Name: ");
            String fullName = input.readLine();
            Response response = authClient.register(username, password, email, fullName);
            System.out.println("Status: " + response.status());
            System.out.println("Payload: " + response.payload());
          }
          case "view-appointments" -> {
            System.out.print("Enter date (YYYY-MM-DD): ");
            try {
              LocalDate localDate = LocalDate.parse(input.readLine());

              List<Object> appointments = apptClient.getAppointmentsByDate(localDate);
              System.out.println("Appointments on " + localDate + ":");
              appointments.forEach(System.out::println);
            } catch (DateTimeParseException e) {
              System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
          }
          case "create-appointment" -> {
            try {
              System.out.print("Appointment ID: ");
              int id = Integer.parseInt(input.readLine());

              System.out.print("Date (YYYY-MM-DD): ");
              LocalDate localDate = LocalDate.parse(input.readLine());
              Date date = Date.valueOf(localDate);

              System.out.print("Time (HH:MM:SS): ");
              LocalTime localTime = LocalTime.parse(input.readLine());
              Time time = Time.valueOf(localTime);

              System.out.print("Barber ID: ");
              int barberId = Integer.parseInt(input.readLine());

              System.out.print("Client ID: ");
              int clientId = Integer.parseInt(input.readLine());

              System.out.print("Price: ");
              double price = Double.parseDouble(input.readLine());

              User barber = new User();
              barber.setId(barberId);

              User client = new User();
              client.setId(clientId);

              AppointmentRequest request = new AppointmentRequest(id, date, time, barber, client, price);
              apptClient.createAppointment(request);
              System.out.println("Appointment created.");
            } catch (NumberFormatException e) {
              System.out.println("Invalid number entered. Please try again.");
            } catch (DateTimeParseException e) {
              System.out.println("Invalid date/time format. Please use YYYY-MM-DD for date and HH:MM:SS for time.");
            }
          }
          case "delete-appointment" -> {
            try {
              System.out.print("Appointment ID to delete: ");
              int id = Integer.parseInt(input.readLine());
              DeleteAppointmentRequest request = new DeleteAppointmentRequest(id);
              apptClient.deleteAppointment(request);
              System.out.println("Appointment deleted.");
            } catch (NumberFormatException e) {
              System.out.println("Invalid appointment ID. Please enter a valid number.");
            }
          }
          default -> System.out.println("Unknown command.");
        }
      }

      socketHandler.close();

    } catch (Exception e) {
      System.err.println("Client error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
