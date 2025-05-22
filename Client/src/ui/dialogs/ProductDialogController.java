package ui.dialogs;

import dtos.product.AddProductRequest;
import dtos.product.ProductDto;
import dtos.product.UpdateProductRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.ErrorPopUp;

public class ProductDialogController {
    @FXML private TextField productNameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private ProductDto productToUpdate;
    private boolean isEditMode = false;
    private boolean saveClicked = false;
    private final ErrorPopUp errorPopUp = new ErrorPopUp();

    public void initialize() {
        // Add input validation listeners
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceField.setText(oldValue);
            }
        });

        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityField.setText(oldValue);
            }
        });
    }

    public void setProductToUpdate(ProductDto product) {
        this.productToUpdate = product;
        this.isEditMode = true;

        // Populate fields with existing product data
        productNameField.setText(product.getProductName());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));

        saveButton.setText("Update Product");
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            saveClicked = true;
            closeDialog();
        }
    }

    @FXML
    private void handleCancel() {
        saveClicked = false;
        closeDialog();
    }

    private boolean validateInput() {
        String productName = productNameField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (productName.isEmpty()) {
            errorPopUp.show("Validation Error", "Product name cannot be empty");
            return false;
        }

        if (priceText.isEmpty()) {
            errorPopUp.show("Validation Error", "Price cannot be empty");
            return false;
        }

        if (quantityText.isEmpty()) {
            errorPopUp.show("Validation Error", "Quantity cannot be empty");
            return false;
        }

        try {
            double price = Double.parseDouble(priceText);
            if (price < 0) {
                errorPopUp.show("Validation Error", "Price cannot be negative");
                return false;
            }
        } catch (NumberFormatException e) {
            errorPopUp.show("Validation Error", "Invalid price format");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity < 0) {
                errorPopUp.show("Validation Error", "Quantity cannot be negative");
                return false;
            }
        } catch (NumberFormatException e) {
            errorPopUp.show("Validation Error", "Invalid quantity format");
            return false;
        }

        return true;
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public AddProductRequest getAddProductRequest() {
        if (!isEditMode && saveClicked) {
            return new AddProductRequest(
                    productNameField.getText().trim(),
                    Double.parseDouble(priceField.getText().trim()),
                    Integer.parseInt(quantityField.getText().trim())
            );
        }
        return null;
    }

    public UpdateProductRequest getUpdateProductRequest() {
        if (isEditMode && saveClicked && productToUpdate != null) {
            return new UpdateProductRequest(
                    productToUpdate.getId(),
                    productNameField.getText().trim(),
                    Double.parseDouble(priceField.getText().trim()),
                    Integer.parseInt(quantityField.getText().trim())
            );
        }
        return null;
    }
}