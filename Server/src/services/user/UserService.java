package services.user;

import dtos.user.*;
import model.User;

public interface UserService {
    User getUserById(GetUserByIdRequest request);
    User getUserByUsername(GetUserByUsernameRequest request);
    void createUser(CreateUserRequest request);
    void updateUser(UpdateUserRequest request);
    void deleteUser(DeleteUserRequest request);
}
