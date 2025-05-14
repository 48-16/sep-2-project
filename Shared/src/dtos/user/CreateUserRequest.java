package dtos.user;


import model.User;

import java.io.Serializable;

public record CreateUserRequest(User user) implements Serializable {}
