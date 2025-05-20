package dtos;

import java.io.Serializable;

public record Response(String status, Object payload) implements Serializable {
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";

    public static Response success(Object payload) {
        return new Response(SUCCESS, payload);
    }

    public static Response error(String errorMessage) {
        return new Response(ERROR, new ErrorResponse(errorMessage));
    }
}