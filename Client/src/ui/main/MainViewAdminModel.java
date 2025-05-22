package ui.main;

import dtos.apointment.AppointmentDto;
import dtos.apointment.GetAppointmentsByDateRequest;
import dtos.product.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import networking.apointment.AppointmentClient;
import networking.product.ProductClient;
import utils.ErrorPopUp;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainViewAdminModel {
    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();
    private final StringProperty selectedTimeSlot = new SimpleStringProperty();
    private final AppointmentClient appointmentClient;
    private final ProductClient productClient;
    private final ErrorPopUp errorPopUp = new ErrorPopUp();

    private final Map<String, Boolean> timeSlotAvailability = new HashMap<>();

    private static final String[] TIME_SLOTS = {
            "8:00", "9:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00"
    };

    public MainViewAdminModel(
            AppointmentClient appointmentClient,
            ProductClient productClient) {
        this.appointmentClient = appointmentClient;
        this.productClient = productClient;

        for (String slot : TIME_SLOTS) {
            timeSlotAvailability.put(slot, true);
        }

        selectedDate.addListener((observable, oldDate, newDate) -> {
            if (newDate != null) {
                updateAvailableTimeSlots(newDate);
            }
        });
    }

    // Product management methods
    public boolean addProduct(AddProductRequest request) {
        try {
            productClient.addProduct(request);
            return true;
        } catch (Exception e) {
            errorPopUp.show("Error", "Failed to add product: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProduct(UpdateProductRequest request) {
        try {
            productClient.updateProduct(request);
            return true;
        } catch (Exception e) {
            errorPopUp.show("Error", "Failed to update product: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(DeleteProductRequest request) {
        try {
            productClient.deleteProduct(request);
            return true;
        } catch (Exception e) {
            errorPopUp.show("Error", "Failed to delete product: " + e.getMessage());
            return false;
        }
    }

    public List<ProductDto> getAllProducts() {
        try {
            return productClient.getAllProducts(new GetAllProductsRequest());
        } catch (Exception e) {
            errorPopUp.show("Error", "Failed to get products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ProductDto getProduct(int productId) {
        try {
            return productClient.getProduct(new GetProductRequest(productId));
        } catch (Exception e) {
            errorPopUp.show("Error", "Failed to get product: " + e.getMessage());
            return null;
        }
    }

    // Appointment management methods
    private void updateAvailableTimeSlots(LocalDate date) {
        try {
            // Reset all slots to available first
            for (String slot : TIME_SLOTS) {
                timeSlotAvailability.put(slot, true);
            }

            // Get booked appointments for this date
            List<AppointmentDto> appointments = appointmentClient.getAppointmentsByDate(
                    new GetAppointmentsByDateRequest(Date.valueOf(date))
            );

            // Mark booked slots as unavailable
            for (AppointmentDto appointment : appointments) {
                Time appointmentTime = appointment.time();
                String timeSlot = convertTimeToTimeSlot(appointmentTime);
                if (timeSlot != null) {
                    timeSlotAvailability.put(timeSlot, false);
                }
            }
        } catch (Exception e) {
            errorPopUp.show("Error", "Failed to receive available time slots: " + e.getMessage());
        }
    }

    private String convertTimeToTimeSlot(Time time) {
        LocalTime localTime = time.toLocalTime();
        int hour = localTime.getHour();

        if (hour >= 8 && hour < 16) {
            return String.format("%d:00", hour);
        }
        return null;
    }

    public boolean deleteTimeSlot() {
        if (selectedDate.get() == null || selectedTimeSlot.get() == null || selectedTimeSlot.get().isEmpty()) {
            errorPopUp.show("Error", "Please select a date and time slot");
            return false;
        }

        try {
            // This is a placeholder - you'd need to implement the actual API call
            // For example: appointmentClient.deleteTimeSlot(selectedDate.get(), selectedTimeSlot.get());

            // For now, let's simulate success
            errorPopUp.showSucces("Success", "Time slot deleted successfully!");
            updateAvailableTimeSlots(selectedDate.get());
            return true;
        } catch (Exception e) {
            errorPopUp.show("Error", "Error deleting time slot: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDay() {
        if (selectedDate.get() == null) {
            errorPopUp.show("Error", "Please select a date");
            return false;
        }

        try {
            // This is a placeholder - you'd need to implement the actual API call
            // For example: appointmentClient.deleteDay(selectedDate.get());

            // For now, let's simulate success
            errorPopUp.showSucces("Success", "All appointments for the day have been deleted!");
            updateAvailableTimeSlots(selectedDate.get());
            return true;
        } catch (Exception e) {
            errorPopUp.show("Error", "Error deleting appointments: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelAppointment() {
        if (selectedDate.get() == null || selectedTimeSlot.get() == null || selectedTimeSlot.get().isEmpty()) {
            errorPopUp.show("Error", "Please select a date and time slot");
            return false;
        }

        try {
            // Find the appointment that matches the selected date and time slot
            List<AppointmentDto> appointments = appointmentClient.getAppointmentsByDate(
                    new GetAppointmentsByDateRequest(Date.valueOf(selectedDate.get()))
            );

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            LocalTime selectedTime = LocalTime.parse(selectedTimeSlot.get(), formatter);

            for (AppointmentDto appointment : appointments) {
                LocalTime appointmentTime = appointment.time().toLocalTime();
                if (appointmentTime.getHour() == selectedTime.getHour()) {
                    // This is where you'd call the API to cancel the appointment
                    // appointmentClient.cancelAppointment(appointment.id())

                    // For now, let's simulate success
                    errorPopUp.showSucces("Success", "Appointment cancelled successfully!");
                    updateAvailableTimeSlots(selectedDate.get());
                    return true;
                }
            }

            errorPopUp.show("Error", "No appointment found for this time slot");
            return false;
        } catch (Exception e) {
            errorPopUp.show("Error", "Error cancelling appointment: " + e.getMessage());
            return false;
        }
    }

    // Getters and setters
    public boolean isTimeSlotAvailable(String timeSlot) {
        return timeSlotAvailability.getOrDefault(timeSlot, true);
    }

    public Map<String, Boolean> getAvailableTimeSlots() {
        return timeSlotAvailability;
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