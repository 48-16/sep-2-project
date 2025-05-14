package dtos.apointment;

import java.io.Serializable;
import java.sql.Date;

public record GetAppointmentsByDateRequest(Date date) implements Serializable {
}
