package dtos.auth;

import java.io.Serializable;

public record RegisterUserRequest(
    String username,
    String password,
    String firstName,
    String lastName,
    String phoneNumber,
    String address,
    String email,
    String userType
) implements Serializable {
}