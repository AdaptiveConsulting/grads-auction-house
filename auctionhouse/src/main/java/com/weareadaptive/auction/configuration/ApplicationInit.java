package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.model.User;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit {
  private final UserState userState;

  public ApplicationInit(UserState userState) {
    this.userState = userState;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void createInitData() {
    var admin = new User(
        userState.nextId(),
        "ADMIN",
        "adminpassword",
        "admin",
        "admin",
        "Adaptive",
        true);
    userState.add(admin);
  }

}
