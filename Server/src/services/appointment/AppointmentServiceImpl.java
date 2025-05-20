package services.appointment;

import dtos.apointment.AppointmentRequest;
import dtos.apointment.DeleteAppointmentRequest;
import dtos.apointment.GetAppointmentsByDateRequest;
import model.Appointment;
import model.User;
import persistance.appointmentDAO.AppointmentDAO;
import persistance.userDAO.UserDAOImpl;

import java.util.Date;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentDAO appointmentDAO;

    public AppointmentServiceImpl(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    @Override
    public void createAppointment(AppointmentRequest appointmentRequest) {
        Appointment appointment = new Appointment( appointmentRequest.id(),
                                                    appointmentRequest.date(),
                                                    appointmentRequest.time(),
                                                    null,
                                                    new UserDAOImpl().getUserById( appointmentRequest.id()),
                                                    appointmentRequest.price());
        appointmentDAO.createAppointment(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByDate(GetAppointmentsByDateRequest getAppointmentsByDateRequest) {
        Date date = getAppointmentsByDateRequest.date();
        List<Appointment> appointments = appointmentDAO.getAllAppointmentsByDate(date);

        return appointments;
    }

    @Override
    public void deleteAppointment(DeleteAppointmentRequest deleteAppointmentRequest) {
        int id = deleteAppointmentRequest.id();
        appointmentDAO.deleteAppointment(id);
    }
}
