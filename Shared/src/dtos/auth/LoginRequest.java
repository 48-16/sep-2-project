package dtos.auth;

import java.io.Serializable;

public record LoginRequest (String email, String password) implements Serializable {
}
