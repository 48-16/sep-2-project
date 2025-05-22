package services.productCart;

import dtos.product.ProductCartDto;
import dtos.product.ProductCartDtoRequest;
import model.Product;

public interface ProductCartService {
    void addProductCart(ProductCartDtoRequest productCartDtoRequest);
}
