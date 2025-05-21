package services.appointment;

import dtos.apointment.AppointmentDto;
import dtos.apointment.AppointmentRequest;
import dtos.apointment.DeleteAppointmentRequest;
import dtos.apointment.GetAppointmentsByDateRequest;
import dtos.user.UserDataDto;
import model.Appointment;
import model.User;
import persistance.appointmentDAO.AppointmentDAO;
import persistance.userDAO.UserDAOImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentDAO appointmentDAO;

    public AppointmentServiceImpl(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    @Override
    public void createAppointment(AppointmentRequest appointmentRequest) {
        try {
            // Get a default barber (first barber in the system)
            UserDAOImpl userDAO = new UserDAOImpl();
            User defaultBarber = userDAO.getUserById(1); // Assuming barber has ID 1
            if (defaultBarber == null) {
                throw new RuntimeException("No barber found in the system");
            }

            User client = userDAO.getUserById(appointmentRequest.client().getId());
            if (client == null) {
                throw new RuntimeException("Client not found");
            }

            Appointment appointment = new Appointment( appointmentRequest.id(),
                    appointmentRequest.date(),
                    appointmentRequest.time(),
                    defaultBarber,
                    client,
                    appointmentRequest.price());
            Appointment createdAppointment = appointmentDAO.createAppointment(appointment);
            if (createdAppointment == null) {
                throw new RuntimeException("Failed to create appointment");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AppointmentDto> getAppointmentsByDate(GetAppointmentsByDateRequest getAppointmentsByDateRequest) {
        Date date = getAppointmentsByDateRequest.date();
        List<Appointment> appointments = appointmentDAO.getAllAppointmentsByDate(date);
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentDtos.add (new AppointmentDto(appointment.getId(),
                    appointment.getDate(),
                    appointment.getTime(),
                    null,
                    new UserDataDto (appointment.getClient()),
                    appointment.getPrice()));
        }

        return appointmentDtos;
    }

    @Override
    public void deleteAppointment(DeleteAppointmentRequest deleteAppointmentRequest) {
        int id = deleteAppointmentRequest.id();
        appointmentDAO.deleteAppointment(id);
    }
}
