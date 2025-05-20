package persistance.productDAO;

import model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    public Product getProductById(int id) throws SQLException;
    public List<Product> getAllProducts() throws SQLException;
    public void addProduct(Product product) throws SQLException;
    public void updateProduct(Product product) throws SQLException;
    public void deleteProduct(int id) throws SQLException;
}
