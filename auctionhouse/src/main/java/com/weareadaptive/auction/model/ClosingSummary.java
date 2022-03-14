package com.weareadaptive.auction.model;

import java.time.Instant;
import java.util.List;

public record ClosingSummary(List<Bid> winningBids, int totalSoldQuantity, double totalRevenue,
                             Instant closingTime) {
}
