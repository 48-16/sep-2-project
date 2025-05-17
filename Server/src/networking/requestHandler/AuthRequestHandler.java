package networking.requestHandler;

import dtos.Response;
import dtos.auth.*;
import services.auth.AuthenticationService;

public class AuthRequestHandler implements RequestHandler {

  private final AuthenticationService authenticationService;

  public AuthRequestHandler(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public boolean canHandle(Object request) {
    return request instanceof LoginRequest ||
        request instanceof RegisterUserRequest ||
        request instanceof SessionRequest ||
        request instanceof LogoutRequest;
  }

  @Override
  public Object handle(Object request) {
    try {
      if (request instanceof LoginRequest loginRequest) {
        UserDataDto userData = authenticationService.login(loginRequest);
        return new Response("login_successful", userData);
      } else if (request instanceof RegisterUserRequest registerRequest) {
        UserDataDto userData = authenticationService.register(registerRequest);
        return new Response("registration_successful", userData);
      } else if (request instanceof SessionRequest sessionRequest) {
        UserDataDto userData = authenticationService.validateSession(sessionRequest);
        if (userData != null) {
          return new Response("session_valid", userData);
        } else {
          return new Response("session_invalid", null);
        }
      } else if (request instanceof LogoutRequest logoutRequest) {
        authenticationService.logout(logoutRequest);
        return new Response("logout_successful", null);
      }
    } catch (Exception e) {
      return new Response("auth_failed", e.getMessage());
    }

    return new Response("error", "Unknown authentication request");
  }
}