package networking.productCart;

import dtos.Request;
import dtos.product.ProductCartDtoRequest;
import networking.SocketService;

public class SocketProductCartClient implements ProductCartClient {
    @Override
    public void createProductCart(ProductCartDtoRequest request) {
        SocketService.sendRequest(new Request("product_сart", "create", request));
    }
}
