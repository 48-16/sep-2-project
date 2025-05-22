package dtos.product;

import dtos.user.UserDataDto;

import java.io.Serializable;
import java.util.List;

public class ProductCartDto implements Serializable {
    private UserDataDto user;
    private List<ProductDto> products;
    private int totalPrice;

    public ProductCartDto(UserDataDto user) {
        this.user = user;
    }

    public ProductCartDto(UserDataDto user, List<ProductDto> products) {
        this.user = user;
        this.products = products;
    }

    public UserDataDto getUser() {
        return user;
    }

    public void setUser(UserDataDto user) {
        this.user = user;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
