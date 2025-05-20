package services.product;

import dtos.product.*;
import model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    ProductDto getProductById(GetProductRequest getProductRequest) throws SQLException;
    List<ProductDto> getAllProducts(GetAllProductsRequest getAllProductsRequest) throws SQLException;
    void addProduct(AddProductRequest addProductRequest) throws SQLException;
    void updateProduct(UpdateProductRequest updateProductRequest) throws SQLException;
    void deleteProduct(DeleteProductRequest deleteProductRequest) throws SQLException;
    void updateProductQuantity(UpdateProductQuantityRequest request) throws SQLException;
}
