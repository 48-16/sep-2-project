package networking;

import dtos.Response;
import dtos.apointment.AppointmentRequest;
import dtos.apointment.DeleteAppointmentRequest;
import dtos.apointment.GetAppointmentsByDateRequest;
import dtos.product.*;
import dtos.revenue.AddRevenueRequest;
import dtos.revenue.ResetRevenueRequest;
import dtos.revenue.RevenueRequest;
import dtos.user.*;
import model.Appointment;
import model.Product;
import model.User;
import services.appointment.AppointmentService;
import services.appointment.AppointmentServiceImpl;
import services.product.ProductService;
import services.product.ProductServiceImpl;
import services.revenue.RevenueService;
import services.revenue.RevenueServiceImpl;
import services.user.UserService;
import startup.ServiceProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MainSocketHandler implements Runnable {

    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    private final AppointmentService appointmentService;
    private final ProductService productService;
    private final RevenueService revenueService;
    private final UserService userService;

    public MainSocketHandler(Socket socket, ServiceProvider provider) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        this.appointmentService = provider.getAppointmentService();
        this.productService = provider.getProductService();
        this.revenueService = provider.getRevenueService();
        this.userService = provider.getUserService();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object request = in.readObject();
                handleRequest(request);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client disconnected or error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private void handleRequest(Object request) throws IOException {
        if (request instanceof AddRevenueRequest addRevenueRequest) {
            revenueService.addRevenue(addRevenueRequest);
            out.writeObject(new Response("revenue_added", revenueService.getTotalRevenue()));
        }

        else if (request instanceof ResetRevenueRequest) {
            revenueService.resetRevenue();
            out.writeObject(new Response("revenue_reset", 0));
        }

        else if (request instanceof RevenueRequest) {
            int total = revenueService.getTotalRevenue();
            out.writeObject(new Response("revenue_total", total));
        }

        else if (request instanceof AddProductRequest addProductRequest) {
            productService.addProduct(addProductRequest);
            out.writeObject(new Response("product_added", null));
        }

        else if (request instanceof UpdateProductRequest updateProductRequest) {
            productService.updateProduct(updateProductRequest);
            out.writeObject(new Response("product_updated", null));
        }

        else if (request instanceof DeleteProductRequest deleteProductRequest) {
            productService.deleteProduct(deleteProductRequest);
            out.writeObject(new Response("product_deleted", null));
        }

        else if (request instanceof GetProductRequest getProductRequest) {
            Product product = productService.getProductById(getProductRequest);
            out.writeObject(new Response("product", product));
        }

        else if (request instanceof GetAllProductsRequest getAllProductsRequest) {
            List<Product> products = productService.getAllProducts(getAllProductsRequest);
            out.writeObject(new Response("all_products", products));
        }

        else if (request instanceof AppointmentRequest appointmentRequest) {
            appointmentService.createAppointment(appointmentRequest);
            out.writeObject(new Response("appointment_created", null));
        }

        else if (request instanceof GetAppointmentsByDateRequest getAppointmentsByDateRequest) {
            List<Appointment> appointments = appointmentService.getAppointmentsByDate(getAppointmentsByDateRequest);
            out.writeObject(new Response("appointments_by_date", appointments));
        }

        else if (request instanceof DeleteAppointmentRequest deleteAppointmentRequest) {
            appointmentService.deleteAppointment(deleteAppointmentRequest);
            out.writeObject(new Response("appointment_deleted", deleteAppointmentRequest.id()));
        }

        else if (request instanceof CreateUserRequest createUserRequest) {
            userService.createUser(createUserRequest);
            out.writeObject(new Response("user_created", null));
        }


        else if (request instanceof UpdateUserRequest updateUserRequest) {
            userService.updateUser(updateUserRequest);
            out.writeObject(new Response("user_updated", null));
        }


        else if (request instanceof DeleteUserRequest deleteUserRequest) {
            userService.deleteUser(deleteUserRequest);
            out.writeObject(new Response("user_deleted", deleteUserRequest.id()));
        }


        else if (request instanceof GetUserByIdRequest getUserByIdRequest) {
            User user = userService.getUserById(getUserByIdRequest);
            out.writeObject(new Response("user", user));
        }


        else if (request instanceof GetUserByUsernameRequest getUserByUsernameRequest) {
            User user = userService.getUserByUsername(getUserByUsernameRequest);
            out.writeObject(new Response("user", user));
        }

        else {
            out.writeObject(new Response("error", "Unknown request type: " + request.getClass().getSimpleName()));
        }
    }
}