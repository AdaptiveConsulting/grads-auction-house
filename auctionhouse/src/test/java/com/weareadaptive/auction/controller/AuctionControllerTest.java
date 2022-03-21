package com.weareadaptive.auction.controller;

import static com.weareadaptive.auction.TestData.ADMIN_AUTH_TOKEN;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.weareadaptive.auction.dto.request.CreateAuctionRequest;
import com.weareadaptive.auction.dto.request.CreateBidRequest;
import com.weareadaptive.auction.service.AuctionService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuctionControllerTest extends IntegrationTest {

  @Autowired
  private AuctionService auctionService;

  public static final String END_POINT = "/auctions";

  @BeforeEach
  public void initialiseRestAssuredMockMvcStandalone() {
    uri = "http://localhost:" + port + END_POINT;
  }


  @DisplayName("create should return a bad request when the auction already exists")
  @Test
  public void create_shouldReturn_400_IfAuctionExist() {
    var createRequest = new CreateAuctionRequest(
        testData.auction1().getSymbol(),
        (float) testData.auction1().getMinPrice(),
        testData.auction1().getQuantity());
    System.out.println("hello world");

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createRequest)
        .when()
        .post()
        .then()
        .log().all()
        .statusCode(BAD_REQUEST.value()).body("message", containsString("already exist"));
    //@formatter:on
  }

  @SuppressWarnings("checkstyle:Indentation")
  @DisplayName("create should create and return the new auction")
  @Test
  public void create_shouldReturnAuctionIfCreated() {
    var name = faker.stock().nyseSymbol();

    var createRequest = new CreateAuctionRequest(
        name,
        (float) testData.auction1().getMinPrice(),
        testData.auction1().getQuantity());
    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createRequest)
        .when()
        .post()
        .then()
        .log().all()
        .statusCode(CREATED.value())
        .body("symbol", equalTo(createRequest.symbol()))
        .body("minPrice", equalTo(createRequest.minPrice()))
        .body("quantity", equalTo(createRequest.quantity())
        );
    //@formatter:on
    var auction = auctionService.getAuction(name);
    Assertions.assertEquals(
        auction.getQuantity(),
        createRequest.quantity()
    );
    Assertions.assertEquals(
        auction.getMinPrice(),
        createRequest.minPrice()
    );
    Assertions.assertEquals(
        auction.getSymbol(),
        createRequest.symbol());
  }


  @DisplayName("Should return bad request if the user provides a negative/0 price for an auction ")
  @ParameterizedTest()
  @ValueSource(strings = {" ", "", "     "})
  public void create_shouldReturn_400_IfInvalidSymbol(String argument) {
    var name = faker.stock().nyseSymbol();

    var createRequest = new CreateAuctionRequest(
        argument,
        (float) testData.auction1().getMinPrice(),
        testData.auction1().getQuantity());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createRequest)
        .when()
        .post()
        .then()
        .log().all()
        .statusCode(BAD_REQUEST.value());

  }


  @DisplayName("Should return bad request if the user provides a negative/0 price for an auction ")
  @ParameterizedTest()
  @ValueSource(floats = {-0.1f, 0.0f, -1f})
  public void create_shouldReturn_400_IfInvalidPrice(float argument) {
    var name = faker.stock().nyseSymbol();

    var createRequest = new CreateAuctionRequest(name, argument, testData.auction1().getQuantity());


    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createRequest)
        .when()
        .post()
        .then()
        .log().all()
        .statusCode(BAD_REQUEST.value());
    //@formatter:on

  }

  @DisplayName
      ("Should return bad request if the user provides a negative/0 quantity for the auction")
  @ParameterizedTest()
  @ValueSource(ints = {0, -1})
  public void create_shouldReturn_400_IfNegativeQuantity(int argument) {
    var name = faker.stock().nyseSymbol();
    var createRequest = new CreateAuctionRequest(
        name,
        (float) testData.auction1().getMinPrice(),
        argument);
    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .body(createRequest)
        .when()
        .post()
        .then()
        .log().all()
        .statusCode(BAD_REQUEST.value());
    //@formatter:on
  }

  @DisplayName("get should when return 404 when user doesn't")
  @Test
  public void get_shouldReturn_404_WhenAuctionNotFound() {
    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", INVALID_ID)
        .when()
        .get("{id}")
        .then()
        .log().all()
        .statusCode(NOT_FOUND.value())
        .body("message", containsString("not found!"));
    //@formatter:on
  }


  @DisplayName("get should when return 400 if invalid id")
  @Test
  public void get_shouldReturn_400_IfInvalidId() {
    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", "dsads")
        .when()
        .get("{id}")
        .then()
        .log().all()
        .statusCode(BAD_REQUEST.value());
    //@formatter:on
  }

  @DisplayName("get should return an auction")
  @Test
  public void get_shouldReturnAuctionIfExists() {
    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .pathParam("id", testData.auction1().getId())
        .when()
        .get("{id}")
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .body("owner", equalTo(testData.auction1().getOwner()))
        .body("symbol", equalTo(testData.auction1().getSymbol()))
        .body("quantity", equalTo(testData.auction1().getQuantity()))
        .body("minPrice", equalTo((float) testData.auction1().getMinPrice()));
    //@formatter:on
  }

  @DisplayName("get should return an auction")
  @Test
  public void get_shouldReturnAuctionIfExistsAndOwner() {
    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user4Token())
        .pathParam("id", testData.auction1().getId())
        .when()
        .get("{id}")
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .body("owner", equalTo(testData.auction1().getOwner()))
        .body("symbol", equalTo(testData.auction1().getSymbol()))
        .body("quantity", equalTo(testData.auction1().getQuantity()))
        .body("minPrice", equalTo((float) testData.auction1().getMinPrice()));
    //@formatter:on
  }

  @DisplayName("get should return a list of auctions")
  @Test
  public void getAllAuctions_shouldReturnIfExists() {
    var find1 = format("find { it.id == %s }.", testData.auction1().getId());
    var find2 = format("find { it.id == %s }.", testData.auction2().getId());
    var find3 = format("find { it.id == %s }.", testData.auction3().getId());
    var find4 = format("find { it.id == %s }.", testData.auction4().getId());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .when()
        .get()
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .body(find1 + "symbol", equalTo(testData.auction1().getSymbol()))
        .body(find1 + "quantity", equalTo(testData.auction1().getQuantity()))
        .body(find1 + "minPrice", equalTo((float) testData.auction1().getMinPrice()))
        .body(find2 + "symbol", equalTo(testData.auction2().getSymbol()))
        .body(find2 + "quantity", equalTo(testData.auction2().getQuantity()))
        .body(find2 + "minPrice", equalTo((float) testData.auction2().getMinPrice()))
        .body(find3 + "symbol", equalTo(testData.auction3().getSymbol()))
        .body(find3 + "quantity", equalTo(testData.auction3().getQuantity()))
        .body(find3 + "minPrice", equalTo((float) testData.auction3().getMinPrice()))
        .body(find4 + "symbol", equalTo(testData.auction4().getSymbol()))
        .body(find4 + "quantity", equalTo(testData.auction4().getQuantity()))
        .body(find4 + "minPrice", equalTo((float) testData.auction4().getMinPrice()));
    //@formatter:on
  }

  @DisplayName("should be reg user to access api")
  @Test
  public void action_shouldReturn_403_WhenNotaUser() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .pathParam("id", INVALID_ID)
    .when()
      .get("{id}")
    .then()
        .log().all()
        .statusCode(FORBIDDEN.value());
    //@formatter:on
  }

  @DisplayName("should create a bid if valid")
  @Test
  void bid_shouldCreateBid() {
    var bid = testData.bid2();
    var user = testData.user2();
    var auctionId = testData.auction2().getId();
    var createRequest = new CreateBidRequest(bid.getQuantity(), bid.getPrice());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user2Token())
        .pathParam("id", auctionId)
        .contentType(ContentType.JSON)
        .body(createRequest)
        .when()
        .post("/{id}/bid")
        .then()
        .log().all()
        .statusCode(CREATED.value())
        .body("quantity", equalTo(bid.getQuantity()))
        .body("price", equalTo((float) bid.getPrice()));
    //@formatter:on

  }


  @DisplayName("should create a bid if valid")
  @Test
  void bid_shouldReturn_400_IfInvalidBid() {
    var bid = testData.bid2();
    var user = testData.user2();
    var auctionId = testData.auction2().getId();
    var createRequest = new CreateBidRequest(
        bid.getQuantity(),
        1);

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user2Token())
        .pathParam("id", auctionId)
        .contentType(ContentType.JSON)
        .body(createRequest)
        .when()
        .post("/{id}/bid")
        .then()
        .log().all()
        .statusCode(BAD_REQUEST.value())
        .body("message", containsString("price must be "));
    //@formatter:on
  }


  @Test
  void bid_shouldReturn_403_IfOwner() {
    var bid = testData.bid1();
    var auctionId = testData.auction1().getId();
    var createRequest = new CreateBidRequest(
        bid.getQuantity(),
        bid.getPrice()
    );

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user4Token())
        .contentType(ContentType.JSON)
        .body(createRequest)
        .pathParam("id", auctionId)
        .when()
        .post("/{id}/bid")
        .then()
        .log().all()
        .statusCode(FORBIDDEN.value())
        .body("message", containsString("cannot bid"));
    //@formatter:on
  }

  @Test
  void getAllBids_shouldReturnIfExists() {
    var bid = testData.bid1();
    var user = testData.user4();
    var auctionId = testData.auction2().getId();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user4Token())
        .contentType(ContentType.JSON)
        .pathParam("id", auctionId)
        .when()
        .get("{id}/bids")
        .then()
        .log().all()
        .statusCode(OK.value());
    //@formatter:on
  }

  @Test
  void getAllBids_shouldReturn_403_IfNotOwner() {
    var auctionId = testData.auction1().getId();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .pathParam("id", auctionId)
        .when()
        .get("{id}/bids")
        .then()
        .log().all()
        .statusCode(FORBIDDEN.value())
        .body("message", containsString("get all bids"));
  }

  @Test
  void close_shouldClose() {
    var bid = testData.bid1();
    var user = testData.user1();
    var auctionId = testData.auction1().getId();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user4Token())
        .contentType(ContentType.JSON)
        .pathParam("id", auctionId)
        .when()
        .post("/{id}/close")
        .then()
        .log().all()
        .statusCode(OK.value());
  }


  @Test
  void close_shouldReturn_403_IfNotOwner() {
    var auctionId = testData.auction2().getId();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
        .contentType(ContentType.JSON)
        .pathParam("id", auctionId)
        .when()
        .post("/{id}/close")
        .then()
        .log().all()
        .statusCode(FORBIDDEN.value()).body("message", containsString("owner can close"));
    //@formatter:on

  }


  @DisplayName("get closed auction")
  @Test
  public void get_closedAuction() {
    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user4Token())
        .pathParam("id", testData.auction1().getId())
        .when()
        .get("{id}")
        .then()
        .log().all()
        .statusCode(HttpStatus.OK.value())
        .body("owner", equalTo(testData.auction1().getOwner()))
        .body("symbol", equalTo(testData.auction1().getSymbol()))
        .body("quantity", equalTo(testData.auction1().getQuantity()))
        .body("minPrice", equalTo((float) testData.auction1().getMinPrice()));
    //@formatter:on
  }

  @Test
  void close_shouldReturn_403_IfAlreadyClosed() {
    var auctionId = testData.auction3().getId();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user4Token())
        .contentType(ContentType.JSON)
        .pathParam("id", auctionId)
        .when()
        .post("/{id}/close")
        .then()
        .log().all()
        .statusCode(FORBIDDEN.value())
        .body("message", containsString("is closed"));
  }


  @Test
  void getAuctionSummary_shouldGetSummary() {
    var auctionId = testData.auction1().getId();


    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user4Token())
        .contentType(ContentType.JSON)
        .pathParam("id", auctionId)
        .when()
        .get("/{id}/summary")
        .then()
        .log().all()
        .statusCode(OK.value());
  }

  @Test
  void getAllBidsFromClosedAuction_shouldReturnIfExists() {
    var bid = testData.bid1();
    var user = testData.user4();
    var auctionId = testData.auction1().getId();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user4Token())
        .contentType(ContentType.JSON)
        .pathParam("id", auctionId)
        .when()
        .get("{id}/bids")
        .then()
        .log().all()
        .statusCode(OK.value());
    //@formatter:on
  }

  @Test
  void getAuctionSummary_shouldReturn_403_IfAuctionOpen() {
    var auctionId = testData.auction2().getId();

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user4Token())
        .contentType(ContentType.JSON)
        .pathParam("id", auctionId)
        .when()
        .get("/{id}/summary")
        .then()
        .log().all()
        .statusCode(FORBIDDEN.value())
        .body("message", containsString("open auction"));
  }




}