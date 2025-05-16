package dtos.auth;

import java.io.Serializable;

public class UserDataDto implements Serializable {
  private final boolean admin;
  private final int userId;
  private final String username;
  private final String fullName;
  private final String email;

  public UserDataDto(boolean admin, int userId, String username, String fullName, String email) {
    this.admin = admin;
    this.userId = userId;
    this.username = username;
    this.fullName = fullName;
    this.email = email;
  }

  public boolean admin() {
    return admin;
  }

  public int getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }
}
