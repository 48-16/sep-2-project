package ui.login;

import startup.ViewHandler;
import dtos.user.UserDataDto;
import dtos.auth.LoginRequest;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import networking.authentication.authenticationClient;
import startup.ViewType;

public class loginViewModel
{
  private final StringProperty emailProperty = new SimpleStringProperty();
  private final StringProperty passwordProperty = new SimpleStringProperty();
  private final BooleanProperty disableLoginButton = new SimpleBooleanProperty(true);
  private final authenticationClient authService;

  public loginViewModel(authenticationClient authService)
  {
    this.authService = authService;
    emailProperty.addListener(this::changeLoginButtonState);
    passwordProperty.addListener(this::changeLoginButtonState);
  }

  public StringProperty getEmailProperty()
  {
    return emailProperty;
  }

  public StringProperty getPasswordProperty()
  {
    return passwordProperty;
  }

  public BooleanProperty getDisableLoginButton()
  {
    return disableLoginButton;
  }

  public void changeLoginButtonState(Observable observable)
  {
    boolean disable =
        emailProperty.get() == null || emailProperty.get().isEmpty()
            || passwordProperty.get() == null || passwordProperty.get()
            .isEmpty();
    disableLoginButton.set(disable);
  }

  public void login()
  {
    LoginRequest loginRequest = new LoginRequest(emailProperty.get(),
        passwordProperty.get());
    try
    {
      UserDataDto user = authService.login(loginRequest);
      if (user.admin())
      {
        ViewHandler.show(ViewType.ADMIN);
      }
      else
      {
        ViewHandler.show(ViewType.USER);
      }

    }
    catch (Exception e)
    {

    }
  }
}
