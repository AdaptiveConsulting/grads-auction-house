package com.weareadaptive.auction.model;

import static java.lang.Math.min;
import static java.lang.String.format;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.reverseOrder;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static org.apache.logging.log4j.util.Strings.isBlank;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AuctionLot implements Entity {
  private final int id;
  private final User owner;
  private final String symbol;
  private final double minPrice;
  private final int quantity;
  private final List<Bid> bids;
  private Status status;
  private ClosingSummary closingSummary;
  private Supplier<Instant> timeProvider;

  public AuctionLot(int id, User owner, String symbol, int quantity, double minPrice) {
    if (owner == null) {
      throw new BusinessException("owner cannot be null");
    }
    if (isBlank(symbol)) {
      throw new BusinessException("symbol cannot be null or empty");
    }
    if (minPrice < 0) {
      throw new BusinessException("minPrice cannot be bellow 0");
    }
    if (quantity < 0) {
      throw new BusinessException("quantity must be above 0");
    }
    this.id = id;
    this.owner = owner;
    this.symbol = symbol.toUpperCase().trim();
    this.quantity = quantity;
    this.minPrice = minPrice;
    bids = new ArrayList<>();
    status = Status.OPENED;
    timeProvider = Instant::now;
  }

  public Status getStatus() {
    return status;
  }

  public User getOwner() {
    return owner;
  }

  public String getSymbol() {
    return symbol;
  }

  public ClosingSummary getClosingSummary() {
    if (Status.CLOSED != status) {
      throw new BusinessException("AuctionLot must be closed to have a closing summary");
    }
    return closingSummary;
  }

  public List<Bid> getBids() {
    return unmodifiableList(bids);
  }

  public void bid(User bidder, int quantity, double price) {
    if (status == Status.CLOSED) {
      throw new BusinessException("Cannot close an already closed.");
    }

    if (bidder == owner) {
      throw new BusinessException("User cannot bid on owned auction");
    }

    if (quantity < 0 || quantity > this.quantity) {
      throw new BusinessException(
          "bidding quantity must be be above 0 and/or not more than auction lot's quantity");
    }

    if (price < minPrice) {
      throw new BusinessException(format("price needs to be above %s", minPrice));
    }

    bids.add(new Bid(bidder, quantity, price));
  }

  public void close() {
    if (status == Status.CLOSED) {
      throw new BusinessException("Cannot close because already closed.");
    }

    status = Status.CLOSED;

    var orderedBids = bids
        .stream()
        .sorted(reverseOrder(comparing(Bid::getPrice))
            .thenComparing(reverseOrder(comparingInt(Bid::getQuantity))))
        .toList();
    var availableQuantity = this.quantity;
    var revenue = BigDecimal.ZERO;
    var winningBids = new ArrayList<WinningBid>();

    for (Bid bid : orderedBids) {
      if (availableQuantity > 0) {
        var bidQuantity = min(availableQuantity, bid.getQuantity());

        winningBids.add(new WinningBid(bidQuantity, bid));
        bid.win(bidQuantity);
        availableQuantity -= bidQuantity;
        revenue = revenue.add(valueOf(bidQuantity).multiply(valueOf(bid.getPrice())));
      } else {
        bid.lost();
      }
    }

    closingSummary =
        new ClosingSummary(unmodifiableList(winningBids), this.quantity - availableQuantity,
            revenue, timeProvider.get());
  }

  public int getId() {
    return id;
  }

  public double getMinPrice() {
    return minPrice;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setTimeProvider(Supplier<Instant> timeProvider) {
    this.timeProvider = timeProvider;
  }

  public List<Bid> getLostBids(User user) {
    return bids
        .stream()
        .filter(bid -> bid.getUser() == user
            && closingSummary.winningBids().stream().noneMatch(wb -> wb.originalBid() == bid))
        .toList();
  }

  public List<WinningBid> getWonBids(User user) {
    return closingSummary.winningBids()
        .stream()
        .filter(b -> b.originalBid().getUser() == user)
        .toList();
  }

  public boolean isOwner(String user) {
    return this.owner.getUsername().equals(user);
  }

  @Override
  public String toString() {
    return "AuctionLot{"
        + "owner=" + owner
        + ", symbol='" + symbol + '\''
        + ", status=" + status
        + '}';
  }

  public enum Status {
    OPENED,
    CLOSED
  }
}
