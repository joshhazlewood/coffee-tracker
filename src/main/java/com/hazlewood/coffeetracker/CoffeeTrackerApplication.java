package com.hazlewood.coffeetracker;

import com.hazlewood.coffeetracker.auth.DatabaseUserDetailsService;
import com.hazlewood.coffeetracker.beans.Beans;
import com.hazlewood.coffeetracker.beans.BeansRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CoffeeTrackerApplication {
  private static final Logger log = LoggerFactory.getLogger(CoffeeTrackerApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(CoffeeTrackerApplication.class, args);
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new DatabaseUserDetailsService();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
	@Profile("!test")
  public CommandLineRunner demo(BeansRepository repository) {
    return (args) -> {
      // save some beans
      repository.save(new Beans("India", "Ancoats", "profile", "India"));
      repository.save(new Beans("Ethiopia", "Ancoats", "profile", "India"));
      repository.save(new Beans("Guatemala", "Ancoats", "profile", "India"));
      repository.save(new Beans("Kenya", "Ancoats", "profile", "India"));

      // fetch all beans
      log.info("Beans found with findAll()");
      log.info("-----------------------------");
      for (Beans bean : repository.findAll()) {
        log.info(bean.toString());
      }
      log.info("");

      // fetch by id
      Beans beans = repository.findById(1L).get();
      log.info("Beans found with findById(1L)");
      log.info("-----------------------------");
      log.info(beans.toString());
      log.info("");

      // fetch bean by name
      log.info("Beans found with findByName(Kenya)");
      log.info("-----------------------------");
      repository.findByName("Kenya").ifPresent(bean -> log.info(bean.toString()));
      log.info("");
    };
  }
}
