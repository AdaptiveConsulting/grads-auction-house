package com.weareadaptive.auction;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.AuctionLotService;
import org.springframework.stereotype.Component;

@Component
public class AuctionTestData {
  private final AuctionLotService auctionLotService;
  private final TestData testData;
  private final Faker faker;
  private AuctionLot auction_user1;
  private AuctionLot auction_user2;

  public AuctionTestData(AuctionLotService auctionLotService, TestData testData) {
    this.auctionLotService = auctionLotService;
    this.testData = testData;
    faker = new Faker();
  }

  public void createInitData() {
    auction_user1 = createRandomAuctionLot(testData.user1());
    auction_user2 = createRandomAuctionLot(testData.user2());
  }

  public AuctionLot auction_user1() {
    return auction_user1;
  }

  public AuctionLot auction_user2() {
    return auction_user2;
  }

  public AuctionLot createRandomAuctionLot(User user) {
    return auctionLotService.create(
        user.getUsername(),
        faker.stock().nsdqSymbol(),
        faker.random().nextDouble() + 0.01,
        faker.random().nextInt(100) + 1
    );
  }
}
