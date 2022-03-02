package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.CreateUserRequest;
import com.weareadaptive.auction.controller.dto.UpdateUserRequest;
import com.weareadaptive.auction.controller.dto.UserResponse;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

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

        return new UserResponse(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getOrganisation());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    User getById(@PathVariable("id") int id) {
        return userService.getById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    Collection<User> getAll() {
        return userService.getAll();
    }

    @PutMapping("/{id}")
    User updateById(@PathVariable("id") int id, @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        return userService.updateById(id, updateUserRequest.firstName(), updateUserRequest.lastName(), updateUserRequest.organisation());
    }

    @PutMapping("/{id}/block")
    void blockById(@PathVariable("id") int id) {
        userService.blockById(id);
    }

    @PutMapping("/{id}/unblock")
    void unblockById(@PathVariable("id") int id) {
        userService.unblockById(id);
    }
}
