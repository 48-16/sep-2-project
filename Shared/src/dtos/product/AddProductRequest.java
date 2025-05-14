package dtos.product;

import java.io.Serializable;

public record AddProductRequest(String productName, int price, int quantity) implements Serializable {
}
