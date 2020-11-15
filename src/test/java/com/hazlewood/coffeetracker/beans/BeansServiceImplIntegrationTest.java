package com.hazlewood.coffeetracker.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class BeansServiceImplIntegrationTest {

  Logger log = LoggerFactory.getLogger(BeansServiceImplIntegrationTest.class);

  @TestConfiguration
  static class BeansServiceImplTestContextConfiguration {
    @Bean
    public BeansService beansService() {
      return new BeansServiceImpl();
    }
  }

  @Autowired private BeansService beansService;

  @MockBean private BeansRepository beansRepository;

  @BeforeEach
  public void setUp() {
    var ancoats = new Beans("Ancoats house blend", BigDecimal.valueOf(500));
    ancoats.setId(10L);
    log.info(ancoats.toString());
    var atkinsons = new Beans("Atkinsons house blend", BigDecimal.valueOf(500));
    var neighbourhood = new Beans("Neighbourhood house blend", BigDecimal.valueOf(500));

    List<Beans> allBeans = Arrays.asList(ancoats, atkinsons, neighbourhood);

    Mockito.when(beansRepository.findAll()).thenReturn(allBeans);
    Mockito.when(beansRepository.findByName(ancoats.getName())).thenReturn(Optional.of(ancoats));
    Mockito.when(beansRepository.findById(ancoats.getId())).thenReturn(Optional.of(ancoats));
    Mockito.when(beansRepository.findByName(atkinsons.getName()))
        .thenReturn(Optional.of(atkinsons));
    Mockito.when(beansRepository.findByName(neighbourhood.getName()))
        .thenReturn(Optional.of(neighbourhood));
  }

  @Test
  public void whenValidName_ThenBeansShouldBeFound() {
    var name = "Ancoats house blend";
    Beans found = beansService.findByName(name).get();
    assertThat(found.getName()).isEqualTo(name);
  }

  @Test
  public void whenInValidName_ThenBeansShouldNotBeFound() {
    Optional<Beans> found = beansService.findByName("non existing name");
    assertThat(found).isEqualTo(Optional.empty());

    verifyFindByNameIsCalledOnce("non existing name");
  }

  @Test
  public void whenValidId_ThenBeansShouldBeFound() {
    Beans found = beansService.getBeansById(10L).get();
    assertThat(found.getName()).isEqualTo("Ancoats house blend");

    verifyFindByIdCalledOnce();
  }

  @Test
  public void whenInValidId_ThenBeansShouldNotBeFound() {
    Optional<Beans> found = beansService.getBeansById(11L);
    assertThat(found).isEqualTo(Optional.empty());

    verifyFindByIdCalledOnce();
  }

  @Test
  public void whenValidName_ThenBeansShouldExist() {
    boolean exists = beansService.exists("Ancoats house blend");
    assertThat(exists).isTrue();

    verifyFindByNameIsCalledOnce("Ancoats house blend");
  }

  @Test
  public void whenNonExistingName_ThenBeansShouldExist() {
    boolean exists = beansService.exists("Non-existing house blend");
    assertThat(exists).isFalse();

    verifyFindByNameIsCalledOnce("Non-existing house blend");
  }

  @Test
  public void given3Beans_WhenGetAll_ThenReturn3Records() {
    List<Beans> beansList = beansService.getAllBeans();
    assertThat(beansList)
        .hasSize(3)
        .extracting(Beans::getName)
        .contains("Ancoats house blend", "Atkinsons house blend", "Neighbourhood house blend");
    verifyFindAllCalledOnce();
  }

  private void verifyFindByNameIsCalledOnce(String name) {
    Mockito.verify(beansRepository, Mockito.times(1)).findByName(name);
    Mockito.reset(beansRepository);
  }

  private void verifyFindByIdCalledOnce() {
    Mockito.verify(beansRepository, Mockito.times(1)).findById(Mockito.anyLong());
    Mockito.reset(beansRepository);
  }

  private void verifyFindAllCalledOnce() {
    Mockito.verify(beansRepository, Mockito.times(1)).findAll();
    Mockito.reset(beansRepository);
  }
}
