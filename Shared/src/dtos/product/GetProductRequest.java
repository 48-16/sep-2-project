package dtos.product;

import java.io.Serializable;

public record GetProductRequest(int productId) implements Serializable {
}
