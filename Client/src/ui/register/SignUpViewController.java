package ui.register;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import startup.ViewHandler;
import startup.ViewType;

public class SignUpViewController
{
  @FXML private TextField firstNameField;

  @FXML private TextField lastNameField;

  @FXML private TextField emailField;

  @FXML private TextField passwordField;

  @FXML private Button registerButton;

  @FXML private Button cancelButton;
  private final SignUpViewModel viewModel;
  private final ViewHandler viewHandler;

  public SignUpViewController(SignUpViewModel viewModel, ViewHandler viewHandler){
    this.viewModel = viewModel;
    this.viewHandler = viewHandler;
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
    if(viewModel.registerUser()){
    viewHandler.show(ViewType.LOGIN);}
  }

  @FXML private void cancel()
  {

    ViewHandler.show(ViewType.LOGIN);
  }

}
