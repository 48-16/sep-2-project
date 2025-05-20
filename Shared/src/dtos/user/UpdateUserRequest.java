package dtos.user;

import model.User;

import java.io.Serializable;

public record UpdateUserRequest(String email, String oldPassword, String newPassword) implements Serializable {
}
