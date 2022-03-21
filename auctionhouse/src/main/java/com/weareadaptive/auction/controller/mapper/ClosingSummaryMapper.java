package com.weareadaptive.auction.controller.mapper;

import com.weareadaptive.auction.controller.dto.auctions.ClosingSummaryResponse;
import com.weareadaptive.auction.model.ClosingSummary;

public class ClosingSummaryMapper {
  public static ClosingSummaryResponse map(ClosingSummary closingSummary) {
    return new ClosingSummaryResponse(
      closingSummary.winningBids()
        .stream()
        .map(BidMapper::map)
        .toList(),
      closingSummary.totalSoldQuantity(),
      closingSummary.totalRevenue(),
      closingSummary.closingTime());
  }
}
