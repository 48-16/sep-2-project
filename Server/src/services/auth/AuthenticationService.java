package services.auth;

import dtos.auth.LoginRequest;
import dtos.auth.LogoutRequest;
import dtos.auth.RegisterUserRequest;
import dtos.auth.SessionRequest;
import dtos.auth.UserDataDto;

public interface AuthenticationService {
  UserDataDto login(LoginRequest loginRequest) throws Exception;
  UserDataDto register(RegisterUserRequest registerRequest) throws Exception;
  UserDataDto validateSession(SessionRequest sessionRequest);
  void logout(LogoutRequest logoutRequest);
}