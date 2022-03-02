package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {
    private final UserState userState;

    public UserService(UserState userState) {
        this.userState = userState;
    }

    public User create(String username, String password, String firstName, String lastName,
                       String organisation) {
        if (userState.getUsernameIndex().containsKey(username)) {
            throw new BusinessException("username already exist");
        }

        var user = new User(userState.nextId(), username, password, firstName, lastName, organisation);
        userState.add(user);

        return user;
    }

    public User getById(int id) {
        Optional<User> user = userState.stream().filter(u -> u.getId() == id).findFirst();

        if (user.isEmpty()) {
            throw new ObjectNotFoundException();
        } else {
            return user.get();
        }
    }

    public User updateById(int id, String firstName, String lastName, String organisation) {
        User user = getById(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setOrganisation(organisation);

        return user;
    }

    public ResponseEntity<HttpStatus>  blockById(int id) {
        User user = getById(id);

        /*
        if (user.isBlocked()) {
            throw new BusinessException("user is already blocked");
        }
         */

        user.block();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> unblockById(int id) {
        User user = getById(id);

        /*
        if (!user.isBlocked()) {
            throw new BusinessException("user is already unblocked");
        }
         */

        user.unblock();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public Collection<User> getAll() {
        return userState.getUsernameIndex().values();
    }
}
