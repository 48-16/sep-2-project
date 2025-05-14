package model;

import persistance.revenueDAO.RevenueDAO;
import persistance.revenueDAO.RevenueDAOImpl;

public final class Revenue {


    private static Revenue INSTANCE;
    private int total;

    private Revenue() {
        RevenueDAO dao = new RevenueDAOImpl();
        this.total = dao.getRevenueTotal();
    }

    public synchronized static Revenue getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Revenue();
        }
        return INSTANCE;
    }

    public synchronized int getTotal() {
        return total;
    }

    public synchronized void add(int amount) {
        RevenueDAO dao = new RevenueDAOImpl();
        dao.addToRevenue(amount);
        this.total += amount;
    }

    public synchronized void reset() {
        RevenueDAO dao = new RevenueDAOImpl();
        dao.resetRevenue();
        this.total = 0;
    }
}
