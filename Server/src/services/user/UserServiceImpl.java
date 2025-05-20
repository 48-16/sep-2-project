package services.user;

import dtos.auth.CreateUserRequest;
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

    }

    @Override
    public void updateUser(UpdateUserRequest request) {

    }


    public void createUser(UserDataDto request) {
        User user = new User(request.getId(),request.getUserName(),request.getPassword(), request.getFirstName(), request.getLastName(), request.getPhoneNumber(), request.getAddress(), request.getEmail(), request.getDiscount(), request.getUserType());
        userDao.createUser(user);
    }


    public void updateUser(UserDataDto request) {

        User user = new User(request.getId(),request.getUserName(),request.getPassword(), request.getFirstName(), request.getLastName(), request.getPhoneNumber(), request.getAddress(), request.getEmail(), request.getDiscount(), request.getUserType());
        userDao.createUser(user);
    }

    @Override
    public void deleteUser(DeleteUserRequest request) {
        userDao.deleteUser(request.id());
    }
}
