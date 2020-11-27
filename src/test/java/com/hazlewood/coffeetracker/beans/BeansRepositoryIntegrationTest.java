package com.hazlewood.coffeetracker.beans;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class BeansRepositoryIntegrationTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private BeansRepository repository;

  @Test
  void injectedComponentsAreNotNull() {
    assertThat(entityManager).isNotNull();
    assertThat(repository).isNotNull();
  }

  @Test
  public void whenFindByName_ThenReturnEmployee() {
    Beans ancoats = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    entityManager.persistAndFlush(ancoats);

    Beans found = repository.findByName("Ancoats house blend").get();
    assertThat(found.getName()).isEqualTo(ancoats.getName());
  }

  @Test
  public void whenInvalidName_ThenReturnEmpty() {
    Optional<Beans> empty = repository.findByName("no name");
    assertThat(empty).isEmpty();
  }

  @Test
  public void whenFindById_ThenReturnBeans() {
    Beans ancoats = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    entityManager.persistAndFlush(ancoats);

    Beans found = repository.findById(ancoats.getId()).get();
    assertThat(found.getName()).isEqualTo(ancoats.getName());
  }

  @Test
  public void whenInvalidId_ThenReturnEmpty() {
    Optional<Beans> empty = repository.findById(-11L);
    assertThat(empty).isEmpty();
  }

  @Test
  public void givenSetOfBeans_WhenFindAll_ThenReturnAllBeans() {
    var ancoats = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    var atkinsons = new Beans("Atkinsons house blend", "Ancoats", "profile", "India");
    var neighbourhood = new Beans("Neighbourhood house blend", "Ancoats", "profile", "India");

    entityManager.persist(ancoats);
    entityManager.persist(atkinsons);
    entityManager.persist(neighbourhood);
    entityManager.flush();

    List<Beans> beansList = repository.findAll();

    assertThat(beansList)
        .hasSize(3)
        .extracting(Beans::getName)
        .containsOnly("Ancoats house blend", "Atkinsons house blend", "Neighbourhood house blend");
  }
}
