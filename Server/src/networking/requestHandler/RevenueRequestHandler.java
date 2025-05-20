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
        try {
            if (request instanceof AddRevenueRequest addRevenueRequest) {
                System.out.println("Adding revenue: " + addRevenueRequest.additionalRevenue());
                revenueService.addRevenue(addRevenueRequest);
                int total = revenueService.getTotalRevenue();
                System.out.println("Revenue added successfully. New total: " + total);
                return Response.success(total);
            } else if (request instanceof ResetRevenueRequest) {
                System.out.println("Resetting revenue to 0");
                revenueService.resetRevenue();
                System.out.println("Revenue reset successfully");
                return Response.success(0);
            } else if (request instanceof RevenueRequest) {
                int total = revenueService.getTotalRevenue();
                System.out.println("Retrieved total revenue: " + total);
                return Response.success(total);
            }
        } catch (Exception e) {
            System.out.println("Error handling revenue request: " + e.getMessage());
            e.printStackTrace();
            return Response.error("Failed to process revenue request: " + e.getMessage());
        }

        return Response.error("Unknown revenue request type: " + request.getClass().getSimpleName());
    }
}