package com.weareadaptive.auction;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.AuctionLotService;
import com.weareadaptive.auction.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

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
  private AuctionLot auctionLot1;
  private AuctionLot auctionLot2;
  private AuctionLot auctionLot3;
  private AuctionLot auctionLot4;


  public TestData(UserService userService, AuctionLotService auctionLotService) {
    this.userService = userService;
    this.auctionLotService = auctionLotService;
    faker = new Faker();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createInitData() {
    user1 = createRandomUser();
    user2 = createRandomUser();
    user3 = createRandomUser();
    user4 = createRandomUser();

    auctionLot1 = createRandomAuction();
    auctionLot2 = createRandomAuction();
    auctionLot3 = createRandomAuction();
    auctionLot4 = createRandomAuction();
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

  public AuctionLot auctionLot1(){
    return auctionLot1;
  }

  public AuctionLot auctionLot2(){
    return auctionLot2;
  }

  public AuctionLot auctionLot3(){
    return auctionLot3;
  }

  public AuctionLot auctionLot4(){
    return auctionLot4;
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
    return userService.create(
      name.username(),
      PASSWORD,
      name.firstName(),
      name.lastName(),
      faker.company().name()
    );
  }

  public AuctionLot createRandomAuction(){
    return auctionLotService.createAuctionLot(
      user1.getUsername(),
      randomStock().toString(),
      faker.number().randomDouble(2, 1, 100),
      faker.number().numberBetween(1, 100));
  }

  public Stock randomStock(){
    Random random = new Random();
    return Arrays.stream(Stock.values()).toList().get(random.nextInt(3));
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