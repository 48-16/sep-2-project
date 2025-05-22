package ui.main;

import dtos.product.AddProductRequest;
import dtos.product.DeleteProductRequest;
import dtos.product.ProductDto;
import dtos.product.UpdateProductRequest;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.dialogs.ProductDialogController;
import utils.ErrorPopUp;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainAdminViewController
{
    // Shop tab components
    @FXML private TableView<ProductDto> productTableView;
    @FXML private TableColumn<ProductDto, String> productNameColumn;
    @FXML private TableColumn<ProductDto, Double> priceColumn;
    @FXML private TableColumn<ProductDto, Integer> quantityColumn;
    @FXML private Button addNewProductButton;
    @FXML private Button updateProductButton;
    @FXML private Button deleteProductButton;

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

        // Setup product selection listener
        productTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateProductButton.setDisable(newValue == null);
                    deleteProductButton.setDisable(newValue == null);
                });

        // Initial state - disable buttons until selection
        updateProductButton.setDisable(true);
        deleteProductButton.setDisable(true);

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

        // Initial state of appointment buttons
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
        try {
            ProductDialogController dialogController = showProductDialog(null);
            if (dialogController != null && dialogController.isSaveClicked()) {
                AddProductRequest request = dialogController.getAddProductRequest();
                if (request != null) {
                    boolean success = viewModel.addProduct(request);
                    if (success) {
                        loadProducts(); // Refresh the table
                        errorPopUp.showSucces("Success", "Product added successfully!");
                    }
                }
            }
        } catch (Exception e) {
            errorPopUp.show("Error", "Failed to add product: " + e.getMessage());
        }
    }

    @FXML
    public void updateProduct() {
        ProductDto selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            errorPopUp.show("Error", "Please select a product to update");
            return;
        }

        try {
            ProductDialogController dialogController = showProductDialog(selectedProduct);
            if (dialogController != null && dialogController.isSaveClicked()) {
                UpdateProductRequest request = dialogController.getUpdateProductRequest();
                if (request != null) {
                    boolean success = viewModel.updateProduct(request);
                    if (success) {
                        loadProducts(); // Refresh the table
                        errorPopUp.showSucces("Success", "Product updated successfully!");
                    }
                }
            }
        } catch (Exception e) {
            errorPopUp.show("Error", "Failed to update product: " + e.getMessage());
        }
    }

    @FXML
    public void deleteProduct() {
        ProductDto selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            errorPopUp.show("Error", "Please select a product to delete");
            return;
        }

        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Product");
        confirmAlert.setContentText("Are you sure you want to delete the product '" +
                selectedProduct.getProductName() + "'?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                DeleteProductRequest request = new DeleteProductRequest(selectedProduct.getId());
                boolean success = viewModel.deleteProduct(request);
                if (success) {
                    loadProducts(); // Refresh the table
                    errorPopUp.showSucces("Success", "Product deleted successfully!");
                }
            } catch (Exception e) {
                errorPopUp.show("Error", "Failed to delete product: " + e.getMessage());
            }
        }
    }

    private ProductDialogController showProductDialog(ProductDto productToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/dialogs/productDialogFXML.fxml"));
            Parent root = loader.load();

            ProductDialogController dialogController = loader.getController();

            // If editing, set the product data
            if (productToEdit != null) {
                dialogController.setProductToUpdate(productToEdit);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(productToEdit == null ? "Add New Product" : "Update Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(addNewProductButton.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            dialogStage.showAndWait();

            return dialogController;

        } catch (IOException e) {
            errorPopUp.show("Error", "Failed to load product dialog: " + e.getMessage());
            return null;
        }
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