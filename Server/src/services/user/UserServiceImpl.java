package services.user;

import dtos.auth.CreateUserRequest;
import dtos.auth.LoginRequest;
import dtos.user.*;
import model.User;
import persistance.userDAO.UserDao;
import utils.PasswordUtil;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createUser(CreateUserRequest request) {
        User user = new User();
        user.setUserName(request.userName());
        user.setPassword(request.password()); // хеширование внутри DAO
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        user.setAddress(request.address());
        user.setEmail(request.email());
        user.setDiscount(0.0); // или request.discount() если есть
        user.setUserType(request.userType());

        userDao.createUser(user);
    }

    @Override
    public UserDataDto login(LoginRequest request) {
        User user = userDao.getUserByUsername(request.email());

        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + request.email());
        }

        if (!PasswordUtil.verifyPassword(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password.");
        }

        return new UserDataDto(
                user.getId(),
                user.getUserName(),
                null, // не отправляем пароль обратно
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getEmail(),
                user.getDiscount(),
                user.getUserType()
        );
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
    public void updateUser(UpdateUserRequest request) {
        // реализуй при необходимости
    }

    @Override
    public void deleteUser(DeleteUserRequest request) {
        userDao.deleteUser(request.id());
    }
}