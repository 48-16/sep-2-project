package networking.revenue;

import dtos.revenue.AddRevenueRequest;
import dtos.revenue.ResetRevenueRequest;
import dtos.revenue.RevenueRequest;

public interface RevenueClient {
    void addRevenue(AddRevenueRequest request);
    void resetRevenue(ResetRevenueRequest request);
    double getTotalRevenue(RevenueRequest request);
}
