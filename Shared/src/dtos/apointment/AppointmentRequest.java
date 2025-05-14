package dtos.apointment;

import model.User;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public record AppointmentRequest(int id, Date date, Time time, User barber, User client, double price) implements Serializable {
}
