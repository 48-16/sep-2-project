package services.revenue;

import dtos.revenue.AddRevenueRequest;
import persistance.revenueDAO.RevenueDAO;

public class RevenueServiceImpl implements RevenueService {
    private final RevenueDAO revenueDAO;

    public RevenueServiceImpl(RevenueDAO revenueDAO) {
        this.revenueDAO = revenueDAO;
    }

    public void addRevenue(AddRevenueRequest request) {
        try {
            System.out.println("Adding revenue: " + request.additionalRevenue());
            revenueDAO.addToRevenue(request.additionalRevenue());
            System.out.println("Revenue added successfully");
        } catch (Exception e) {
            System.out.println("Error adding revenue: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add revenue: " + e.getMessage(), e);
        }
    }

    public int getTotalRevenue() {
        try {
            int total = revenueDAO.getRevenueTotal();
            System.out.println("Retrieved total revenue: " + total);
            return total;
        } catch (Exception e) {
            System.out.println("Error getting total revenue: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get total revenue: " + e.getMessage(), e);
        }
    }

    public void resetRevenue() {
        try {
            System.out.println("Resetting revenue to 0");
            revenueDAO.resetRevenue();
            System.out.println("Revenue reset successfully");
        } catch (Exception e) {
            System.out.println("Error resetting revenue: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to reset revenue: " + e.getMessage(), e);
        }
    }
}

