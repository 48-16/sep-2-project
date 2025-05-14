package model;

import java.sql.Date;
import java.sql.Time;

public class Appointment {

    private int id;
    private Date date;
    private Time time;
    private User barber;
    private User client;
    private double price;

    public Appointment(int id,
                       Date date,
                       Time time,
                       User barber,
                       User client,
                       double price) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.barber = barber;
        this.client = client;
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public User getBarber() {
        return barber;
    }

    public void setBarber(User barber) {
        this.barber = barber;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}