package com.weareadaptive.auction.controller;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.controller.dto.auctions.CreateAuctionLotRequest;
import com.weareadaptive.auction.controller.dto.bids.CreateBidRequest;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.AuctionLotService;
import com.weareadaptive.auction.service.UserService;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static java.math.BigDecimal.valueOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuctionLotControllerTest {
  public static final int INVALID_AUCTION_ID = 99999;

  @Autowired
  private AuctionLotService auctionLotService;
  @Autowired
  private UserService userService;
  @Autowired
  private TestData testData;
  @LocalServerPort
  private int port;
  private String uri;
  private final Faker faker = new Faker();

  @BeforeEach
  public void initialiseRestAssuredMockMvcStandalone() {
    uri = "http://localhost:" + port;
  }

  @DisplayName("create should create and return the new auction")
  @Test
  public void shouldReturnAuctionIfCreated() {
    User testUser = testData.user1();
    AuctionLot testAuctionLot = testData.auctionLot1();
    var createRequest =
      new CreateAuctionLotRequest(
        testAuctionLot.getSymbol(),
        testAuctionLot.getMinPrice(),
        testAuctionLot.getQuantity()
      );

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.getToken(testUser))
      .contentType(ContentType.JSON)
      .body(createRequest)
      .when()
      .post("/auctions")
      .then()
      .statusCode(HttpStatus.CREATED.value())
      .body("id", greaterThanOrEqualTo(1))
      .body("owner", equalTo(testUser.getUsername()))
      .body("symbol", equalTo(createRequest.symbol()))
      .body("minPrice", equalTo( (float) createRequest.minPrice() ))
      .body("quantity", equalTo(createRequest.quantity()))
      .body("status", equalTo(AuctionLot.Status.OPENED.toString()));
    //@formatter:on
  }

  @DisplayName("get should return an auctionLot")
  @Test
  public void shouldReturnAuctionLotIfExists() {
    User testUser = testData.user1();
    AuctionLot testAuctionLot = testData.auctionLot1();

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.getToken(testUser))
      .pathParam("id", testAuctionLot.getId())
      .when()
      .get("/auctions/{id}")
      .then()
      .statusCode(HttpStatus.OK.value())
      .body("id", greaterThanOrEqualTo(1))
      .body("owner", equalTo(testUser.getUsername()))
      .body("symbol", equalTo(testAuctionLot.getSymbol()))
      .body("minPrice", equalTo( (float) testAuctionLot.getMinPrice() ))
      .body("quantity", equalTo(testAuctionLot.getQuantity()))
      .body("status", equalTo(testAuctionLot.getStatus().toString()));
    //@formatter:on
  }

  @DisplayName("get should when return 404 when auctionLot doesn't exist")
  @Test
  public void shouldReturn404WhenAuctionLotDoesntExist() {
    User testUser = testData.user1();

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.getToken(testUser))
      .pathParam("id", INVALID_AUCTION_ID)
      .when()
      .get("/auctions/{id}")
      .then()
      .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("get should return all auctionLots")
  @Test
  public void getAll_shouldReturnAllAuctionLots() {
    User testUser = testData.user1();
    var find1 = format("find { it.id == %s }.", testData.auctionLot1().getId());
    var find2 = format("find { it.id == %s }.", testData.auctionLot2().getId());
    testData.auctionLot2().close();

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.getToken(testUser))
      .when()
      .get("/auctions")
      .then()
      .statusCode(HttpStatus.OK.value())
      // Validate Auction1
      .body(find1 + "id", greaterThanOrEqualTo(testData.auctionLot1().getId()))
      .body(find1 + "owner", equalTo(testData.auctionLot1().getOwner().getUsername()))
      .body(find1 + "symbol", equalTo(testData.auctionLot1().getSymbol()))
      .body(find1 + "minPrice", equalTo( (float) testData.auctionLot1().getMinPrice() ))
      .body(find1 + "quantity", equalTo(testData.auctionLot1().getQuantity()))
      .body(find1 + "status", equalTo(testData.auctionLot1().getStatus().toString()))
      // Validate Auction2
      .body(find2 + "id", greaterThanOrEqualTo(testData.auctionLot2().getId()))
      .body(find2 + "owner", equalTo(testData.auctionLot2().getOwner().getUsername()))
      .body(find2 + "symbol", equalTo(testData.auctionLot2().getSymbol()))
      .body(find2 + "minPrice", equalTo( (float) testData.auctionLot2().getMinPrice() ))
      .body(find2 + "quantity", equalTo(testData.auctionLot2().getQuantity()))
      .body(find2 + "status", equalTo(testData.auctionLot2().getStatus().toString()));
    //@formatter:on
  }

  @DisplayName("create should create and return the new bid")
  @Test
  public void shouldReturnBidIfCreated(){
    User testUser = testData.user2();
    AuctionLot testAuctionLot = testData.auctionLot1();
    CreateBidRequest createBidRequest = new CreateBidRequest(
      testAuctionLot.getQuantity(),
      testAuctionLot.getMinPrice()
    );

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.getToken(testUser))
      .pathParam("id", testAuctionLot.getId())
      .contentType(ContentType.JSON)
      .body(createBidRequest)
      .when()
      .post("/auctions/{id}/bids")
      .then()
      .statusCode(HttpStatus.OK.value())
      .body("username", equalTo(testUser.getUsername()))
      .body("quantity", equalTo(createBidRequest.quantity()))
      .body("price", equalTo((float) createBidRequest.price()))
      .body("state", equalTo(Bid.State.PENDING.toString()));
    //@formatter:on
  }

  @DisplayName("get should return all the bids for an auction")
  @Test
  public void shouldReturnAllBidsOfAnAuction(){
    User testUser2 = testData.user2();
    User testUser3 = testData.user3();
    AuctionLot testAuctionLot = testData.auctionLot1();

    var find1 = format("find { it.username == '%s' }.", testUser2.getUsername());
    var find2 = format("find { it.username == '%s' }.", testUser3.getUsername());

    testAuctionLot.bid(testUser2, testAuctionLot.getQuantity(), testAuctionLot.getMinPrice());
    testAuctionLot.bid(testUser3, testAuctionLot.getQuantity(), testAuctionLot.getMinPrice());

    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.getToken(testData.user1()))
      .pathParam("id", testAuctionLot.getId())
      .when()
      .get("/auctions/{id}/bids")
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(find1 + "username", equalTo(testUser2.getUsername()))
      .body(find1 + "quantity", equalTo(testAuctionLot.getQuantity()))
      .body(find1 + "price", equalTo( (float) testAuctionLot.getMinPrice() ))
      .body(find1 + "state", equalTo(Bid.State.PENDING.toString()))
      .body(find2 + "username", equalTo(testUser3.getUsername()))
      .body(find2 + "quantity", equalTo(testAuctionLot.getQuantity()))
      .body(find2 + "price", equalTo( (float) testAuctionLot.getMinPrice() ))
      .body(find2 + "state", equalTo(Bid.State.PENDING.toString()));
  }

  @DisplayName("put should return closing summary when closing an auctionLot")
  @Test
  public void shouldReturnClosingSummaryWhenClosingAnAuctionLot(){
    User testUser2 = testData.user2();
    User testUser3 = testData.user3();
    AuctionLot testAuctionLot = testData.auctionLot3();

    double winningPrice = testAuctionLot.getMinPrice() + 1.0;
    BigDecimal revenue = BigDecimal.ZERO;
    revenue = revenue.add(valueOf(winningPrice).multiply(valueOf(testAuctionLot.getQuantity())));

    testAuctionLot.bid(testUser2, testAuctionLot.getQuantity(), testAuctionLot.getMinPrice());
    testAuctionLot.bid(testUser3, testAuctionLot.getQuantity(), winningPrice);

    var find1 = format("winBids.find { it.username == '%s' }.", testUser3.getUsername());

    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.getToken(testData.user1()))
      .pathParam("id", testAuctionLot.getId())
      .when()
      .put("/auctions/{id}/close")
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(find1 + "username", equalTo(testUser3.getUsername()))
      .body(find1 + "quantity", equalTo(testAuctionLot.getQuantity()))
      .body(find1 + "price", equalTo( (float) winningPrice ))
      .body(find1 + "state", equalTo("WIN"))
      .body("totalSoldQuantity", equalTo(testAuctionLot.getQuantity()))
      .body("totalRevenue", equalTo(revenue.floatValue()));
      //closingTime
  }

  @DisplayName("get should return closing summary on a closed auction")
  @Test
  public void shouldReturnClosingSummaryOnClosedAuction(){
    User testUser2 = testData.user2();
    User testUser3 = testData.user3();
    AuctionLot testAuctionLot = testData.auctionLot4();

    double winningPrice = testAuctionLot.getMinPrice() + 1.0;
    BigDecimal revenue = BigDecimal.ZERO;
    revenue = revenue.add(valueOf(winningPrice).multiply(valueOf(testAuctionLot.getQuantity())));

    testAuctionLot.bid(testUser2, testAuctionLot.getQuantity(), testAuctionLot.getMinPrice());
    testAuctionLot.bid(testUser3, testAuctionLot.getQuantity(), winningPrice);

    var find1 = format("winBids.find { it.username == '%s' }.", testUser3.getUsername());

    auctionLotService.getAuctionLot(testAuctionLot.getId()).close();

    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.getToken(testData.user1()))
      .pathParam("id", testAuctionLot.getId())
      .when()
      .get("/auctions/{id}/closingSummary")
      .then()
      .statusCode(HttpStatus.OK.value())
      .body(find1 + "username", equalTo(testUser3.getUsername()))
      .body(find1 + "quantity", equalTo(testAuctionLot.getQuantity()))
      .body(find1 + "price", equalTo( (float) winningPrice ))
      .body(find1 + "state", equalTo("WIN"))
      .body("totalSoldQuantity", equalTo(testAuctionLot.getQuantity()))
      .body("totalRevenue", equalTo(revenue.floatValue()));
      //closingTime
  }
}
