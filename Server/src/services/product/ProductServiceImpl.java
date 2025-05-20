package services.product;

import dtos.product.*;
import model.Product;
import persistance.productDAO.ProductDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public ProductDto getProductById(GetProductRequest getProductRequest) throws SQLException {
        Product product = productDAO.getProductById(getProductRequest.productId());
        ProductDto productDto = new ProductDto(product);
        return productDto;
    }

    @Override
    public List<ProductDto> getAllProducts(GetAllProductsRequest getAllProductsRequest) throws SQLException {
        List<Product> products = productDAO.getAllProducts();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(new ProductDto(product));
        }
        return productDtos;
    }

    @Override
    public void addProduct(AddProductRequest addProductRequest) throws SQLException {
        Product product = new Product(addProductRequest.productName(),
                addProductRequest.price(),
                addProductRequest.quantity());
        productDAO.addProduct(product);
    }

    @Override
    public void updateProduct(UpdateProductRequest updateProductRequest) throws SQLException {
        Product product = productDAO.getProductById(updateProductRequest.id());
        product.setProductName(updateProductRequest.productName());
        product.setPrice(updateProductRequest.price());
        product.setQuantity(updateProductRequest.quantity());
        productDAO.updateProduct(product);
    }

    @Override
    public void deleteProduct(DeleteProductRequest deleteProductRequest) throws SQLException {
        int id = deleteProductRequest.id();
        productDAO.deleteProduct(id);
    }

    @Override
    public void updateProductQuantity(UpdateProductQuantityRequest request) throws SQLException {
        Product product = productDAO.getProductById(request.productId());
        product.setQuantity(request.quantity());
        productDAO.updateProduct(product);
    }
}