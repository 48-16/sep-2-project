package dtos.product;

import java.io.Serializable;

public record UpdateProductRequest(int id, String productName, int price, int quantity) implements Serializable {
}
