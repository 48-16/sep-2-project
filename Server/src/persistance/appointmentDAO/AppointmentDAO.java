package persistance.appointmentDAO;

import dtos.model.Appointment;

import java.util.Date;
import java.util.List;

public interface AppointmentDAO {
    public Appointment createAppointment(Appointment appointment);
    public void deleteAppointment(int id);
    public List<Appointment> getAllAppointmentsByDate(Date date);
}
