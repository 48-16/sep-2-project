package ui.main;

import dtos.user.UserDataDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import networking.apointment.SocketAppointmentClient;
import utils.ErrorPopUp;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class mainViewUserModel {
  private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();
  private final StringProperty selectedTimeSlot = new SimpleStringProperty();
  private final SocketAppointmentClient appointmentService;
  private final UserDataDto currentUser;
  private ErrorPopUp errorPopUp = new ErrorPopUp();

  private final Map<String, Boolean> timeSlotAvailability = new HashMap<>();

  private static final String[] TIME_SLOTS = {
      "8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00",
      "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00"
  };

  public mainViewUserModel(
      SocketAppointmentClient apointmentService, UserDataDto currentUser) {
    this.appointmentService = apointmentService;
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
      availableTimeSlotsResponse response = appointmentService.getAvailableTimeSlots(date);
      boolean[] availableSlots = response.getAvailableSlots();

      for (int i = 0; i < TIME_SLOTS.length; i++) {
        timeSlotAvailability.put(TIME_SLOTS[i], availableSlots[i]);
      }
    } catch (Exception e) {
      errorPopUp.show("Error", "Failed to recive available time slots: " + e.getMessage());
    }
  }

  public boolean bookAppointment() {
    if (selectedDate.get() == null || selectedTimeSlot.get() == null || selectedTimeSlot.get().isEmpty()) {
      errorPopUp.show("Error", "Please select a date and time slot");
      return false;
    }

    try {
      bookAppointmentRequest request = new bookAppointmentRequest(
          currentUser.id(),
          selectedDate.get(),
          selectedTimeSlot.get()
      );

      appointmentDto result = appointmentService.bookAppointment(request);
      if (result != null) {
        errorPopUp.showSucces("Success", "Appointment booked successfully!");
        updateAvailableTimeSlots(selectedDate.get());
        return true;
      } else {
        errorPopUp.show("Error", "Failed to book appointment");
        return false;
      }
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