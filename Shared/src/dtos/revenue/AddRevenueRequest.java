package dtos.revenue;

import java.io.Serializable;

public record AddRevenueRequest(int additionalRevenue) implements Serializable {
}
