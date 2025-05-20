package persistance.userDAO;

import model.User;
import persistance.PostgresConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDao {

    @Override
    public User getUserById(int id) {
        String query = "SELECT * FROM person WHERE id = ?";
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
        }

        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM person WHERE username = ?";
        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void createUser(User user) {
        String insertPersonQuery = "INSERT INTO person (username, password, first_name, last_name, phone_number, address, email, discount, user_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement personStatement = connection.prepareStatement(insertPersonQuery)) {

            personStatement.setString(1, user.getUserName());
            personStatement.setString(2, hashPassword(user.getPassword()));
            personStatement.setString(3, user.getFirstName());
            personStatement.setString(4, user.getLastName());
            personStatement.setString(5, user.getPhoneNumber());
            personStatement.setString(6, user.getAddress());
            personStatement.setString(7, user.getEmail());
            personStatement.setDouble(8, user.getDiscount());
            personStatement.setString(9, user.getUserType());

            ResultSet rs = personStatement.executeQuery();
            if (rs.next()) {
                int personId = rs.getInt("id");

                // Вставка в соответствующую подтаблицу
                String userType = user.getUserType().toLowerCase();
                String subtableQuery = "INSERT INTO " + userType + " (person_id) VALUES (?)";

                try (PreparedStatement subtableStmt = connection.prepareStatement(subtableQuery)) {
                    subtableStmt.setInt(1, personId);
                    subtableStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            throw new RuntimeException("User creation failed", e);
        }
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE person SET username = ?, password = ?, first_name = ?, last_name = ?, phone_number = ?, address = ?, email = ?, discount = ?, user_type = ? WHERE id = ?";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getUserName());
            String finalPassword = isHashed(user.getPassword())
                    ? user.getPassword()
                    : hashPassword(user.getPassword());
            statement.setString(2, finalPassword);
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getPhoneNumber());
            statement.setString(6, user.getAddress());
            statement.setString(7, user.getEmail());
            statement.setDouble(8, user.getDiscount());
            statement.setString(9, user.getUserType());
            statement.setInt(10, user.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(int id) {
        String query = "DELETE FROM person WHERE id = ?";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setAddress(resultSet.getString("address"));
        user.setEmail(resultSet.getString("email"));
        user.setDiscount(resultSet.getDouble("discount"));
        user.setUserType(resultSet.getString("user_type"));
        return user;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private boolean isHashed(String password) {
        return password.matches("^[a-fA-F0-9]{64}$");
    }
}