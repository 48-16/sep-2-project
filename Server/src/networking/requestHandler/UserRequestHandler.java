package networking.requestHandler;

import dtos.Response;
import dtos.auth.CreateUserRequest;
import dtos.user.*;
import model.User;
import services.user.UserService;

public class UserRequestHandler implements RequestHandler {

    private final UserService userService;

    public UserRequestHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean canHandle(Object request) {
        return request instanceof CreateUserRequest ||
                request instanceof UpdateUserRequest ||
                request instanceof DeleteUserRequest ||
                request instanceof GetUserByIdRequest ||
                request instanceof GetUserByUsernameRequest;
    }

    @Override
    public Object handle(Object request) {
        if (request instanceof CreateUserRequest createUserRequest) {
            userService.createUser(createUserRequest);
            return new Response("user_created", null);
        } else if (request instanceof UpdateUserRequest updateUserRequest) {
            userService.updateUser(updateUserRequest);
            return new Response("user_updated", null);
        } else if (request instanceof DeleteUserRequest deleteUserRequest) {
            userService.deleteUser(deleteUserRequest);
            return new Response("user_deleted", deleteUserRequest.id());
        } else if (request instanceof GetUserByIdRequest getUserByIdRequest) {
            User user = userService.getUserById(getUserByIdRequest);
            return new Response("user", user);
        } else if (request instanceof GetUserByUsernameRequest getUserByUsernameRequest) {
            User user = userService.getUserByUsername(getUserByUsernameRequest);
            return new Response("user", user);
        }

        return new Response("error", "Unknown user request");
    }
}