package startup;

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

public class ServiceProvider {


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