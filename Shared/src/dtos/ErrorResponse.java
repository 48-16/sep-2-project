package dtos;

import java.io.Serializable;

public record ErrorResponse(String message) implements Serializable {
}
