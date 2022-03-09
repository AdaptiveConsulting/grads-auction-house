package com.weareadaptive.auction;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.AuctionLotService;
import com.weareadaptive.auction.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TestData {
  public static final String PASSWORD = "mypassword";
  public static final String ADMIN_AUTH_TOKEN = "Bearer ADMIN:adminpassword";

  private final AuctionLotService auctionLotService;
  private final UserService userService;
  private final Faker faker;
  private User user1;
  private User user2;
  private User user3;
  private User user4;
  private AuctionLot auction_user1;
  private AuctionLot auction_user2;

  public TestData(AuctionLotService auctionLotService, UserService userService) {
    this.auctionLotService = auctionLotService;
    this.userService = userService;
    faker = new Faker();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createInitData() {
    user1 = createRandomUser();
    user2 = createRandomUser();
    user3 = createRandomUser();
    user4 = createRandomUser();
  }

  public User user1() {
    return user1;
  }

  public User user2() {
    return user2;
  }

  public User user3() {
    return user3;
  }

  public User user4() {
    return user4;
  }

  public AuctionLot getAuction_user1() {
    return auction_user1;
  }

  public AuctionLot getAuction_user2() {
    return auction_user2;
  }

  public String user1Token() {
    return getToken(user1);
  }

  public String user2Token() {
    return getToken(user2);
  }

  public String user3Token() {
    return getToken(user3);
  }

  public String user4Token() {
    return getToken(user4);
  }

  public User createRandomUser() {
    var name = faker.name();
    var user = userService.create(
        name.username(),
        PASSWORD,
        name.firstName(),
        name.lastName(),
        faker.company().name()
    );
    return user;
  }

  public AuctionLot createRandomAuctionLot(User user) {
    return auctionLotService.create(
        user.getUsername(),
        faker.stock().nsdqSymbol(),
        faker.random().nextDouble() + 0.01,
        faker.random().nextInt(100) + 1
    );
  }

  public String getToken(User user) {
    return "Bearer " + user.getUsername() + ":" + PASSWORD;
  }

  public enum Stock {
    APPLE("AAPL"),
    MICROSOFT("MSFT"),
    META("FB");

    private final String symbol;

    Stock(String symbol) {
      this.symbol = symbol;
    }
  }
}
