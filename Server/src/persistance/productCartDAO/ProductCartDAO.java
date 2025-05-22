package persistance.productCartDAO;

import model.Product;
import model.User;

import java.util.List;

public interface ProductCartDAO {
    void createProductCart(User client, String productNames, String totalPrice);
}
