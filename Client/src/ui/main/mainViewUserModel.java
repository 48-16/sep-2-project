package ui.main;

import dtos.appointment.AppointmentRequest;
import dtos.auth.UserDataDto;
import dtos.model.Appointment;
import dtos.model.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ClientNetworking.appointment.appointmentClient;
import utils.ErrorPopUp;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class mainViewUserModel {
  private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();
  private final StringProperty selectedTimeSlot = new SimpleStringProperty();
  private final appointmentClient appointmentService;
  private final User currentUser;
  private ErrorPopUp errorPopUp = new ErrorPopUp();

  private final Map<String, Boolean> timeSlotAvailability = new HashMap<>();

  private static final String[] TIME_SLOTS = {
      "8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00",
      "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00"
  };

  public mainViewUserModel(
      appointmentClient appointmentService, User currentUser) {
    this.appointmentService = appointmentService;
    this.currentUser = currentUser;

    for (String slot : TIME_SLOTS) {
      timeSlotAvailability.put(slot, true);
    }

    selectedDate.addListener((Observable, oldDate, newDate) -> {
      if (newDate != null) {
        updateAvailableTimeSlots(newDate);
      }
    });
  }

  private void updateAvailableTimeSlots(LocalDate date) {
    try {
      // Step 1: Get appointments for the day
      List<Object> appointments =  appointmentService.getAppointmentsByDate(date);


      // Step 2: Convert to booked times
      Set<LocalTime> bookedSlots = appointments.stream()
          .map(a -> a.getTime().toLocalTime())
          .collect(Collectors.toSet());


      // Step 3: Fill availability
      for (int i = 0; i < TIME_SLOTS.length; i++) {
        LocalTime slot = TIME_SLOTS[i];
        timeSlotAvailability.put(slot, !bookedSlots.contains(slot));
      }

    } catch (Exception e) {
      errorPopUp.show("Error", "Failed to receive available time slots: " + e.getMessage());
    }
  }


  public boolean bookAppointment() {
    if (selectedDate.get() == null || selectedTimeSlot.get() == null || selectedTimeSlot.get().isEmpty()) {
      errorPopUp.show("Error", "Please select a date and time slot");
      return false;
    }

    try {
      Date sqlDate = Date.valueOf(selectedDate.get());
      Time sqlTime = Time.valueOf(LocalTime.parse(selectedTimeSlot.get()));

      // Convert UserDataDto -> User
      User user = new User(currentUser.getId(), currentUser.getUserName(), currentUser.getFirstName(),currentUser.getLastName(),
          currentUser.getEmail());

      AppointmentRequest request = new AppointmentRequest(
          0, sqlDate, sqlTime, user, user, 50.0
      );

      appointmentService.createAppointment(request);

      errorPopUp.showSucces("Success", "Appointment booked successfully!");
      updateAvailableTimeSlots(selectedDate.get());
      return true;

    } catch (Exception e) {
      errorPopUp.show("Error", "Error booking appointment: " + e.getMessage());
      return false;
    }
  }



  public boolean isTimeSlotAvailable(String timeSlot) {
    return timeSlotAvailability.getOrDefault(timeSlot, false);
  }

  public ObjectProperty<LocalDate> getSelectedDateProperty() {
    return selectedDate;
  }

  public StringProperty getSelectedTimeSlotProperty() {
    return selectedTimeSlot;
  }

  public void setSelectedDate(LocalDate date) {
    selectedDate.set(date);
  }

  public void setSelectedTimeSlot(String timeSlot) {
    selectedTimeSlot.set(timeSlot);
  }
}