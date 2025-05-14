package networking.requestHandler;

import dtos.Response;
import dtos.product.*;
import model.Product;
import services.product.ProductService;

public class ProductRequestHandler implements RequestHandler {

    private final ProductService productService;

    public ProductRequestHandler(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean canHandle(Object request) {
        return request instanceof AddProductRequest ||
                request instanceof UpdateProductRequest ||
                request instanceof DeleteProductRequest ||
                request instanceof GetProductRequest ||
                request instanceof GetAllProductsRequest;
    }

    @Override
    public Object handle(Object request) {
        if (request instanceof AddProductRequest addProductRequest) {
            productService.addProduct(addProductRequest);
            return new Response("product_added", null);
        } else if (request instanceof UpdateProductRequest updateProductRequest) {
            productService.updateProduct(updateProductRequest);
            return new Response("product_updated", null);
        } else if (request instanceof DeleteProductRequest deleteProductRequest) {
            productService.deleteProduct(deleteProductRequest);
            return new Response("product_deleted", deleteProductRequest.id());
        } else if (request instanceof GetProductRequest getProductRequest) {
            Product product = productService.getProductById(getProductRequest);
            return new Response("product", product);
        } else if (request instanceof GetAllProductsRequest getAllProductsRequest) {
            return new Response("all_products", productService.getAllProducts(getAllProductsRequest));
        }

        return new Response("error", "Unknown product request");
    }
}