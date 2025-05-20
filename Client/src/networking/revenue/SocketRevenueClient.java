package networking.revenue;

import dtos.Request;
import dtos.revenue.AddRevenueRequest;
import dtos.revenue.ResetRevenueRequest;
import dtos.revenue.RevenueRequest;
import networking.SocketService;

public class SocketRevenueClient implements RevenueClient{
    @Override
    public void addRevenue(AddRevenueRequest request) {
        SocketService.sendRequest(new Request("revenue", "add", request));
    }

    @Override
    public void resetRevenue(ResetRevenueRequest request) {
        SocketService.sendRequest(new Request("revenue", "reset", request));
    }

    @Override
    public double getTotalRevenue(RevenueRequest request) {
        return (double) SocketService.sendRequest(new Request("revenue", "get", request));
    }
}
