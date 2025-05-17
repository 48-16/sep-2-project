package dtos.auth;

import java.io.Serializable;

public record LogoutRequest(String sessionId) implements Serializable {
}