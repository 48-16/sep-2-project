package networking.authentication;

import dtos.Request;
import dtos.auth.CreateUserRequest;
import dtos.auth.LoginRequest;
import dtos.user.UserDataDto;
import networking.SocketService;

public class SocketAuthenticationClient implements AuthenticationClient {
    @Override
    public void registerUser(CreateUserRequest registerRequest) {
        Request request = new Request("auth", "register", registerRequest);
        SocketService.sendRequest(request);
    }

    @Override
    public UserDataDto login(LoginRequest loginRequest) {
        Request request = new Request("auth", "login", loginRequest);
        UserDataDto userData = (UserDataDto) SocketService.sendRequest(request);
        return userData;
    }
}
