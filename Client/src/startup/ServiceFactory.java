package startup;

import networking.apointment.AppointmentClient;
import networking.apointment.SocketAppointmentClient;
import networking.authentication.AuthenticationClient;
import networking.authentication.SocketAuthenticationClient;
import networking.product.ProductClient;
import networking.product.SocketProductClient;
import networking.revenue.RevenueClient;
import networking.revenue.SocketRevenueClient;
import networking.user.SocketUserClient;
import networking.user.UserClient;

/**
 * Factory for creating and managing service clients.
 * This centralizes client creation and ensures only one instance of each client exists.
 */
public class ServiceFactory {

    private static ServiceFactory instance;

    // Client instances - lazily initialized
    private AuthenticationClient authClient;
    private ProductClient productClient;
    private AppointmentClient appointmentClient;
    private RevenueClient revenueClient;
    private UserClient userClient;

    private ServiceFactory() {
        // Private constructor to enforce singleton pattern
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public AuthenticationClient getAuthenticationClient() {
        if (authClient == null) {
            authClient = new SocketAuthenticationClient();
        }
        return authClient;
    }

    public ProductClient getProductClient() {
        if (productClient == null) {
            productClient = new SocketProductClient();
        }
        return productClient;
    }

    public AppointmentClient getAppointmentClient() {
        if (appointmentClient == null) {
            appointmentClient = new SocketAppointmentClient();
        }
        return appointmentClient;
    }

    public RevenueClient getRevenueClient() {
        if (revenueClient == null) {
            revenueClient = new SocketRevenueClient();
        }
        return revenueClient;
    }

    public UserClient getUserClient() {
        if (userClient == null) {
            userClient = new SocketUserClient();
        }
        return userClient;
    }
}