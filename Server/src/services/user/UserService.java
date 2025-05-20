package services.user;

import dtos.auth.CreateUserRequest;
import dtos.auth.LoginRequest;
import dtos.user.*;
import model.User;

public interface UserService {
    User getUserById(GetUserByIdRequest request);
    User getUserByUsername(GetUserByUsernameRequest request);
    void createUser(CreateUserRequest request);
    void updateUser(UpdateUserRequest request);
    void deleteUser(DeleteUserRequest request);
    UserDataDto login(LoginRequest request);
}
