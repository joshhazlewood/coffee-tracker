package com.hazlewood.coffeetracker.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
  void save(User user);

  @Override
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
