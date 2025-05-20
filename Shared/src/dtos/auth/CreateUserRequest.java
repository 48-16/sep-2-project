package dtos.auth;


import model.User;

import java.io.Serializable;

public record CreateUserRequest(String userName, String password, String firstName, String lastName, String phoneNumber, String address, String email, String userType) implements Serializable {}
