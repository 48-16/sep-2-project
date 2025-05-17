package networking.requestHandler;

import dtos.Response;
import dtos.auth.*;
import services.auth.AuthenticationService;
import dtos.auth.UserDataDto;

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
  public Response handle(Object request) {
    try {
      if (request instanceof LoginRequest loginRequest) {
        UserDataDto userData = authenticationService.login(loginRequest);
        if (userData != null) {
          return new Response("login_successful", userData);
        } else {
          return new Response("login_failed", "Invalid credentials.");
        }

      } else if (request instanceof RegisterUserRequest registerRequest) {
        UserDataDto userData = authenticationService.register(registerRequest);
        if (userData != null) {
          return new Response("registration_successful", userData);
        } else {
          return new Response("registration_failed", "Could not register user.");
        }

      } else if (request instanceof SessionRequest sessionRequest) {
        UserDataDto userData = authenticationService.validateSession(sessionRequest);
        return new Response(userData != null ? "session_valid" : "session_invalid", userData);

      } else if (request instanceof LogoutRequest logoutRequest) {
        authenticationService.logout(logoutRequest);
        return new Response("logout_successful", null);
      }

    } catch (Exception e) {
      e.printStackTrace();
      return new Response("auth_failed", "Error: " + e.getMessage());
    }

    return new Response("error", "Unknown authentication request.");
  }
}
