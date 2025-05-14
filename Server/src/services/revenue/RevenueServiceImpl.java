package services.revenue;

import dtos.revenue.AddRevenueRequest;
import model.Revenue;
import persistance.revenueDAO.RevenueDAO;

public class RevenueServiceImpl implements RevenueService {
    private final RevenueDAO revenueDAO;

    public RevenueServiceImpl(RevenueDAO revenueDAO) {
        this.revenueDAO = revenueDAO;
    }

    public void addRevenue(AddRevenueRequest request) {
        revenueDAO.addToRevenue(request.additionalRevenue());
    }

    public int getTotalRevenue() {
        return revenueDAO.getRevenueTotal();
    }

    public void resetRevenue() {
        revenueDAO.resetRevenue();
    }
}

