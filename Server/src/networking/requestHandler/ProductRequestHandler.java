package networking.requestHandler;

import dtos.Response;
import dtos.product.*;
import model.Product;
import services.product.ProductService;

import java.util.List;

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
                request instanceof GetAllProductsRequest ||
                request instanceof UpdateProductQuantityRequest;
    }

    @Override
    public Object handle(Object request) {
        try {
            if (request instanceof AddProductRequest addProductRequest) {
                System.out.println("Adding product: " + addProductRequest.productName());
                productService.addProduct(addProductRequest);
                System.out.println("Product added successfully: " + addProductRequest.productName());
                return Response.success("Product added successfully");
            } else if (request instanceof UpdateProductRequest updateProductRequest) {
                System.out.println("Updating product with ID: " + updateProductRequest.id());
                productService.updateProduct(updateProductRequest);
                System.out.println("Product updated successfully: " + updateProductRequest.id());
                return Response.success("Product updated successfully");
            } else if (request instanceof DeleteProductRequest deleteProductRequest) {
                System.out.println("Deleting product with ID: " + deleteProductRequest.id());
                productService.deleteProduct(deleteProductRequest);
                System.out.println("Product deleted successfully: " + deleteProductRequest.id());
                return Response.success("Product deleted successfully");
            } else if (request instanceof GetProductRequest getProductRequest) {
                System.out.println("Getting product with ID: " + getProductRequest.productId());
                ProductDto product = productService.getProductById(getProductRequest);
                System.out.println("Product retrieved successfully: " + getProductRequest.productId());
                return Response.success(product);
            } else if (request instanceof GetAllProductsRequest getAllProductsRequest) {
                System.out.println("Getting all products");
                List<ProductDto> products = productService.getAllProducts(getAllProductsRequest);
                System.out.println("Retrieved " + products.size() + " products");
                return Response.success(products);
            } else if (request instanceof UpdateProductQuantityRequest updateQuantityRequest) {
                System.out.println("Updating product quantity for ID: " + updateQuantityRequest.productId());
                productService.updateProductQuantity(updateQuantityRequest);
                System.out.println("Product quantity updated successfully");
                return Response.success("Product quantity updated successfully");
            }
        } catch (Exception e) {
            System.out.println("Error handling product request: " + e.getMessage());
            e.printStackTrace();
            return Response.error("Failed to process product request: " + e.getMessage());
        }

        return Response.error("Unknown product request type: " + request.getClass().getSimpleName());
    }
}