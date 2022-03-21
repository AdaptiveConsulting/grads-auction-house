package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class  UserService {
  private final UserState userState;

  public UserService(UserState userState) {
    this.userState = userState;
  }

  public User create(
      String username,
      String password,
      String firstName,
      String lastName,
      String organisation) {
    User newUser = new User(
        userState.nextId(),
        username,
        password,
        firstName,
        lastName,
        organisation);
    userState.add(newUser);
    return newUser;
  }

  public User get(int id) {
    return userState.get(id);
  }

  public Stream<User> all() {
    return userState.stream();
  }

  public User update(
      int id,
      String firstName,
      String lastName,
      String organisation) {
    User user = userState.get(id);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setOrganisation(organisation);
    return user;
  }

  public void block(int id) {
    userState.get(id).block();
  }

  public void unblock(int id) {
    userState.get(id).unblock();
  }
}
