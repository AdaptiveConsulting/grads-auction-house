package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.CreateUserRequest;
import com.weareadaptive.auction.controller.dto.UpdateUserRequest;
import com.weareadaptive.auction.controller.dto.UserResponse;
import com.weareadaptive.auction.exception.ExceptionHandlerControllerAdvice;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

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
  UserResponse createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
    User user = userService.create(
            createUserRequest.username(),
            createUserRequest.password(),
            createUserRequest.firstName(),
            createUserRequest.lastName(),
            createUserRequest.organisation());

    return new UserResponse(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getOrganisation());
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Optional<User> getUserById(@PathVariable("id") int id) {
    Optional<User> user = userService.getById(id);

    if (user.isPresent()) {

    }

    return userService.getById(id);
  }

  @PutMapping("/{id}")
  Optional<User> updateUserById(@PathVariable("id") int id, @RequestBody @Valid UpdateUserRequest updateUserRequest, HttpServletResponse servletResponse) {
    Optional<User> user = userService.updateById(id, updateUserRequest.firstName(), updateUserRequest.lastName(), updateUserRequest.organisation());

    if (user.isPresent()) {
      servletResponse.setStatus(HttpServletResponse.SC_OK);
    } else {
      servletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    return user;
  }

  @PutMapping("/{id}/block")
  void blockUserById(@PathVariable("id") int id, HttpServletResponse servletResponse) {
    Optional<User> user = userService.getById(id);

    if (user.isEmpty()) {
      servletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else if (!user.get().isBlocked()) {
      user.get().block();
      servletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
  }
}
