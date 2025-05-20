package dtos.product;

import java.io.Serializable;

public record UpdateProductQuantityRequest(int productId, int quantity) implements Serializable {}
