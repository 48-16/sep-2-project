package dtos.user;

import dtos.model.User;

import java.io.Serializable;

public record UpdateUserRequest(User user) implements Serializable {
}
