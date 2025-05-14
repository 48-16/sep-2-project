package services.appointment;

import dtos.apointment.AppointmentRequest;
import dtos.apointment.DeleteAppointmentRequest;
import dtos.apointment.GetAppointmentsByDateRequest;
import model.Appointment;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    void createAppointment(AppointmentRequest appointmentRequest);
    List<Appointment> getAppointmentsByDate(GetAppointmentsByDateRequest getAppointmentsByDateRequest);
    void deleteAppointment(DeleteAppointmentRequest deleteAppointmentRequest);
}
