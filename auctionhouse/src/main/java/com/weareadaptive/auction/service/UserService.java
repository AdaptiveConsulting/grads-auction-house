package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserState userState;

    public UserService(UserState userState) {
        this.userState = userState;
    }

    public User create(String username, String password, String firstName, String lastName,
                       String organisation) {
        var user = new User(userState.nextId(), username, password, firstName, lastName, organisation);
        userState.add(user);

        return user;
    }

    public Optional<User> getById(int id) {
        return userState.stream().filter(u -> u.getId() == id).findFirst();
    }

    public Optional<User> updateById(int id, String firstName, String lastName, String organisation) {
        Optional<User> user = getById(id);

        if (user.isPresent()) {
            user.get().setFirstName(firstName);
            user.get().setLastName(lastName);
            user.get().setOrganisation(organisation);
        }

        return user;
    }
}
