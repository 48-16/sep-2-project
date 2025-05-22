package services.productCart;

import dtos.product.ProductCartDtoRequest;
import dtos.product.ProductDto;
import model.Product;
import model.User;
import persistance.productCartDAO.ProductCartDAO;

import java.util.ArrayList;
import java.util.List;

public class ProductCartServiceImpl implements ProductCartService {
    private final ProductCartDAO productCartDAO;

    public ProductCartServiceImpl(ProductCartDAO productCartDAO) {
        this.productCartDAO = productCartDAO;
    }

    @Override
    public void addProductCart(ProductCartDtoRequest productCartDtoRequest) {
        User client = new User(productCartDtoRequest.productCartDto().getUser().getId(),
                productCartDtoRequest.productCartDto().getUser().getUserName(),
                productCartDtoRequest.productCartDto().getUser().getPassword(),
                productCartDtoRequest.productCartDto().getUser().getFirstName(),
                productCartDtoRequest.productCartDto().getUser().getLastName(),
                productCartDtoRequest.productCartDto().getUser().getPhoneNumber(),
                productCartDtoRequest.productCartDto().getUser().getAddress(),
                productCartDtoRequest.productCartDto().getUser().getEmail(),
                productCartDtoRequest.productCartDto().getUser().getDiscount(),
                productCartDtoRequest.productCartDto().getUser().getUserType());
        StringBuilder stringBuilder = new StringBuilder("");
        for (ProductDto productDto : productCartDtoRequest.productCartDto().getProducts()) {
            stringBuilder.append(productDto.getProductName()).append(",\n");
        }
        String prodctNames = stringBuilder.toString();
        String totalPrice = String.valueOf(productCartDtoRequest.productCartDto().getTotalPrice());
        productCartDAO.createProductCart(client, prodctNames, totalPrice);
    }
}
