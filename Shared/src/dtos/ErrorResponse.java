package dtos;

import java.io.Serializable;

public record ErrorResponse(String errorMessage) implements Serializable {
}
