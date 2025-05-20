package ui.main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.ErrorPopUp;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class mainViewAdminModel {
//  private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();
//  private final StringProperty selectedTimeSlot = new SimpleStringProperty();
//  private final appointmentClient appointmentClient;
//  private final Map<String, Boolean> timeSlotAvailability = new HashMap<>();
//  private final ErrorPopUp errorPopUp = new ErrorPopUp();
//
//  private static final String[] TIME_SLOTS = {
//      "8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00",
//      "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00"
//  };
//
//  public mainViewAdminModel(appointmentClient appointmentClient) {
//    this.appointmentClient = appointmentClient;
//
//    for (String slot : TIME_SLOTS) {
//      timeSlotAvailability.put(slot, true);
//    }
//
//    selectedDate.addListener((obs, oldDate, newDate) -> {
//      if (newDate != null) {
//        updateAvailableTimeSlots(newDate);
//      }
//    });
//  }
//
//  private void updateAvailableTimeSlots(LocalDate date) {
//    try {
//      availableTimeSlotsResponse response = appointmentClient.getAvailableTimeSlots(date);
//      boolean[] availableSlots = response.getAvailableSlots();
//
//      for (int i = 0; i < TIME_SLOTS.length; i++) {
//        timeSlotAvailability.put(TIME_SLOTS[i], availableSlots[i]);
//      }
//    } catch (Exception e) {
//      errorPopUp.show("Error", "Failed to fetch available time slots: " + e.getMessage());
//    }
//  }
//
//  public boolean deleteTimeSlot() {
//    if (selectedDate.get() == null || selectedTimeSlot.get() == null || selectedTimeSlot.get().isEmpty()) {
//      errorPopUp.show("Error", "Please select a date and time slot");
//      return false;
//    }
//
//    try {
//      boolean result = appointmentClient.deleteTimeSlot(selectedDate.get(), selectedTimeSlot.get());
//      if (result) {
//        errorPopUp.showSucces("Success", "Time slot deleted successfully!");
//        updateAvailableTimeSlots(selectedDate.get());
//        return true;
//      } else {
//        errorPopUp.show("Error", "Failed to delete time slot");
//        return false;
//      }
//    } catch (Exception e) {
//      errorPopUp.show("Error", "Error deleting time slot: " + e.getMessage());
//      return false;
//    }
//  }
//
//  public boolean deleteDay() {
//    if (selectedDate.get() == null) {
//      errorPopUp.show("Error", "Please select a date");
//      return false;
//    }
//
//    try {
//      boolean result = appointmentClient.deleteDay(selectedDate.get());
//      if (result) {
//        errorPopUp.show("Success", "All appointments for the day have been deleted!");
//        updateAvailableTimeSlots(selectedDate.get());
//        return true;
//      } else {
//        errorPopUp.show("Error", "Failed to delete appointments");
//        return false;
//      }
//    } catch (Exception e) {
//      errorPopUp.show("Error", "Error deleting appointments: " + e.getMessage());
//      return false;
//    }
//  }
//
//  public boolean cancelAppointment() {
//    if (selectedDate.get() == null || selectedTimeSlot.get() == null || selectedTimeSlot.get().isEmpty()) {
//      errorPopUp.show("Error", "Please select a date and time slot");
//      return false;
//    }
//
//    try {
//      List<appointmentDto> appointments = appointmentClient.getAppointmentsByDate(selectedDate.get());
//
//      for (appointmentDto appointment : appointments) {
//        if (appointment.getTimeSlot().equals(selectedTimeSlot.get())) {
//          boolean result = appointmentClient.cancelAppointment(appointment.getUserId());
//          if (result) {
//            errorPopUp.showSucces("Success", "Appointment cancelled successfully!");
//            updateAvailableTimeSlots(selectedDate.get());
//            return true;
//          } else {
//            errorPopUp.show("Error", "Failed to cancel appointment");
//            return false;
//          }
//        }
//      }
//
//      errorPopUp.show("Error", "No appointment found for this time slot");
//      return false;
//    } catch (Exception e) {
//      errorPopUp.show("Error", "Error cancelling appointment: " + e.getMessage());
//      return false;
//    }
//  }
//
//  public boolean isTimeSlotAvailable(String timeSlot) {
//    return timeSlotAvailability.getOrDefault(timeSlot, false);
//  }
//
//  public ObjectProperty<LocalDate> getSelectedDateProperty() {
//    return selectedDate;
//  }
//
//  public StringProperty getSelectedTimeSlotProperty() {
//    return selectedTimeSlot;
//  }
//
//  public void setSelectedDate(LocalDate date) {
//    selectedDate.set(date);
//  }
//
//  public void setSelectedTimeSlot(String timeSlot) {
//    selectedTimeSlot.set(timeSlot);
//  }
//}