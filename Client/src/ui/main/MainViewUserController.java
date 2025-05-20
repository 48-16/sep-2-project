package ui.main;

import dtos.product.ProductDto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.ErrorPopUp;

import java.util.List;
import java.util.Map;

public class MainViewUserController
{
  @FXML private Button addToCartButton;
  @FXML private TableColumn<ProductDto, String> productNameColumn;
  @FXML private TableColumn<ProductDto, Double> priceColumn;
  @FXML private TableColumn<ProductDto, Integer> quantityColumn;
  @FXML private TableView<ProductDto> productTableView;
  @FXML private Button submitPurchaseButton;
  @FXML private Button confirmBookingButton;
  @FXML private DatePicker datePicker;
  @FXML private ListView<String> timeSlotListView;

  private final MainViewUserModel viewModel;
  private final ErrorPopUp errorPopUp = new ErrorPopUp();
  private final ObservableList<ProductDto> products = FXCollections.observableArrayList();
  private final ObservableList<String> availableTimeSlots = FXCollections.observableArrayList();

  public MainViewUserController(MainViewUserModel viewModel)
  {
    this.viewModel = viewModel;
  }

  public void initialize()
  {
    // Setup product table
    initializeProductTable();
    loadProducts();

    // Setup date picker
    datePicker.valueProperty()
            .bindBidirectional(viewModel.getSelectedDateProperty());

    datePicker.valueProperty().addListener((observable, oldDate, newDate) -> {
      if (newDate != null)
      {
        updateAvailableTimeSlots();
      }
    });

    // Setup time slot list view
    timeSlotListView.setItems(availableTimeSlots);
    timeSlotListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != null) {
                viewModel.setSelectedTimeSlot(newValue);
                confirmBookingButton.setDisable(false);
              }
            });

    confirmBookingButton.setDisable(true);
  }

  private void initializeProductTable() {
    productNameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getProductName()));

    priceColumn.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

    priceColumn.setCellFactory(column -> new TableCell<>() {
      @Override
      protected void updateItem(Double price, boolean empty) {
        super.updateItem(price, empty);
        if (empty || price == null) {
          setText(null);
        } else {
          setText(String.format("$%.2f", price));
        }
      }
    });

    quantityColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

    productTableView.setItems(products);

    productTableView.getSelectionModel().setCellSelectionEnabled(false);
    productTableView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
  }

  private void loadProducts() {
    try {
      List<ProductDto> productList = viewModel.getAllProducts();
      products.setAll(productList);
    } catch (Exception e) {
      errorPopUp.show("Error", "Failed to load products: " + e.getMessage());
    }
  }

  private void updateAvailableTimeSlots() {
    availableTimeSlots.clear();

    Map<String, Boolean> slots = viewModel.getAvailableTimeSlots();
    slots.forEach((slot, available) -> {
      if (available) {
        availableTimeSlots.add(slot);
      }
    });

    confirmBookingButton.setDisable(true);
    viewModel.setSelectedTimeSlot(null);
  }

  @FXML
  public void addToCart() {
    ProductDto selectedProduct = productTableView.getSelectionModel().getSelectedItem();
    if (selectedProduct == null) {
      errorPopUp.show("Error", "Please select a product");
      return;
    }

    boolean success = viewModel.addProductToCart(selectedProduct);
    if (success) {
      errorPopUp.showSucces("Success", "Product added to cart");
    }
  }

  @FXML
  public void confirmPurchase() {
    boolean success = viewModel.completePurchase();
    if (success) {
      errorPopUp.showSucces("Success", "Purchase completed successfully");
      // Refresh products to update quantities
      loadProducts();
    }
  }

  @FXML
  public void confirmBooking() {
    if (datePicker.getValue() == null) {
      errorPopUp.show("Error", "Please select a date");
      return;
    }

    String selectedTimeSlot = timeSlotListView.getSelectionModel().getSelectedItem();
    if (selectedTimeSlot == null || selectedTimeSlot.isEmpty()) {
      errorPopUp.show("Error", "Please select a time slot");
      return;
    }

    boolean success = viewModel.bookAppointment();
    if (success) {
      updateAvailableTimeSlots();
      errorPopUp.showSucces("Success", "Appointment booked successfully");
    }
  }
}