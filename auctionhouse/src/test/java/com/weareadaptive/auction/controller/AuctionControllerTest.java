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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

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
        .body("minPrice", equalTo(createRequest.minPrice()))
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

}
