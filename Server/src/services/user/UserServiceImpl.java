package services.user;

import dtos.user.*;
import model.User;
import persistance.userDAO.UserDao;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(GetUserByIdRequest request) {
        return userDao.getUserById(request.id());
    }

    @Override
    public User getUserByUsername(GetUserByUsernameRequest request) {
        return userDao.getUserByUsername(request.username());
    }

    @Override
    public void createUser(CreateUserRequest request) {
        userDao.createUser(request.user());
    }

    @Override
    public void updateUser(UpdateUserRequest request) {
        userDao.updateUser(request.user());
    }

    @Override
    public void deleteUser(DeleteUserRequest request) {
        userDao.deleteUser(request.id());
    }
}
