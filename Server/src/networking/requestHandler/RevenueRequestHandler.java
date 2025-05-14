package networking.requestHandler;

import dtos.Response;
import dtos.revenue.AddRevenueRequest;
import dtos.revenue.ResetRevenueRequest;
import dtos.revenue.RevenueRequest;
import services.revenue.RevenueService;

public class RevenueRequestHandler implements RequestHandler {

    private final RevenueService revenueService;

    public RevenueRequestHandler(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @Override
    public boolean canHandle(Object request) {
        return request instanceof AddRevenueRequest ||
                request instanceof ResetRevenueRequest ||
                request instanceof RevenueRequest;
    }

    @Override
    public Object handle(Object request) {
        if (request instanceof AddRevenueRequest addRevenueRequest) {
            revenueService.addRevenue(addRevenueRequest);
            return new Response("revenue_added", revenueService.getTotalRevenue());
        } else if (request instanceof ResetRevenueRequest) {
            revenueService.resetRevenue();
            return new Response("revenue_reset", 0);
        } else if (request instanceof RevenueRequest) {
            return new Response("revenue_total", revenueService.getTotalRevenue());
        }

        return new Response("error", "Unknown revenue request");
    }
}