package com.weareadaptive.auction.controller;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.controller.dto.BidAuctionRequest;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.service.AuctionLotService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuctionControllerTest {
  private final Faker faker = new Faker();
  @Autowired
  private AuctionLotService auctionLotService;
  @Autowired
  private TestData testData;
  @LocalServerPort
  private int port;
  private String uri;

  @BeforeEach
  public void initialiseRestAssuredMockMvcStandalone() {
    uri = "http://localhost:" + port;
  }

  @DisplayName("Create should create and return new auction")
  @Test
  public void create_shouldReturnAuctionIfCreated() {
    var createRequest = new CreateAuctionRequest(
        faker.stock().nsdqSymbol(),
        faker.random().nextDouble(),
        faker.random().nextInt(100) + 1);

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createRequest)
    .when()
        .post("/auctions")
    .then()
        .statusCode(CREATED.value())
        .body("id", greaterThan(0))
        .body("symbol", equalTo(createRequest.symbol()))
        .body("minPrice", equalTo((float) createRequest.minPrice()))
        .body("quantity", equalTo(createRequest.quantity()));
    //@formatter:on
  }

  @DisplayName("create should return a bad request for bad input")
  @Test
  public void create_shouldReturnBadRequestForBadInput() {
    var createRequest = new CreateAuctionRequest(
        faker.stock().nsdqSymbol(),
        0,
        faker.random().nextInt(100) + 1);

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createRequest)
    .when()
        .post("/auctions")
    .then()
        .statusCode(BAD_REQUEST.value())
        .body("message", equalTo("Bad Request"));
    //@formatter:on
  }

  @DisplayName("Get should return a full auction info for owner")
  @Test
  public void get_returnAnAuctionById() {
    var auctionLot = testData.getAuction_user1();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionLot.getId())
    .when()
        .get("/auctions/{id}")
    .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .body("id", greaterThan(0))
        .body("symbol", equalTo(auctionLot.getSymbol()))
        .body("minPrice", equalTo((float) auctionLot.getMinPrice()))
        .body("quantity", equalTo(auctionLot.getQuantity()))
        .body("status", equalTo(valueOf(auctionLot.getStatus())))
        .body("bids", equalTo(auctionLot.getBids()));
    //@formatter:on
  }

  @DisplayName("Get should return basic info of auction if it's regular user")
  @Test
  public void get_returnBasicAuctionInfo() {
    var auctionLot = testData.getAuction_user2();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionLot.getId())
    .when()
        .get("/auctions/{id}")
    .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .body("id", greaterThan(0))
        .body("symbol", equalTo(auctionLot.getSymbol()))
        .body("minPrice", equalTo((float) auctionLot.getMinPrice()))
        .body("quantity", equalTo(auctionLot.getQuantity()));
    //@formatter:on
  }

  @DisplayName("get should return NOT_FOUND when a user get a nonexistent auction")
  @Test
  public void get_return404WhenAuctionDontExist() {
    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionLotService.getAuctionState().getAuctionIndex().size() + 1)
    .when()
        .get("/auctions/{id}")
    .then()
        .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("Get all should return all auctions")
  @Test
  public void getAll_returnAllAuctions() {
    var auctionLot1 = testData.getAuction_user1();
    var auctionLot2 = testData.getAuction_user2();
    var find1 = format("find { it.id == %s }.", auctionLot1.getId());
    var find2 = format("find { it.id == %s }.", auctionLot2.getId());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
    .when()
        .get("/auctions")
    .then()
        .statusCode(HttpStatus.OK.value())
        .body(find1 + "id", equalTo(auctionLot1.getId()))
        .body(find1 + "symbol", equalTo(auctionLot1.getSymbol()))
        .body(find1 + "minPrice", equalTo((float) auctionLot1.getMinPrice()))
        .body(find1 + "quantity", equalTo(auctionLot1.getQuantity()))
        .body(find2 + "id", equalTo(auctionLot2.getId()))
        .body(find2 + "symbol", equalTo(auctionLot2.getSymbol()))
        .body(find2 + "minPrice", equalTo((float) auctionLot2.getMinPrice()))
        .body(find2 + "quantity", equalTo(auctionLot2.getQuantity()));
    //@formatter:on
  }

  @DisplayName("Bid by auction id should return created bid")
  @Test
  public void bid_shouldReturnAuctionBid() {
    var auctionLot = testData.getAuction_user2();
    var bidder = testData.user1();
    var bidRequest = new BidAuctionRequest(
        faker.random().nextInt(1, auctionLot.getQuantity()),
        auctionLot.getMinPrice() + faker.random().nextDouble());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .pathParam("id", testData.getAuction_user2().getId())
        .body(bidRequest)
    .when()
        .post("/auctions/{id}/bid")
    .then()
        .body("auctionId", equalTo(auctionLot.getId()))
        .body("symbol", equalTo(auctionLot.getSymbol()))
        .body("bidderName", equalTo(bidder.getUsername()))
        .body("bidQuantity", equalTo(bidRequest.quantity()))
        .body("bidPrice", equalTo((float) bidRequest.price()));
    //@formatter:on
  }

  @DisplayName("Bid should return a bad request for bidding own auction")
  @Test
  public void bid_shouldReturnOwnerBadRequest() {
    var auctionLot = testData.getAuction_user1();
    var bidRequest = new BidAuctionRequest(
        faker.random().nextInt(1, auctionLot.getQuantity()),
        auctionLot.getMinPrice() + faker.random().nextDouble());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .pathParam("id", testData.getAuction_user1().getId())
        .body(bidRequest)
    .when()
        .post("/auctions/{id}/bid")
    .then()
        .statusCode(BAD_REQUEST.value())
        .body("message", containsString("cannot bid on owned auction"));
    //@formatter:on
  }

  @DisplayName("Bid should return a bad request for bidding with bad inputs")
  @Test
  public void bid_shouldReturnBadInputsBadRequest() {
    var auctionLot = testData.getAuction_user1();
    var bidRequest = new BidAuctionRequest(
        auctionLot.getQuantity() + 1,
        auctionLot.getMinPrice() + faker.random().nextDouble());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user2Token())
        .contentType(ContentType.JSON)
        .pathParam("id", testData.getAuction_user1().getId())
        .body(bidRequest)
    .when()
        .post("/auctions/{id}/bid")
    .then()
        .statusCode(BAD_REQUEST.value())
        .body("message", containsString("not more than auction lot's quantity"));
    //@formatter:on
  }

  @DisplayName("get all bids should return list of bid for an auction by id")
  @Test
  public void getAllBids_shouldReturnBidsOfAuction() {
    var owner = testData.user1();
    var auctionLot = testData.createRandomAuctionLot(owner);

    var bidder1 = testData.user2();
    var bidder2 = testData.user3();
    auctionLot.bid(bidder1, 20, auctionLot.getMinPrice() + 0.02);
    auctionLot.bid(bidder2, 30, auctionLot.getMinPrice() + 0.03);

    var find1 = format("find { it.quantity == %s }.", 20);
    var find2 = format("find { it.quantity == %s }.", 30);

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.getToken(owner))
        .contentType(ContentType.JSON)
        .pathParam("id", auctionLot.getId())
    .when()
        .get("/auctions/{id}/all-bids")
    .then()
        .body(find1 + "bidder", containsString(bidder1.getUsername()))
        .body(find1 + "quantity", equalTo(20))
        .body(find1 + "price", equalTo((float) (auctionLot.getMinPrice() + 0.02)))
        .body(find1 + "state", equalTo(valueOf(Bid.State.PENDING)))
        .body(find1 + "winQuantity", equalTo(0))
        .body(find2 + "bidder", containsString(bidder2.getUsername()))
        .body(find2 + "quantity", equalTo(30))
        .body(find2 + "price", equalTo((float) (auctionLot.getMinPrice() + 0.03)))
        .body(find2 + "state", equalTo(valueOf(Bid.State.PENDING)))
        .body(find2 + "winQuantity", equalTo(0));
    //@formatter:on
  }

  @DisplayName("get all bids should return unauthorized if it's not owner")
  @Test
  public void getAllBids_shouldReturnUnauthorized() {
    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .pathParam("id", testData.getAuction_user2().getId())
    .when()
        .get("/auctions/{id}/all-bids")
    .then()
        .statusCode(UNAUTHORIZED.value())
        .body("message", containsString("owner of auction lot."));
    //@formatter:on
  }

  @DisplayName("close should return ClosingSummary")
  @Test
  public void close_shouldReturnClosingSummary() {
    var owner = testData.user1();
    var auctionLot = auctionLotService.create(owner.getUsername(), "TEST", 2.50, 10);
    var bidder1 = testData.user2();
    var bidder2 = testData.user3();
    var bidder3 = testData.user4();
    auctionLot.bid(bidder1, 3, 3.00);
    auctionLot.bid(bidder2, 5, 3.50);
    auctionLot.bid(bidder3, 7, 4.00);

    var expectRevenue = 7 * 4.00 + 3 * 3.50;

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.getToken(owner))
        .contentType(ContentType.JSON)
        .pathParam("id", auctionLot.getId())
    .when()
        .post("/auctions/{id}/close")
    .then()
        .body("totalSoldQuantity", lessThanOrEqualTo(auctionLot.getQuantity()))
        .body("totalRevenue", equalTo((float) expectRevenue));
    //@formatter:on
  }

  @DisplayName("close should return UNAUTHORIZED for non-owner")
  @Test
  public void close_shouldReturnUnauthorizedForNonOwner() {
    var owner = testData.user1();
    var auctionLot = auctionLotService.create(owner.getUsername(), "TEST", 2.50, 10);
    auctionLot.bid(testData.user2(), faker.random().nextInt(1, auctionLot.getQuantity()),
        faker.random().nextDouble() + auctionLot.getMinPrice());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user3Token())
        .pathParam("id", auctionLot.getId())
    .when()
        .post("/auctions/{id}/close")
    .then()
        .statusCode(UNAUTHORIZED.value())
        .body("message", containsString("not the owner"));
    //@formatter:on
  }

  @DisplayName("close should return BAD_REQUEST for already closed auction")
  @Test
  public void close_shouldReturnBadRequestForClosedAuction() {
    var owner = testData.user1();
    var auctionLot = auctionLotService.create(owner.getUsername(), "TEST", 2.50, 10);
    auctionLot.close();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionLot.getId())
        .when()
        .post("/auctions/{id}/close")
        .then()
        .statusCode(BAD_REQUEST.value())
        .body("message", containsString("already closed"));
    //@formatter:on
  }

  @DisplayName("Get summary should return closing summary")
  @Test
  public void getSummary_shouldReturnClosingSummary() {
    var owner = testData.user1();
    var auctionLot = auctionLotService.create(owner.getUsername(), "TEST", 2.50, 10);
    var bidder1 = testData.user2();
    var bidder2 = testData.user3();
    auctionLot.bid(bidder1, 5, 3.00);
    auctionLot.bid(bidder2, 7, 3.50);

    var expectRevenue = 7 * 3.50 + 3 * 3.00;

    auctionLot.close();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.getToken(owner))
        .contentType(ContentType.JSON)
        .pathParam("id", auctionLot.getId())
    .when()
        .get("/auctions/{id}/close-summary")
    .then()
        .body("totalSoldQuantity", lessThanOrEqualTo(auctionLot.getQuantity()))
        .body("totalRevenue", equalTo((float) expectRevenue));
    //@formatter:on
  }

  @DisplayName("Get summary should return BAD_REQUEST for opened auction")
  @Test
  public void getSummary_shouldReturnBadRequestForOpenedAuction() {
    var owner = testData.user1();
    var auctionLot = auctionLotService.create(owner.getUsername(), "TEST", 2.50, 10);
    auctionLot.bid(testData.user2(), faker.random().nextInt(1, auctionLot.getQuantity()),
        faker.random().nextDouble() + auctionLot.getMinPrice());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.getToken(owner))
        .pathParam("id", auctionLot.getId())
    .when()
        .get("/auctions/{id}/close-summary")
    .then()
        .statusCode(BAD_REQUEST.value())
        .body("message", containsString("must be closed"));
    //@formatter:on
  }

  @DisplayName("Get summary should return BAD_REQUEST for non-owner")
  @Test
  public void getSummary_shouldReturnBadRequestForNonOwner() {
    var owner = testData.user1();
    var auctionLot = auctionLotService.create(owner.getUsername(), "TEST", 2.50, 10);
    auctionLot.bid(testData.user2(), faker.random().nextInt(1, auctionLot.getQuantity()),
        faker.random().nextDouble() + auctionLot.getMinPrice());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user3Token())
        .pathParam("id", auctionLot.getId())
    .when()
        .get("/auctions/{id}/close-summary")
    .then()
        .statusCode(UNAUTHORIZED.value())
        .body("message", containsString("not the owner"));
    //@formatter:on
  }
}
