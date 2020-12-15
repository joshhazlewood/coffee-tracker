package com.hazlewood.coffeetracker.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest {
  @TestConfiguration
  static class UserServiceTestContextConfiguration {
    @Bean
    public UserService userService() {
      return new DatabaseUserDetailsService();
    }
  }

  @Autowired private UserService userService;

  @MockBean private UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    var user = new User("test@test.com", "password");
    Mockito.when(userRepository.findByUsername("test@test.com")).thenReturn(Optional.of(user));
  }

  @Test
  public void givenNameExists_WhenLoadingUserByUsername_ThenSuccess() {
    var found = (User) userService.loadUserByUsername("test@test.com");
    assertThat(found.getUsername()).isEqualTo("test@test.com");
    assertThat(found.getPassword()).isEqualTo("password");
    verifyFindByUsernameCalledOnce();
  }

  @Test
  public void givenNameDoesNotExist_WhenLoadingUserByUsername_ThenEmpty() {
    UsernameNotFoundException exception =
        assertThrows(
            UsernameNotFoundException.class,
            () -> userService.loadUserByUsername("no-match@test.com"));
    assertThat(exception.getMessage()).isEqualTo("Unable to find user with name no-match@test.com");
    verifyFindByUsernameCalledOnce();
  }

  @Test
  public void whenSavingUser_ThenSuccess() {
    var user = new User("test@test.com", "password");
    userService.save(user);
    verifySaveCalledOnce();
  }

  private void verifyFindByUsernameCalledOnce() {
    Mockito.verify(userRepository, Mockito.times(1)).findByUsername(anyString());
    Mockito.reset(userRepository);
  }

  private void verifySaveCalledOnce() {
    Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    Mockito.reset(userRepository);
  }
}
