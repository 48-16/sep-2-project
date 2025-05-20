package ui;

import dtos.auth.LoginRequest;
import dtos.user.UserDataDto;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.apointment.AppointmentClient;
import networking.apointment.SocketAppointmentClient;
import networking.authentication.AuthenticationClient;
import networking.authentication.SocketAuthenticationClient;
import networking.product.ProductClient;
import networking.product.SocketProductClient;
import networking.revenue.RevenueClient;
import networking.revenue.SocketRevenueClient;
import ui.main.MainViewUserController;
import ui.main.MainViewUserModel;
import utils.ErrorPopUp;

import java.io.IOException;

public class MainApplication extends Application {
    private final ErrorPopUp errorPopUp = new ErrorPopUp();
    private UserDataDto currentUser;
    private Stage primaryStage;

    // Create clients
    private final AuthenticationClient authClient = new SocketAuthenticationClient();
    private final ProductClient productClient = new SocketProductClient();
    private final AppointmentClient appointmentClient = new SocketAppointmentClient();
    private final RevenueClient revenueClient = new SocketRevenueClient();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Barbershop Management System");

        // For development/testing purposes, perform auto-login
        // In a real application, you would show a login screen first
        tryAutoLogin();
    }

    private void tryAutoLogin() {
        try {
            // Use test credentials for development
            LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
            currentUser = authClient.login(loginRequest);
            showMainView();
        } catch (Exception e) {
            errorPopUp.show("Login Error", "Could not auto-login: " + e.getMessage() +
                    "\n\nPlease check server connection and try again.");
        }
    }

    private void showMainView() {
        try {
            // Create the view model
            MainViewUserModel viewModel = new MainViewUserModel(
                    appointmentClient,
                    productClient,
                    revenueClient,
                    currentUser
            );

            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/mainViewUserFXML.fxml"));

            // Set the controller factory to use our view model
            loader.setControllerFactory(param -> new MainViewUserController(viewModel));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            errorPopUp.show("Error", "Could not load main view: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}