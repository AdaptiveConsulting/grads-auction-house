package com.weareadaptive.auction.model.auction;

import com.weareadaptive.auction.model.bid.WinningBid;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public record ClosingSummary(List<WinningBid> winningBids,
                             double remainingQuantity,
                             BigDecimal totalRevenue,
                             Instant closingTime) {
  @Override
  public String toString() {
    var wonBid = winningBids.stream()
        .map(WinningBid::toString)
        .collect(Collectors.joining("\n"));


    return "ClosingSummary{"
        + " \n winningBids=" + wonBid
        + ",\n remainingQuantity=" + remainingQuantity
        + ",\n totalRevenue=" + totalRevenue
        + ",\n closingTime=" + closingTime
        + '}';
  }
}
