package services.product;

import dtos.product.*;
import model.Product;

import java.util.List;

public interface ProductService {
    Product getProductById(GetProductRequest getProductRequest);
    List<Product> getAllProducts(GetAllProductsRequest getAllProductsRequest);
    void addProduct(AddProductRequest addProductRequest);
    void updateProduct(UpdateProductRequest updateProductRequest);
    void deleteProduct(DeleteProductRequest deleteProductRequest);
}
