package dtos.apointment;

import dtos.user.UserDataDto;
import model.User;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public record AppointmentRequest(int id, Date date, Time time,
                                 UserDataDto barber, UserDataDto client, double price) implements Serializable {
}
