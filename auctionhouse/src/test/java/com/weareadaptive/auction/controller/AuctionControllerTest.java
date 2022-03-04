package com.weareadaptive.auction.controller;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.controller.dto.CreateAuctionRequest;
import com.weareadaptive.auction.service.AuctionLotService;
import com.weareadaptive.auction.service.UserService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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

  @DisplayName("Get should return an open auction by id")
  @Test
  public void get_returnAnAuctionById() {
    var user = testData.user1();
    var auctionLot = testData.getAuction_user1();

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionLot.getId())
        .when()
        .get("/auctions/{id}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", greaterThan(0))
        .body("owner", equalTo(user.getUsername()))
        .body("symbol", equalTo(auctionLot.getSymbol()))
        .body("minPrice", equalTo((float) auctionLot.getMinPrice()))
        .body("quantity", equalTo(auctionLot.getQuantity()));
  }

  @DisplayName("get return 403 when server refuse a user to access others' auctions")
  @Test
  public void get_return403WhenGetOthersAuctionLots() {
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user2Token())
        .contentType(ContentType.JSON)
        .pathParam("id", testData.getAuction_user1().getId())
        .when()
        .get("/auctions/{id}")
        .then()
        .statusCode(UNAUTHORIZED.value())
        .body("message", containsString("user is not authorized"));
  }

  @DisplayName("get return 404 when a user get a nonexistent auction")
  @Test
  public void get_return404WhenUserDontExist() {

    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", auctionLotService.getAuctionState().getOwnerIndex().size())
        .when()
        .get("/auctions/{id}")
        .then()
        .statusCode(NOT_FOUND.value());
  }
}
