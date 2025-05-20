package ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import startup.ViewHandler;
import startup.ViewType;

public class LoginViewControler
{

  @FXML private TextField emailField;
  @FXML private TextField passwordField;
  @FXML private Button loginButton;
  @FXML private Button signupButton;
  private final LoginViewModel viewModel;
  private final ViewHandler viewHandler;

  public LoginViewControler(LoginViewModel viewModel, ViewHandler viewHandler)
  {
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;
  }

  public void initialize()
  {
    emailField.textProperty().bindBidirectional(viewModel.getUserNameProperty());
    passwordField.textProperty().bindBidirectional(viewModel.getPasswordProperty());
    loginButton.disableProperty().bind(viewModel.getDisableLoginButton());
  }

  @FXML private void logIn()
  {
    viewModel.login();
  }

  @FXML private void signUp()
  {
    ViewHandler.show(ViewType.REGISTER);
  }

}
