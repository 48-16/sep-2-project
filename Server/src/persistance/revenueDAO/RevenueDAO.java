package persistance.revenueDAO;

import java.sql.SQLException;

public interface RevenueDAO {
    int getRevenueTotal() throws SQLException;
    void addToRevenue(int amount) throws SQLException;
    void resetRevenue() throws SQLException;
}
