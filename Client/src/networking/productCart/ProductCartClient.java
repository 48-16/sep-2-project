package networking.productCart;

import dtos.product.ProductCartDtoRequest;

public interface ProductCartClient {
    void createProductCart(ProductCartDtoRequest productCartDtoRequest);
}
