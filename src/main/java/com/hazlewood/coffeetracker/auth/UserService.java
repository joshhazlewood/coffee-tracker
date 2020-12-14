package com.hazlewood.coffeetracker.auth;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
  void save(User user);
}
