package com.hazlewood.coffeetracker.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static Logger log = LoggerFactory.getLogger(AuthController.class);

  @Autowired private DatabaseUserDetailsService userDetailsService;
  @Autowired private UserRepository userRepository;
  @Autowired private BCryptPasswordEncoder encoder;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public void register(@Valid @RequestBody User user) {

    userDetailsService.save(user);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public String login(@Valid @RequestBody User user, HttpServletResponse res) {
    User loadedUser =
        Optional.ofNullable(userDetailsService.loadUserByUsername(user.getUsername()))
            .orElseThrow(
                () -> new UsernameNotFoundException("Error while attempting to find user"));

    if (passwordsMatch(user.getPassword(), loadedUser.getPassword())) {
      var token = createToken(loadedUser.getUsername());
      res.setHeader("Authorization", "Authorization: Bearer " + token);
      return "Success";
    } else {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED, "Invalid username and/or password.");
    }
  }

  private boolean passwordsMatch(String attempt, String actual) {
    return encoder.matches(attempt, actual);
  }

  private String createToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setExpiration(Date.from(Instant.now().plusSeconds(5 * 60)))
        .signWith(Keys.hmacShaKeyFor(("placeholder_key" + UUID.randomUUID()).getBytes()))
        .compact();
  }
}
