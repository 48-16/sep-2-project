package networking.requestHandler;

import dtos.Response;
import dtos.appointment.AppointmentRequest;
import dtos.appointment.DeleteAppointmentRequest;
import dtos.appointment.GetAppointmentsByDateRequest;
import dtos.model.Appointment;
import services.appointment.AppointmentService;

import java.util.List;

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
            appointmentService.createAppointment(appointmentRequest);
            return new Response("appointment_created", null);
        } else if (request instanceof DeleteAppointmentRequest deleteAppointmentRequest) {
            appointmentService.deleteAppointment(deleteAppointmentRequest);
            return new Response("appointment_deleted", deleteAppointmentRequest.id());
        } else if (request instanceof GetAppointmentsByDateRequest getAppointmentsByDateRequest) {
            List<Appointment> appointments = appointmentService.getAppointmentsByDate(getAppointmentsByDateRequest);
            return new Response("appointments_by_date", appointments);
        }

        return new Response("error", "Unknown appointment request");
    }
}