package com.weareadaptive.auction.controller;

import static com.weareadaptive.auction.controller.mapper.UserMapper.map;

import com.weareadaptive.auction.controller.dto.users.CreateUserRequest;
import com.weareadaptive.auction.controller.dto.users.UpdateUserRequest;
import com.weareadaptive.auction.controller.dto.users.UserResponse;
import com.weareadaptive.auction.controller.mapper.UserMapper;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.UserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse create(@RequestBody @Valid CreateUserRequest createUserRequest) {
    User user = userService.create(
        createUserRequest.username(),
        createUserRequest.password(),
        createUserRequest.firstName(),
        createUserRequest.lastName(),
        createUserRequest.organisation());
    return map(user);
  }

  @GetMapping("/users/{id}")
  public UserResponse get(@PathVariable int id) {
    User user = userService.get(id);
    return map(user);
  }

  @GetMapping("/users")
  public List<UserResponse> all() {
    return userService.all()
      .map(UserMapper::map)
      .toList();
  }

  @PutMapping("/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponse update(
      @RequestBody @Valid UpdateUserRequest updateUserRequest,
      @PathVariable int id) {
    User user = userService.update(
        id,
        updateUserRequest.firstName(),
        updateUserRequest.lastName(),
        updateUserRequest.organisation());
    return map(user);
  }

  @PutMapping("/users/{id}/block")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void block(@PathVariable int id) {
    userService.block(id);
  }

  @PutMapping("/users/{id}/unblock")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unblock(@PathVariable int id) {
    userService.unblock(id);
  }
}
