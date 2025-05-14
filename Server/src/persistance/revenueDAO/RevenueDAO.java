package persistance.revenueDAO;

public interface RevenueDAO {
    public int getRevenueTotal();
    public void addToRevenue(int amount);
    public void resetRevenue();
}
