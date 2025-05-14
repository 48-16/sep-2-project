package persistance.appointmentDAO;

import model.Appointment;
import model.User;
import persistance.PostgresConnection;
import persistance.userDAO.UserDAOImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public Appointment createAppointment(Appointment appointment) {
        String query = "INSERT INTO appointment(date, time, barber_id, client_id, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setDate(1, appointment.getDate());
            statement.setTime(2, appointment.getTime());
            statement.setInt(3, appointment.getBarber().getId());
            statement.setInt(4, appointment.getClient().getId());
            statement.setDouble(5, appointment.getPrice());

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                return new Appointment(id, appointment.getDate(), appointment.getTime(), appointment.getBarber(), appointment.getClient(), appointment.getPrice());
            } else {
                throw new SQLException("Failed to create appointment: no ID returned");
            }

        } catch (Exception e) {
            System.err.println("Error creating appointment: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void deleteAppointment(int id) {
        String query = "DELETE FROM appointment WHERE id = ?";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

        } catch (Exception e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
        }
    }

    @Override
    public List<Appointment> getAllAppointmentsByDate(Date date) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT id, date, time, barber_id, client_id, price FROM appointment WHERE date = ?";

        try (Connection connection = PostgresConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            statement.setDate(1, sqlDate);

            UserDAOImpl userDAOimpl = new UserDAOImpl();

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    java.sql.Date apptDate = resultSet.getDate("date");
                    Time time = resultSet.getTime("time");
                    int barberId = resultSet.getInt("barber_id");
                    int clientId = resultSet.getInt("client_id");
                    double price = resultSet.getDouble("price");

                    User barber = userDAOimpl.getUserById(barberId);
                    User client = userDAOimpl.getUserById(clientId);

                    Appointment appointment = new Appointment(id, apptDate, time, barber, client, price);
                    appointments.add(appointment);
                }
            }

        } catch (Exception e) {
            System.err.println("Error retrieving appointments: " + e.getMessage());
        }

        return appointments;
    }
}