package dtos.auth;

import java.io.Serializable;

public record LoginRequest(String username, String password) implements Serializable {
}