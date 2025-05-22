package persistance.productCartDAO;

import model.Appointment;
import model.User;
import persistance.PostgresConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductCartDAOImpl implements ProductCartDAO {
    @Override
    public void createProductCart(User client, String productNames, String totalPrice) {
        String query = "INSERT INTO product_cart(client_id, product_names, total_price) VALUES (?, ?, ?)";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, client.getId());
            statement.setString(2, productNames);
            statement.setString(3, totalPrice);

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

        } catch (Exception e) {
            System.err.println("Error creating shopping cart: " + e.getMessage());
        }
    }
}
