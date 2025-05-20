package networking.user;

import dtos.auth.CreateUserRequest;
import dtos.user.*;

public interface UserClient {
    void createUser(CreateUserRequest request);
    void updateUser(UpdateUserRequest request);
    void deleteUser(DeleteUserRequest request);
    UserDataDto getUserById(GetUserByIdRequest request);
    UserDataDto getUserByUsername(GetUserByUsernameRequest request);
}
