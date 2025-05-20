package networking.product;

import dtos.product.*;

import java.util.List;

public interface ProductClient {
    void addProduct(AddProductRequest request);
    void updateProduct(UpdateProductRequest request);
    void deleteProduct(DeleteProductRequest request);
    ProductDto getProduct(GetProductRequest request);
    List<ProductDto> getAllProducts(GetAllProductsRequest request);
}
