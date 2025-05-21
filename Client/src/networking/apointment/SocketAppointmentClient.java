package networking.apointment;

import dtos.Request;
import dtos.Response;
import dtos.apointment.AppointmentDto;
import dtos.apointment.AppointmentRequest;
import dtos.apointment.DeleteAppointmentRequest;
import dtos.apointment.GetAppointmentsByDateRequest;
import networking.SocketService;

import java.util.List;

public class SocketAppointmentClient implements AppointmentClient {
    @Override
    public void createAppointment(AppointmentRequest request) {
        Object payload = SocketService.sendRequest(new Request("appointment", "create", request));

        // если всё дошло до сюда — значит был SUCCESS
        if (!(payload instanceof String message) || !message.equals("appointment_created")) {
            throw new RuntimeException("Unexpected payload from server: " + payload);
        }
    }

    @Override
    public void deleteAppointment(DeleteAppointmentRequest request) {
        SocketService.sendRequest(new Request("appointment", "delete", request));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AppointmentDto> getAppointmentsByDate(GetAppointmentsByDateRequest request) {
        return (List<AppointmentDto>) SocketService.sendRequest(new Request("appointment", "get_by_date", request));
    }
}