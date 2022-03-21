package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.model.user.AuctionUser;
import com.weareadaptive.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit {

  @Autowired
  private UserRepository userRepository;

  public ApplicationInit(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createInitData() {
    var admin = AuctionUser.builder()
        .username("ADMIN")
        .password("adminpassword")
        .firstName("admin")
        .lastName("admin")
        .organization("Adaptive")
        .isAdmin(true)
        .isBlocked(false)
        .build();
    userRepository.save(admin);
  }

}
