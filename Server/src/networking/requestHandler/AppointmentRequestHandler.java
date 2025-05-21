package networking.requestHandler;

import dtos.Response;
import dtos.apointment.AppointmentDto;
import dtos.apointment.AppointmentRequest;
import dtos.apointment.DeleteAppointmentRequest;
import dtos.apointment.GetAppointmentsByDateRequest;
import dtos.user.UserDataDto;
import model.Appointment;
import services.appointment.AppointmentService;

import java.util.List;
import java.util.stream.Collectors;

public class AppointmentRequestHandler implements RequestHandler {

    private final AppointmentService appointmentService;

    public AppointmentRequestHandler(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public boolean canHandle(Object request) {
        return request instanceof AppointmentRequest ||
                request instanceof DeleteAppointmentRequest ||
                request instanceof GetAppointmentsByDateRequest;
    }

    @Override
    public Object handle(Object request) {
        if (request instanceof AppointmentRequest appointmentRequest) {
            try {
                System.out.println("Received appointment request:");
                System.out.println("Date: " + appointmentRequest.date());
                System.out.println("Time: " + appointmentRequest.time());
                System.out.println("Client: " + appointmentRequest.client());
                System.out.println("Barber: " + appointmentRequest.barber());
                System.out.println("Price: " + appointmentRequest.price());

                appointmentService.createAppointment(appointmentRequest);
                return Response.success("appointment_created");

            } catch (Exception e) {
                System.out.println("Error booking appointment:");
                e.printStackTrace();
                return Response.error("Error booking appointment: " +
                        (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
            }
        }

        if (request instanceof DeleteAppointmentRequest deleteAppointmentRequest) {
            try {
                appointmentService.deleteAppointment(deleteAppointmentRequest);
                return Response.success("appointment_deleted");
            } catch (Exception e) {
                return Response.error("Error deleting appointment: " +
                        (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
            }
        }

        if (request instanceof GetAppointmentsByDateRequest getAppointmentsByDateRequest) {
            try {
                List<AppointmentDto> appointments = appointmentService.getAppointmentsByDate(getAppointmentsByDateRequest);
                List<AppointmentDto> appointmentDtos = appointments.stream()
                        .map(appointment -> new AppointmentDto(
                                appointment.id(),
                                appointment.date(),
                                appointment.time(),
                                null,
                                new UserDataDto(
                                        appointment.client().getId(),
                                        appointment.client().getUserName(),
                                        appointment.client().getPassword(),
                                        appointment.client().getFirstName(),
                                        appointment.client().getLastName(),
                                        appointment.client().getPhoneNumber(),
                                        appointment.client().getAddress(),
                                        appointment.client().getEmail(),
                                        appointment.client().getDiscount(),
                                        appointment.client().getUserType()
                                ),
                                appointment.price()
                        ))
                        .collect(Collectors.toList());
                return Response.success(appointmentDtos);
            } catch (Exception e) {
                return Response.error("Error fetching appointments: " +
                        (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
            }
        }

        return Response.error("Unknown appointment request");
    }
}