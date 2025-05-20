package persistance.revenueDAO;

import persistance.PostgresConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RevenueDAOImpl implements RevenueDAO {

    @Override
    public int getRevenueTotal() throws SQLException {
        String query = "SELECT total FROM revenue WHERE id = 1";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            throw new SQLException("Revenue record not found");
        }
    }

    @Override
    public void addToRevenue(int amount) throws SQLException {
        String query = "UPDATE revenue SET total = total + ? WHERE id = 1";

        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, amount);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update revenue: no rows affected");
            }
        }
    }

    @Override
    public void resetRevenue() throws SQLException {
        String query = "UPDATE revenue SET total = 0 WHERE id = 1";

        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to reset revenue: no rows affected");
            }
        }
    }
}
