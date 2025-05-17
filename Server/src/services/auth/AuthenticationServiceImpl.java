package services.auth;

import dtos.auth.*;
import dtos.model.User;
import persistance.userDAO.UserDao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserDao userDao;
  private final SessionManager sessionManager;

  public AuthenticationServiceImpl(UserDao userDao) {
    this.userDao = userDao;
    this.sessionManager = SessionManager.getInstance();
  }

  @Override
  public UserDataDto login(LoginRequest loginRequest) throws Exception {
    User user = userDao.getUserByUsername(loginRequest.username());

    if (user == null) {
      throw new Exception("User not found");
    }

    // Hash the provided password and compare with stored hash
    String hashedPassword = hashPassword(loginRequest.password());
    if (!user.getPassword().equals(hashedPassword)) {
      throw new Exception("Invalid password");
    }

    boolean isAdmin = "admin".equalsIgnoreCase(user.getUserType());
    String fullName = user.getFirstName() + " " + user.getLastName();

    UserDataDto userData = new UserDataDto(
        isAdmin,
        user.getId(),
        user.getUserName(),
        fullName,
        user.getEmail()
    );

    // Create a session for the user
    Session session = sessionManager.createSession(userData);

    // Attach the session ID to the user data
    // We need to modify UserDataDto to include the session ID
    return new UserDataDto(
        isAdmin,
        user.getId(),
        user.getUserName() + ":" + session.getSessionId(), // Temporarily add session ID to username
        fullName,
        user.getEmail()
    );
  }

  @Override
  public UserDataDto register(RegisterUserRequest registerRequest) throws Exception {
    // Check if username already exists
    User existingUser = userDao.getUserByUsername(registerRequest.username());
    if (existingUser != null) {
      throw new Exception("Username already exists");
    }

    // Create new user object
    User newUser = new User();
    newUser.setUserName(registerRequest.username());
    newUser.setPassword(registerRequest.password()); // Will be hashed by UserDAOImpl
    newUser.setFirstName(registerRequest.firstName());
    newUser.setLastName(registerRequest.lastName());
    newUser.setPhoneNumber(registerRequest.phoneNumber());
    newUser.setAddress(registerRequest.address());
    newUser.setEmail(registerRequest.email());
    newUser.setUserType(registerRequest.userType());

    // Set default discount for new users
    if ("client".equalsIgnoreCase(registerRequest.userType())) {
      newUser.setDiscount(0.0); // Default 0% discount for new clients
    } else {
      newUser.setDiscount(0.0); // No discount applicable for non-clients
    }

    // Create user in database
    userDao.createUser(newUser);

    // Retrieve the user with assigned ID
    User createdUser = userDao.getUserByUsername(registerRequest.username());
    if (createdUser == null) {
      throw new Exception("Failed to create user");
    }

    // Return user data
    boolean isAdmin = "admin".equalsIgnoreCase(registerRequest.userType());
    String fullName = registerRequest.firstName() + " " + registerRequest.lastName();

    UserDataDto userData = new UserDataDto(
        isAdmin,
        createdUser.getId(),
        createdUser.getUserName(),
        fullName,
        createdUser.getEmail()
    );

    // Create a session for the new user
    Session session = sessionManager.createSession(userData);

    return new UserDataDto(
        isAdmin,
        createdUser.getId(),
        createdUser.getUserName() + ":" + session.getSessionId(), // Temporarily add session ID to username
        fullName,
        createdUser.getEmail()
    );
  }

  @Override
  public UserDataDto validateSession(SessionRequest sessionRequest) {
    Session session = sessionManager.getSession(sessionRequest.sessionId());
    if (session != null) {
      return session.getUser();
    }
    return null;
  }

  @Override
  public void logout(LogoutRequest logoutRequest) {
    sessionManager.invalidateSession(logoutRequest.sessionId());
  }

  private String hashPassword(String password) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(password.getBytes());
      StringBuilder hexString = new StringBuilder();

      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }

      return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error hashing password", e);
    }
  }
}