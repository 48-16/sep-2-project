package ui.login;


import dtos.auth.LoginRequest;
import dtos.user.GetUserByUsernameRequest;
import dtos.user.UserDataDto;
import networking.authentication.SocketAuthenticationClient;
import networking.user.SocketUserClient;
import startup.ViewHandler;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import startup.ViewType;
import utils.ErrorPopUp;


public class LoginViewModel
{
  private final StringProperty userNameProperty = new SimpleStringProperty();
  private final StringProperty passwordProperty = new SimpleStringProperty();
  private final BooleanProperty disableLoginButton = new SimpleBooleanProperty(true);
  private final SocketAuthenticationClient authService;
  private final ErrorPopUp errorPopUp = new ErrorPopUp();

  public LoginViewModel(SocketAuthenticationClient authService)
  {
    this.authService = authService;
    userNameProperty.addListener(this::changeLoginButtonState);
    passwordProperty.addListener(this::changeLoginButtonState);
  }

  public StringProperty getUserNameProperty()
  {
    return userNameProperty;
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
        userNameProperty.get() == null || userNameProperty.get().isEmpty()
            || passwordProperty.get() == null || passwordProperty.get()
            .isEmpty();
    disableLoginButton.set(disable);
  }

  public void login()
  {
    LoginRequest loginRequest = new LoginRequest(userNameProperty.get(),
        passwordProperty.get());
    try
    {
      authService.login(loginRequest);
      GetUserByUsernameRequest userRequest = new GetUserByUsernameRequest(userNameProperty.get());
      SocketUserClient userService = new SocketUserClient();
      UserDataDto user = userService.getUserByUsername(userRequest);

      if (user.getUserType().equals ("admin"))
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
      errorPopUp.show("Error", "Failed to login: " + e.getMessage());
    }
  }
}
