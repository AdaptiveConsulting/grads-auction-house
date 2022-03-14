package com.weareadaptive.auction.model;

import static java.time.Instant.now;
import static org.apache.logging.log4j.util.Strings.isBlank;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "AuctionLot")
public class AuctionLot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private int ownerId;
  private String symbol;
  private double minPrice;
  private int quantity;
  private String status;
  private Instant createTime;
  private int totalSoldQuantity;
  private double totalRevenue;
  private Instant closingTime;

  public AuctionLot() {
  }

  public AuctionLot(int ownerId, String symbol, double minPrice, int quantity) {
    if (ownerId == 0) {
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
    this.ownerId = ownerId;
    this.symbol = symbol.toUpperCase().trim();
    this.quantity = quantity;
    this.minPrice = minPrice;
    this.status = String.valueOf(Status.OPENED);
    this.closingTime = now();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public double getMinPrice() {
    return minPrice;
  }

  public void setMinPrice(double minPrice) {
    this.minPrice = minPrice;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = String.valueOf(status);
  }

  public Instant getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Instant createTime) {
    this.createTime = createTime;
  }

  public int getTotalSoldQuantity() {
    return totalSoldQuantity;
  }

  public void setTotalSoldQuantity(int totalSoldQuantity) {
    this.totalSoldQuantity = totalSoldQuantity;
  }

  public double getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(double totalRevenue) {
    this.totalRevenue = totalRevenue;
  }

  public Instant getClosingTime() {
    return closingTime;
  }

  public void setClosingTime(Instant closingTime) {
    this.closingTime = closingTime;
  }

  @Override
  public String toString() {
    return "AuctionLot{"
        + "ownerId=" + ownerId
        + ", symbol='" + symbol + '\''
        + ", status=" + status
        + '}';
  }

  public enum Status {
    OPENED,
    CLOSED
  }
}
