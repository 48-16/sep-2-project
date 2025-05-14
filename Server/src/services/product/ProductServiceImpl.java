package services.product;

import dtos.product.*;
import model.Product;
import persistance.productDAO.ProductDAO;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Product getProductById(GetProductRequest getProductRequest) {
        Product product = productDAO.getProductById(getProductRequest.productId());
        return product;
    }

    @Override
    public List<Product> getAllProducts(GetAllProductsRequest getAllProductsRequest) {
        List<Product> products = productDAO.getAllProducts();
        return products;
    }

    @Override
    public void addProduct(AddProductRequest addProductRequest) {
        Product product = new Product(addProductRequest.productName(),
                                        addProductRequest.price(),
                                        addProductRequest.quantity());
        productDAO.addProduct(product);
    }

    @Override
    public void updateProduct(UpdateProductRequest updateProductRequest) {
        Product product = productDAO.getProductById(updateProductRequest.id());
        product.setProductName(updateProductRequest.productName());
        product.setPrice(updateProductRequest.price());
        product.setQuantity(updateProductRequest.quantity());
        productDAO.updateProduct(product);
    }

    @Override
    public void deleteProduct(DeleteProductRequest deleteProductRequest) {
        int id = deleteProductRequest.id();
        productDAO.deleteProduct(id);
    }
}
