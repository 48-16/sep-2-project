package persistance.productDAO;

import model.Product;

import java.util.List;

public interface ProductDAO {
    public Product getProductById(int id);
    public List<Product> getAllProducts();
    public void addProduct(Product product);
    public void updateProduct(Product product);
    public void deleteProduct(int id);
}
