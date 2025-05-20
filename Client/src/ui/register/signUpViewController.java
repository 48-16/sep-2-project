package ui.register;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import startup.ViewHandler;
import startup.ViewType;

public class signUpViewController
{
  @FXML private TextField firstNameField;

  @FXML private TextField lastNameField;

  @FXML private TextField emailField;

  @FXML private TextField passwordField;

  @FXML private Button registerButton;

  @FXML private Button cancelButton;
  private final signUpVIewModel viewModel;

  public signUpViewController(signUpVIewModel viewModel){
    this.viewModel = viewModel;
  }

  public  void initialize(){
    firstNameField.textProperty().bindBidirectional(viewModel.firstNameProp());
    lastNameField.textProperty().bindBidirectional(viewModel.lastNameProp());
    emailField.textProperty().bindBidirectional(viewModel.emailProp());
    passwordField.textProperty().bindBidirectional(viewModel.passwordProp());
    registerButton.disableProperty().bind(viewModel.disableSignUp());
  }

  @FXML private void register()
  {
    viewModel.registerUser();
  }

  @FXML private void cancel()
  {
    //For now gonna leave it like this maybe latter gonna change to something better
    ViewHandler.show(ViewType.LOGIN);
  }

}
