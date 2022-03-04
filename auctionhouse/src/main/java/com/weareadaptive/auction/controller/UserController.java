package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.CreateUserRequest;
import com.weareadaptive.auction.controller.dto.UpdateUserRequest;
import com.weareadaptive.auction.controller.dto.UserMapper;
import com.weareadaptive.auction.controller.dto.UserResponse;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.UserService;
import java.util.ArrayList;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  UserResponse create(@RequestBody @Valid CreateUserRequest createUserRequest) {
    User user = userService.create(
        createUserRequest.username(),
        createUserRequest.password(),
        createUserRequest.firstName(),
        createUserRequest.lastName(),
        createUserRequest.organisation());
    return UserMapper.map(user);
  }

  @GetMapping("/{id}")
  UserResponse getById(@PathVariable int id) {
    return UserMapper.map(userService.getById(id));
  }

  @GetMapping
  ArrayList<UserResponse> getAll() {
    return UserMapper.mapAll(userService.getAll());
  }

  @PutMapping("/{id}")
  UserResponse updateById(@PathVariable int id,
                          @RequestBody @Valid UpdateUserRequest userRequest) {
    return UserMapper.map(userService.updateById(
        id, userRequest.firstName(), userRequest.lastName(), userRequest.organisation()
    ));
  }

  @PutMapping("/{id}/block")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void blockById(@PathVariable int id) {
    userService.blockById(id);
  }

  @PutMapping("/{id}/unblock")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void unblockById(@PathVariable int id) {
    userService.unblockById(id);
  }
}
