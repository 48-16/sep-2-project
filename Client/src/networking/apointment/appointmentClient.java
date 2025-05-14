package networking.apointment;

import java.time.LocalDate;
import java.util.List;

public class appointmentClient
{
  //make conetction to server ? or something

  public List<appointmentDto> getAppointmentsByDate(LocalDate date) {
    return null;
  }

  public availableTimeSlotsResponse getAvailableTimeSlots(LocalDate date) {
    return null;
  }

  public appointmentDto bookAppointment(bookAppointmentRequest request) {
    return null;
  }

  public boolean cancelAppointment(String appointmentId) {
    return false;
  }

  public boolean deleteTimeSlot(LocalDate date, String timeSlot) {
    return false;
  }

  public boolean deleteDay(LocalDate date) {
    return false;
  }
}