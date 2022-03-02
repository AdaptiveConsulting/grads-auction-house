package com.weareadaptive.auction.model;

import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class UserState extends State<User> {

  private final Map<String, User> usernameIndex;

  public UserState() {
    usernameIndex = new HashMap<>();
  }

  @Override
  protected void onAdd(User model) {
    if (usernameIndex.containsKey(model.getUsername())) {
      throw new BusinessException(format("Username \"%s\" already exist", model.getUsername()));
    }

    usernameIndex.put(model.getUsername(), model);
  }

  public Optional<User> getByUsername(@NotNull String username) {
    return Optional.ofNullable(usernameIndex.get(username));
  }

  public Optional<User> validateUsernamePassword(String username, String password) {
    if (!usernameIndex.containsKey(username)) {
      return Optional.empty();
    }
    var user = usernameIndex.get(username);
    if (!user.validatePassword(password)) {
      return Optional.empty();
    }
    return Optional.of(user);
  }

  public List<String> findOrganisations() {
    return stream()
        .filter(u -> !u.isAdmin())
        .map(User::getOrganisation)
        .distinct()
        .sorted()
        .toList();
  }

  public List<OrganisationDetails> getOrganisationsDetails() {
    return stream()
        .filter(u -> !u.isAdmin())
        .collect(groupingBy(User::getOrganisation))
        .entrySet()
        .stream()
        .map(e -> new OrganisationDetails(e.getKey(), e.getValue()))
        .sorted(comparing(OrganisationDetails::organisationName))
        .toList();
  }

  public Map<String, User> getUsernameIndex() {
    return usernameIndex;
  }
}
