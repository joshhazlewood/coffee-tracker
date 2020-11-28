package com.hazlewood.coffeetracker.espresso;

import com.hazlewood.coffeetracker.beans.BeansRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EspressoShotRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired private EspressoShotRepository repository;

  @Test
  void injectedComponentsAreNotNull() {
    assertThat(entityManager).isNotNull();
    assertThat(repository).isNotNull();
  }

}