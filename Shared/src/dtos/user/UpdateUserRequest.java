package dtos.user;

import model.User;

import java.io.Serializable;

public record UpdateUserRequest(User user) implements Serializable {
}
