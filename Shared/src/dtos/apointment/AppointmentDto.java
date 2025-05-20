package dtos.apointment;

import dtos.user.UserDataDto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public record AppointmentDto(int id, Date date, Time time,
                             UserDataDto barber, UserDataDto client, double price) implements Serializable {}
