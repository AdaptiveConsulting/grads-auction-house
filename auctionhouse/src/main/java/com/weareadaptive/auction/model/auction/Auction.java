package com.weareadaptive.auction.model.auction;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity(name = "auction")
public class Auction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  private String symbol;

  private double minPrice;

  private int quantity;

  private String status;

  private String owner;

  private String closingSummary;

  private Instant closeTimestamp;

  public boolean isClosed() {
    return this.status.equals(Status.CLOSED.toString());
  }

  public void close() {
    this.status = Status.CLOSED.toString();
  }

  public enum Status {
    CLOSED, OPENED;
  }

}
