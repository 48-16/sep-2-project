package services.revenue;

import dtos.revenue.AddRevenueRequest;
import dtos.revenue.RevenueRequest;

public interface RevenueService {
    int getTotalRevenue();
    void addRevenue(AddRevenueRequest addRevenueRequest);
    void resetRevenue();
}