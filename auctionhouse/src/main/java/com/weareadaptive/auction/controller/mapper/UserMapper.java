package com.weareadaptive.auction.controller.mapper;

import com.weareadaptive.auction.controller.dto.users.UserResponse;
import com.weareadaptive.auction.model.User;

public class UserMapper {
  public static UserResponse map(User user) {
    return new UserResponse(
      user.getId(),
      user.getUsername(),
      user.getFirstName(),
      user.getLastName(),
      user.getOrganisation());
  }
}
