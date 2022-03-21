package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {
  public static UserResponse map(User user) {
    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getFirstName(),
        user.getLastName(),
        user.getOrganisation());
  }

  public static ArrayList<UserResponse> mapAll(List<User> users) {
    return new ArrayList<>(users.stream().map(UserMapper::map).toList());
  }
}
