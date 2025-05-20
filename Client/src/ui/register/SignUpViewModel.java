package ui.register;

import dtos.auth.CreateUserRequest;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import networking.authentication.SocketAuthenticationClient;
import utils.ErrorPopUp;
import utils.FieldChecker;

public class SignUpViewModel {
    private final StringProperty firstNameProperty= new SimpleStringProperty();
    private final StringProperty lastNameProperty= new SimpleStringProperty();
    private final StringProperty emailProperty= new SimpleStringProperty();
    private final StringProperty passwordProperty= new SimpleStringProperty();
    private final BooleanProperty disableSignUpProperty = new SimpleBooleanProperty(true);

    private final SocketAuthenticationClient authClient;
    private final ErrorPopUp errorPopUp;

    public SignUpViewModel(SocketAuthenticationClient authClient){
        this.authClient = authClient;
        errorPopUp = new ErrorPopUp();

        firstNameProperty.addListener((observable, oldValue, newValue) -> updateSignUpButtonState() );
        lastNameProperty.addListener((observable, oldValue, newValue) -> updateSignUpButtonState() );
        emailProperty.addListener((observable, oldValue, newValue) -> updateSignUpButtonState() );
        passwordProperty.addListener((observable, oldValue, newValue) -> updateSignUpButtonState() );


    }
    private void updateSignUpButtonState() {
        boolean disable = FieldChecker.isNullOrEmpty(firstNameProperty.get()) ||
                FieldChecker.isNullOrEmpty(lastNameProperty.get()) ||
                FieldChecker.isNullOrEmpty(emailProperty.get()) ||
                FieldChecker.isNullOrEmpty(passwordProperty.get()) ||
                !isValidEmail(emailProperty.get()) ||
                !isValidPassword(passwordProperty.get());

        disableSignUpProperty.set(disable);
    }
    private boolean isValidEmail(String email){
        if(email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPassword(String password){
        if(password == null) return false;
        else return true;
    }

    public boolean registerUser(){
        try{
            CreateUserRequest request = new CreateUserRequest(
                    emailProperty.get(),
                    passwordProperty.get(),
                    firstNameProperty.get(),
                    lastNameProperty.get(),
                    "",
                    "",
                    emailProperty.get(),
                    "client"

            );
            authClient.registerUser(request);
            errorPopUp.showSucces("Success", "User registered successfully!");
            return true;
        } catch (Exception e){
            errorPopUp.show("Error", "Failed to register user: " + e.getMessage());
            return false;
        }
    }
    public StringProperty firstNameProp() {
        return firstNameProperty;
    }
    public StringProperty lastNameProp() {
        return lastNameProperty;
    }
    public StringProperty emailProp() {
        return emailProperty;
    }

    public StringProperty passwordProp() {
        return passwordProperty;
    }

    public BooleanProperty disableSignUp() {
        return disableSignUpProperty;
    }
}
