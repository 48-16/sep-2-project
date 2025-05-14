package persistance.productDAO;

import model.Product;
import persistance.PostgresConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product getProductById(int id) {
        String query = "SELECT * FROM barbershop_product WHERE id = ?";
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractProductFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error getting product by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM barbershop_product";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(extractProductFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            System.err.println("Error getting all products: " + e.getMessage());
        }
        return products;
    }

    @Override
    public void addProduct(Product product) {
        String query = "INSERT INTO barbershop_product (product_name, price, quantity) VALUES (?, ?, ?)";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, product.getProductName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) {
        String query = "UPDATE barbershop_product SET product_name = ?, price = ?, quantity = ? WHERE id = ?";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, product.getProductName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setInt(4, product.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(int id) {
        String query = "DELETE FROM barbershop_product WHERE id = ?";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }

    private Product extractProductFromResultSet(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setPrice(resultSet.getDouble("price"));
        product.setQuantity(resultSet.getInt("quantity"));
        return product;
    }
}
