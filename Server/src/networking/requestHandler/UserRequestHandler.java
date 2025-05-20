package networking.requestHandler;

import dtos.ErrorResponse;
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
            return Response.success("user_created");
        } else if (request instanceof UpdateUserRequest updateUserRequest) {
            userService.updateUser(updateUserRequest);
            return Response.success("user_updated");
        } else if (request instanceof DeleteUserRequest deleteUserRequest) {
            userService.deleteUser(deleteUserRequest);
            return Response.success("user_deleted: " + deleteUserRequest.id());
        } else if (request instanceof GetUserByIdRequest getUserByIdRequest) {
            User user = userService.getUserById(getUserByIdRequest);
            UserDataDto userDataDto = new UserDataDto(user);
            return Response.success(userDataDto);
        } else if (request instanceof GetUserByUsernameRequest getUserByUsernameRequest) {
            User user = userService.getUserByUsername(getUserByUsernameRequest);
            UserDataDto userDataDto = new UserDataDto(user);
            return Response.success(userDataDto);
        }

        return Response.error("Unknown user request type: " + request.getClass().getSimpleName());
    }
}