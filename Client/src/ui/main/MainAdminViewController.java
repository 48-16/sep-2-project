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

public class MainAdminViewController
{
    // Shop tab components
    @FXML private TableView<ProductDto> productTableView;
    @FXML private TableColumn<ProductDto, String> productNameColumn;
    @FXML private TableColumn<ProductDto, Double> priceColumn;
    @FXML private TableColumn<ProductDto, Integer> quantityColumn;
    @FXML private Button addNewProductButton;
    @FXML private Button updateProductButton;

    // Barbershop tab components
    @FXML private DatePicker datePicker;
    @FXML private ListView<String> timeSlotListView;
    @FXML private Button deleteTimeButton;
    @FXML private Button deleteDayButton;
    @FXML private Button cancelAppointmentButton;

    private final MainViewAdminModel viewModel;
    private final ErrorPopUp errorPopUp = new ErrorPopUp();
    private final ObservableList<ProductDto> products = FXCollections.observableArrayList();
    private final ObservableList<String> timeSlots = FXCollections.observableArrayList();

    public MainAdminViewController(MainViewAdminModel viewModel)
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
                updateTimeSlots();
            }
        });

        // Setup time slot list view
        timeSlotListView.setItems(timeSlots);
        timeSlotListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        viewModel.setSelectedTimeSlot(newValue);
                        boolean isBooked = !viewModel.isTimeSlotAvailable(newValue);
                        deleteTimeButton.setDisable(!isBooked);
                        cancelAppointmentButton.setDisable(!isBooked);
                    }
                });

        // Initial state of buttons
        deleteTimeButton.setDisable(true);
        cancelAppointmentButton.setDisable(true);
    }

    private void initializeProductTable() {
        productNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProductName()));

        priceColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        priceColumn.setCellFactory(column -> new TableCell<ProductDto, Double>() {
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
        productTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void loadProducts() {
        try {
            List<ProductDto> productList = viewModel.getAllProducts();
            products.setAll(productList);
        } catch (Exception e) {
            errorPopUp.show("Error", "Failed to load products: " + e.getMessage());
        }
    }

    private void updateTimeSlots() {
        timeSlots.clear();
        Map<String, Boolean> availableSlots = viewModel.getAvailableTimeSlots();
        availableSlots.forEach((slot, available) -> {
            timeSlots.add(slot);
        });

        deleteTimeButton.setDisable(true);
        cancelAppointmentButton.setDisable(true);
        viewModel.setSelectedTimeSlot(null);
    }

    @FXML
    public void addNewProduct() {
        // Implementation for adding a new product
        errorPopUp.show("Info", "Product creation dialog would open here");
    }

    @FXML
    public void updateProduct() {
        ProductDto selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            errorPopUp.show("Error", "Please select a product to update");
            return;
        }
        errorPopUp.show("Info", "Product update dialog would open here for: " + selectedProduct.getProductName());
    }

    @FXML
    public void deleteTime() {
        if (datePicker.getValue() == null) {
            errorPopUp.show("Error", "Please select a date");
            return;
        }

        String selectedTimeSlot = timeSlotListView.getSelectionModel().getSelectedItem();
        if (selectedTimeSlot == null || selectedTimeSlot.isEmpty()) {
            errorPopUp.show("Error", "Please select a time slot");
            return;
        }

        boolean success = viewModel.deleteTimeSlot();
        if (success) {
            updateTimeSlots();
            errorPopUp.showSucces("Success", "Time slot deleted successfully");
        }
    }

    @FXML
    public void deleteDay() {
        if (datePicker.getValue() == null) {
            errorPopUp.show("Error", "Please select a date");
            return;
        }

        boolean success = viewModel.deleteDay();
        if (success) {
            updateTimeSlots();
            errorPopUp.showSucces("Success", "All appointments for the day have been deleted");
        }
    }

    @FXML
    public void cancelAppointment() {
        if (datePicker.getValue() == null) {
            errorPopUp.show("Error", "Please select a date");
            return;
        }

        String selectedTimeSlot = timeSlotListView.getSelectionModel().getSelectedItem();
        if (selectedTimeSlot == null || selectedTimeSlot.isEmpty()) {
            errorPopUp.show("Error", "Please select a time slot");
            return;
        }

        boolean success = viewModel.cancelAppointment();
        if (success) {
            updateTimeSlots();
            errorPopUp.showSucces("Success", "Appointment canceled successfully");
        }
    }
}