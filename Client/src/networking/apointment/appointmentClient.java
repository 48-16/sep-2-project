package networking.apointment;

import dtos.apointment.AppointmentDto;
import dtos.apointment.AppointmentRequest;
import dtos.apointment.DeleteAppointmentRequest;
import dtos.apointment.GetAppointmentsByDateRequest;

import java.util.List;

public interface AppointmentClient {
    void createAppointment(AppointmentRequest request);
    void deleteAppointment(DeleteAppointmentRequest request);
    List<AppointmentDto> getAppointmentsByDate(GetAppointmentsByDateRequest request);
}
