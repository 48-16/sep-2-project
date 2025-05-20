package ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import startup.ViewHandler;
import startup.ViewType;

public class loginViewControler
{

  @FXML private TextField emailField;
  @FXML private TextField passwordField;
  @FXML private Button loginButton;
  @FXML private Button signupButton;
  private final loginViewModel viewModel;
  private final ViewHandler handler;

  public loginViewControler(loginViewModel viewModel)
  {
    this.viewModel = viewModel;
    handler = new ViewHandler();
  }

  public void initialize()
  {
    emailField.textProperty().bind(viewModel.getUserNameProperty());
    passwordField.textProperty().bind(viewModel.getPasswordProperty());
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
