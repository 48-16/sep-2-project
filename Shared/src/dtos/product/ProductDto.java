package dtos.product;

import java.io.Serializable;

public record ProductDto(int id, String productName, double price, int quantity) implements Serializable {}