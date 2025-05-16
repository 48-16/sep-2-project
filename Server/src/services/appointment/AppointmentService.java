package services.appointment;

import dtos.appointment.AppointmentRequest;
import dtos.appointment.DeleteAppointmentRequest;
import dtos.appointment.GetAppointmentsByDateRequest;
import model.Appointment;

import java.util.List;

public interface AppointmentService {
    void createAppointment(AppointmentRequest appointmentRequest);
    List<Appointment> getAppointmentsByDate(GetAppointmentsByDateRequest getAppointmentsByDateRequest);
    void deleteAppointment(DeleteAppointmentRequest deleteAppointmentRequest);
}
