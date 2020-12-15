package com.hazlewood.coffeetracker.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserService {
  private static Logger log = LoggerFactory.getLogger(DatabaseUserDetailsService.class);
  @Autowired private UserRepository userRepository;
  @Autowired private BCryptPasswordEncoder encoder;

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Unable to find user with name " + username));
  }

  public void save(User user) {
    log.info("pw before encoding: {}", user.getPassword());
    user.setPassword(encoder.encode(user.getPassword()));
    userRepository.save(user);
    log.info("Saved user: {} : {}", user.getUsername(), user.getPassword());
  }

  public List<User> getAll() {
    return userRepository.findAll();
  }
}
