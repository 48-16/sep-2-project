package ui.main;

import dtos.apointment.AppointmentDto;
import dtos.apointment.AppointmentRequest;
import dtos.apointment.GetAppointmentsByDateRequest;
import dtos.product.GetAllProductsRequest;
import dtos.product.ProductDto;
import dtos.product.UpdateProductQuantityRequest;
import dtos.revenue.AddRevenueRequest;
import dtos.user.UserDataDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.apointment.AppointmentClient;
import networking.product.ProductClient;
import networking.revenue.RevenueClient;
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

public class MainViewUserModel {
  private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>();
  private final StringProperty selectedTimeSlot = new SimpleStringProperty();
  private final AppointmentClient appointmentClient;
  private final ProductClient productClient;
  private final RevenueClient revenueClient;
  private final UserDataDto currentUser;
  private final ErrorPopUp errorPopUp = new ErrorPopUp();
  private final ObservableList<ProductDto> cart = FXCollections.observableArrayList();

  private final Map<String, Boolean> timeSlotAvailability = new HashMap<>();

  private static final String[] TIME_SLOTS = {
          "8:00", "9:00", "10:00", "11:00",
          "12:00", "13:00", "14:00", "15:00"
  };

  public MainViewUserModel(
          AppointmentClient appointmentClient,
          ProductClient productClient,
          RevenueClient revenueClient,
          UserDataDto currentUser) {
    this.appointmentClient = appointmentClient;
    this.productClient = productClient;
    this.revenueClient = revenueClient;
    this.currentUser = currentUser;

    for (String slot : TIME_SLOTS) {
      timeSlotAvailability.put(slot, true);
    }

    selectedDate.addListener((observable, oldDate, newDate) -> {
      if (newDate != null) {
        updateAvailableTimeSlots(newDate);
      }
    });
  }

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

  public boolean bookAppointment() {
    if (selectedDate.get() == null || selectedTimeSlot.get() == null || selectedTimeSlot.get().isEmpty()) {
      errorPopUp.show("Error", "Please select a date and time slot");
      return false;
    }

    try {

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
      LocalTime localTime = LocalTime.parse(selectedTimeSlot.get(), formatter);
      Time time = Time.valueOf(localTime.withSecond(0));

      // Create a new appointment request
      AppointmentRequest request = new AppointmentRequest(
              0, // ID will be assigned by server
              Date.valueOf(selectedDate.get()),
              time,
              null, // barber - server should assign a barber
              new UserDataDto(currentUser.getId(), currentUser.getUserName(), currentUser.getPassword(), currentUser.getFirstName(), currentUser.getLastName(), currentUser.getPhoneNumber(), currentUser.getAddress(), currentUser.getEmail(), currentUser.getDiscount(), currentUser.getUserType()), // client - use current user
              50.0  // default price
      );

      System.out.println("Booking appointment:");
      System.out.println("Date: " + request.date());
      System.out.println("Time: " + request.time());
      System.out.println("Client: " + request.client());
      System.out.println("Client ID: " + request.client().getId());
      System.out.println("User type: " + request.client().getUserType());

      // Send the request to book the appointment
      appointmentClient.createAppointment(request);
      errorPopUp.showSucces("Success", "Appointment booked successfully!");
      updateAvailableTimeSlots(selectedDate.get());
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      errorPopUp.show("Error", "Error booking appointment: " + e.getMessage());
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

  public boolean addProductToCart(ProductDto product) {
    if (product.getQuantity() <= 0) {
      errorPopUp.show("Error", "Product is out of stock");
      return false;
    }

    cart.add(product);
    return true;
  }

  public boolean completePurchase() {
    if (cart.isEmpty()) {
      errorPopUp.show("Error", "Your cart is empty");
      return false;
    }

    try {
      double totalRevenue = 0.0;

      // Process each product in the cart
      for (ProductDto product : cart) {
        totalRevenue += product.getPrice();

        // Update product quantity
        int newQuantity = product.getQuantity() - 1;
        if (newQuantity < 0) {
          errorPopUp.show("Error", "Product " + product.getProductName() + " is out of stock");
          return false;
        }

        productClient.updateProductQuantity(new UpdateProductQuantityRequest(product.getId(), newQuantity));
      }

      // Add the revenue from this purchase
      revenueClient.addRevenue(new AddRevenueRequest((int)totalRevenue));

      // Clear the cart
      cart.clear();
      return true;
    } catch (Exception e) {
      errorPopUp.show("Error", "Failed to complete purchase: " + e.getMessage());
      return false;
    }
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