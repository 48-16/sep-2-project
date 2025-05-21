package startup;

import networking.requestHandler.*;
import persistance.appointmentDAO.AppointmentDAO;
import persistance.appointmentDAO.AppointmentDAOImpl;
import persistance.productDAO.ProductDAO;
import persistance.productDAO.ProductDAOImpl;
import persistance.revenueDAO.RevenueDAO;
import persistance.revenueDAO.RevenueDAOImpl;
import persistance.userDAO.UserDAOImpl;
import persistance.userDAO.UserDao;
import services.appointment.AppointmentService;
import services.appointment.AppointmentServiceImpl;
import services.product.ProductService;
import services.product.ProductServiceImpl;
import services.revenue.RevenueService;
import services.revenue.RevenueServiceImpl;
import services.user.UserService;
import services.user.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {

    private final Map<String, RequestHandler> handlerMap;

    public ServiceProvider() {
        handlerMap = new HashMap<>();

        // Регистрация всех хендлеров по ключу
        handlerMap.put("auth", new AuthenticationRequestHandler(getUserService()));
        handlerMap.put("user", new UserRequestHandler(getUserService()));
        handlerMap.put("product", new ProductRequestHandler(getProductService()));
        handlerMap.put("appointment", new AppointmentRequestHandler(getAppointmentService()));
        handlerMap.put("revenue", new RevenueRequestHandler(getRevenueService()));
    }

    public RequestHandler getHandler(String handlerName) {
        return handlerMap.get(handlerName.toLowerCase());
    }

    public RevenueService getRevenueService() {
        return new RevenueServiceImpl(getRevenueDAO());
    }

    private RevenueDAO getRevenueDAO() {
        return new RevenueDAOImpl();
    }

    public ProductService getProductService() {
        return new ProductServiceImpl(getProductDAO());
    }

    private ProductDAO getProductDAO() {
        return new ProductDAOImpl();
    }

    public AppointmentService getAppointmentService() {
        return new AppointmentServiceImpl(getAppointmentDAO());
    }

    private AppointmentDAO getAppointmentDAO() {
        return new AppointmentDAOImpl();
    }

    public UserService getUserService() {
        return new UserServiceImpl(getUserDao());
    }

    private UserDao getUserDao() {
        return new UserDAOImpl();
    }
}