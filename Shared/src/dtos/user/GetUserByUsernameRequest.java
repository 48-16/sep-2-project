package dtos.user;

import java.io.Serializable;

public record GetUserByUsernameRequest(String username) implements Serializable {
}
