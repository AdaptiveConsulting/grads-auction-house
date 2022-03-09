package com.weareadaptive.auction;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {
  @LocalServerPort
  protected int port;
  protected String uri;
  @Autowired
  protected TestData testData;

  @Container
  public static PostgreSQLContainer<?> postgreSQL =
    new PostgreSQLContainer<>("postgres:13.2")
      .withUsername("testUsername")
      .withPassword("testPassword");

  @DynamicPropertySource
  public static void postgreSQLProperties(@NotNull DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQL::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQL::getUsername);
    registry.add("spring.datasource.password", postgreSQL::getPassword);
  }

  @BeforeEach
  public void initialiseRestAssuredMockMvcStandalone() {
    uri = "http://localhost:" + port;
  }
}
