package networking.authentication;

import dtos.auth.CreateUserRequest;
import dtos.auth.LoginRequest;
import dtos.user.UserDataDto;

public interface AuthenticationClient {
    void registerUser(CreateUserRequest request);
    UserDataDto login(LoginRequest loginRequest);
}
