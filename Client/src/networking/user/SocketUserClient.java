package networking.user;

import dtos.Request;
import dtos.auth.CreateUserRequest;
import dtos.user.*;
import networking.SocketService;

public class SocketUserClient implements UserClient{
    @Override
    public void createUser(CreateUserRequest request) {
        SocketService.sendRequest(new Request("user", "create", request));
    }

    @Override
    public void updateUser(UpdateUserRequest request) {
        SocketService.sendRequest(new Request("user", "update", request));
    }

    @Override
    public void deleteUser(DeleteUserRequest request) {
        SocketService.sendRequest(new Request("user", "delete", request));
    }

    @Override
    public UserDataDto getUserById(GetUserByIdRequest request) {
        return (UserDataDto) SocketService.sendRequest(new Request("user", "get_by_id", request));
    }

    @Override
    public UserDataDto getUserByUsername(GetUserByUsernameRequest request) {
        return (UserDataDto) SocketService.sendRequest(new Request("user", "get_by_username", request));
    }
}
