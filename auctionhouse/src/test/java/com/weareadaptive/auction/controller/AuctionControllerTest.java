package com.weareadaptive.auction.controller;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.controller.dto.BidAuctionRequest;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.service.AuctionLotService;
import com.weareadaptive.auction.service.UserService;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
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
  private UserService userService;
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
  public void create_shouldReturnIfCreated() {
    var createRequest = new CreateAuctionRequest(
        faker.stock().nsdqSymbol(),
        faker.random().nextDouble(),
        faker.random().nextInt(100) + 1);

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
  }

  @DisplayName("create should return a bad request for bad input")
  @Test
  public void create_shouldReturnBadRequestIfUserExist() {
    var createRequest = new CreateAuctionRequest(
        faker.stock().nsdqSymbol(),
        0,
        faker.random().nextInt(100) + 1);

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createRequest)
        .when()
        .post("/auctions")
        .then()
        .statusCode(BAD_REQUEST.value());
  }

  @DisplayName("Get should return a full OPENED auction for owner")
  @Test
  public void get_returnAnAuctionById() {
    var auctionLot = testData.getAuction_user1();

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
        .body("bids", equalTo(auctionLot.getBids()));
  }

  @DisplayName("Get should return basic info of auction if it's regular user")
  @Test
  public void get_return403WhenGetOthersAuctionLots() {
    var auctionLot = testData.getAuction_user2();

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
  }

  @DisplayName("get return 404 when a user get a nonexistent auction")
  @Test
  public void get_return404WhenUserDontExist() {

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionLotService.getAuctionState().getAuctionIndex().size() + 1)
        .when()
        .get("/auctions/{id}")
        .then()
        .statusCode(NOT_FOUND.value());
  }

  @DisplayName("Get all should return all auctions")
  @Test
  public void getAll_returnAllAuctions() {
    var user1 = testData.user1();
    var user2 = testData.user2();
    var auctionLot1 = testData.getAuction_user1();
    var auctionLot2 = testData.getAuction_user2();
    var find1 = format("find { it.id == %s }.", auctionLot1.getId());
    var find2 = format("find { it.id == %s }.", auctionLot2.getId());

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .when()
        .get("/auctions")
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .body(find1 + "id", equalTo(auctionLot1.getId()))
        .body(find1 + "symbol", equalTo(auctionLot1.getSymbol()))
        .body(find1 + "minPrice", equalTo((float) auctionLot1.getMinPrice()))
        .body(find1 + "quantity", equalTo(auctionLot1.getQuantity()))
        .body(find2 + "id", equalTo(auctionLot2.getId()))
        .body(find2 + "symbol", equalTo(auctionLot2.getSymbol()))
        .body(find2 + "minPrice", equalTo((float) auctionLot2.getMinPrice()))
        .body(find2 + "quantity", equalTo(auctionLot2.getQuantity()));
  }

  @DisplayName("Bid by auction id should return auction and created bid")
  @Test
  public void bid_shouldReturnAuctionBid() {
    var bidRequest = new BidAuctionRequest(
        faker.random().nextInt(100) + 1,
        faker.random().nextDouble());

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .pathParam("id", testData.getAuction_user2().getId())
        .body(bidRequest)
        .when()
        .post("/auctions/{id}/bid")
        .then()
        .log().all();
  }
}
