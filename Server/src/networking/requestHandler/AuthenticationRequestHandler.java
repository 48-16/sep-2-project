package networking.requestHandler;

import dtos.Response;
import dtos.auth.CreateUserRequest;
import dtos.auth.LoginRequest;
import dtos.user.UserDataDto;
import services.user.UserService;

public class AuthenticationRequestHandler implements RequestHandler {

    private final UserService userService;

    public AuthenticationRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean canHandle(Object request) {
        return request instanceof CreateUserRequest || request instanceof LoginRequest;
    }

    @Override
    public Object handle(Object request) {
        if (request instanceof CreateUserRequest createUserRequest) {
            System.out.println("Received registration request for user: " + createUserRequest.userName());
            try {
                userService.createUser(createUserRequest);
                System.out.println("User registered successfully: " + createUserRequest.userName());
                return Response.success("user_registered");
            } catch (Exception e) {
                System.out.println("Registration failed for user: " + createUserRequest.userName());
                e.printStackTrace();
                return Response.error("Failed to register user: " + e.getMessage());
            }
        }

        if (request instanceof LoginRequest loginRequest) {
            System.out.println("Received login request for user: " + loginRequest.email());
            try {
                UserDataDto user = userService.login(loginRequest);
                System.out.println("User logged in successfully: " + loginRequest.email());
                return Response.success(user);
            } catch (Exception e) {
                System.out.println("Login failed for user: " + loginRequest.email());
                return Response.error("Login failed: " + e.getMessage());
            }
        }

        return Response.error("Unknown authentication request: " + request.getClass().getSimpleName());
    }
}