package dtos.user;


import dtos.model.User;

import java.io.Serializable;

public record CreateUserRequest(User user) implements Serializable {}
