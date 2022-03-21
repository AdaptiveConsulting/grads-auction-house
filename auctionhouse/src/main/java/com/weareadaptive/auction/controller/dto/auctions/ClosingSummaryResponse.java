package com.weareadaptive.auction.controller.dto.auctions;

import com.weareadaptive.auction.controller.dto.bids.BidResponse;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ClosingSummaryResponse(
    List<BidResponse> winBids,
    int totalSoldQuantity,
    BigDecimal totalRevenue,
    Instant closingTime) {
}
