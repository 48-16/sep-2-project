package networking.requestHandler;

import dtos.Response;
import dtos.product.ProductCartDto;
import dtos.product.ProductCartDtoRequest;
import services.productCart.ProductCartService;

public class ProductCartRequestHandler implements RequestHandler {
    private final ProductCartService productCartService;

    public ProductCartRequestHandler(ProductCartService productCartService) {
        this.productCartService = productCartService;
    }

    @Override
    public boolean canHandle(Object request) {
        return request instanceof ProductCartDtoRequest;
    }

    @Override
    public Object handle(Object request) {
        if (request instanceof ProductCartDtoRequest productCartDtoRequest) {
            try {
                System.out.println("Creating product cart");
                productCartService.addProductCart(productCartDtoRequest);
                System.out.println("Product cart created successfully");
                return Response.success("product_cart_created"); // ✅ ВАЖНО
            } catch (Exception e) {
                System.out.println("Product cart creation failed");
                e.printStackTrace();
                return Response.error("Failed to create product cart: " + e.getMessage());
            }
        }

        return Response.error("Unknown creating shopping cart request: " + request.getClass().getSimpleName());
    }
}