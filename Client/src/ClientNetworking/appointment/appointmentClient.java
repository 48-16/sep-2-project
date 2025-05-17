package ClientNetworking.appointment;

import dtos.Response;
import dtos.appointment.AppointmentRequest;
import dtos.appointment.DeleteAppointmentRequest;
import dtos.appointment.GetAppointmentsByDateRequest;
import ClientNetworking.communication.ClientCommunicationController;
import ClientNetworking.communication.ClientRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class appointmentClient {

  private final ClientRequest clientRequest;

  public appointmentClient(ClientCommunicationController communication) {
    this.clientRequest = new ClientRequest(communication);
  }

  @SuppressWarnings("unchecked")
  public List<Object> getAppointmentsByDate(LocalDate date) throws IOException, ClassNotFoundException {
    Response response = clientRequest.send(new GetAppointmentsByDateRequest(java.sql.Date.valueOf(date)));
    return (List<Object>) response.payload();
  }

  public void createAppointment(AppointmentRequest request) throws IOException, ClassNotFoundException {
    clientRequest.send(request);
  }

  public void deleteAppointment(DeleteAppointmentRequest request) throws IOException, ClassNotFoundException {
    clientRequest.send(request);
  }
}
