package networking.product;

import dtos.Request;
import dtos.product.*;
import networking.SocketService;

import java.util.List;

public class SocketProductClient implements ProductClient{
    @Override
    public void addProduct(AddProductRequest request) {
        SocketService.sendRequest(new Request("product", "add", request));
    }

    @Override
    public void updateProduct(UpdateProductRequest request) {
        SocketService.sendRequest(new Request("product", "update", request));
    }

    @Override
    public void deleteProduct(DeleteProductRequest request) {
        SocketService.sendRequest(new Request("product", "delete", request));
    }

    @Override
    public ProductDto getProduct(GetProductRequest request) {
        return (ProductDto) SocketService.sendRequest(new Request("product", "get", request));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProductDto> getAllProducts(GetAllProductsRequest request) {
        return (List<ProductDto>) SocketService.sendRequest(new Request("product", "get_all", request));
    }
}
