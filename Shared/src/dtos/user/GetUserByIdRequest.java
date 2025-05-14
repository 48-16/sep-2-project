package dtos.user;

import java.io.Serializable;

public record GetUserByIdRequest(int id) implements Serializable {
}
