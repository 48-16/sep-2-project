package networking.authentication;

import dtos.Request;
import dtos.auth.CreateUserRequest;
import dtos.auth.LoginRequest;
import dtos.user.UserDataDto;
import networking.SocketService;
import state.AppState;

public class SocketAuthenticationClient implements AuthenticationClient {
    @Override
    public void registerUser(CreateUserRequest registerRequest) {
        try {
            System.out.println("Sending registration request for user: " + registerRequest.userName());
            Request request = new Request("auth", "register", registerRequest);
            SocketService.sendRequest(request);
            System.out.println("Registration request sent successfully for user: " + registerRequest.userName());
        } catch (Exception e) {
            System.out.println("Failed to send registration request: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    @Override
    public UserDataDto login(LoginRequest loginRequest) {
        try {
            System.out.println("Sending login request for user: " + loginRequest.email());
            Request request = new Request("auth", "login", loginRequest);
            UserDataDto userData = (UserDataDto) SocketService.sendRequest(request);
            AppState.setCurrentUser(userData);
            System.out.println("Login request sent successfully for user: " + loginRequest.email());
            return userData;
        } catch (Exception e) {
            System.out.println("Failed to send login request: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }
}
