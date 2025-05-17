package dtos.auth;

import java.io.Serializable;

public record SessionRequest(String sessionId) implements Serializable {
}