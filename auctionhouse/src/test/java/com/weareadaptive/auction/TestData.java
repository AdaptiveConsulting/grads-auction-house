package com.weareadaptive.auction;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.AuctionLotService;
import com.weareadaptive.auction.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TestData {
    public static final String PASSWORD = "mypassword";
    public static final String ADMIN_AUTH_TOKEN = "Bearer ADMIN:adminpassword";
    public static final String USER_AUTH_TOKEN = "Bearer USER:userpassword";

    private final UserService userService;
    private final AuctionLotService auctionLotService;
    private final Faker faker;
    private User user1;
    private User user2;
    private User user3;
    private User user4;

    private AuctionLot auction1;
    private AuctionLot auction2;
    private AuctionLot auction3;
    private AuctionLot auction4;

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

        auction1 = createRandomAuction();
        auction2 = createRandomAuction();
        auction3 = createRandomAuction();
        auction4 = createRandomAuction();
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


    public AuctionLot auction1() {
        return auction1;
    }

    public AuctionLot auction2() {
        return auction2;
    }

    public AuctionLot auction3() {
        return auction3;
    }

    public AuctionLot auction4() {
        return auction4;
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

    public AuctionLot createRandomAuction() {
        var stock = faker.stock();
        var auctionLot = auctionLotService.create(
                user1,
                stock.nsdqSymbol(),
                faker.number().randomDigit(),
                faker.random().nextDouble()
        );
        return auctionLot;

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
