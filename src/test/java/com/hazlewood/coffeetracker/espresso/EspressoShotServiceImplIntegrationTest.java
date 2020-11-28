package com.hazlewood.coffeetracker.espresso;

import com.hazlewood.coffeetracker.beans.Beans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
class EspressoShotServiceImplIntegrationTest {

  @TestConfiguration
  static class EspressoShotServiceImplTestContextConfiguration {
    @Bean
    public EspressoShotService EspressoShotService() {
      return new EspressoShotServiceImpl();
    }
  }

  @Autowired private EspressoShotService service;

  @MockBean
  private EspressoShotRepository repository;

  @BeforeEach
  public void setUp() {
    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    beans.setId(10L);
    var shot1 = new EspressoShot(beans, BigDecimal.valueOf(18.4), BigDecimal.valueOf(35.3), 25, 5, "");
    shot1.setId(1L);
    var shot2 = new EspressoShot(beans, BigDecimal.valueOf(18.7), BigDecimal.valueOf(37.3), 27, 5, "");
    shot2.setId(2L);
    var shot3 = new EspressoShot(beans, BigDecimal.valueOf(18.4), BigDecimal.valueOf(34.3), 31, 4, "");
    shot3.setId(3L);

    var shotList = Arrays.asList(shot1, shot2, shot3);

    when(repository.findAll()).thenReturn(shotList);
    when(repository.findById(1L)).thenReturn(Optional.of(shot1));
  }

  @Test
  public void whenFindingById_ThenSuccess() {
    Optional<EspressoShot> shot = service.findById(1L);

    assertThat(shot).isNotEmpty();
    assertThat(shot.get().getId()).isEqualTo(1L);
    assertThat(shot.get().getBeans().getName()).isEqualTo("Ancoats house blend");
    assertThat(shot.get().getInputWeight()).isEqualTo(BigDecimal.valueOf(18.4));

    verifyFindByIdIsCalledOnce();
  }

  @Test
  public void givenNonFound_WhenFindingById_ThenEmpty() {
    Optional<EspressoShot> shot = service.findById(-1L);
    assertThat(shot).isEmpty();
    verifyFindByIdIsCalledOnce();
  }

  @Test
  public void whenFindingAll_ThenSuccess() {
    List<EspressoShot> shots = service.getAll();
    assertThat(shots)
        .hasSize(3)
        .extracting(EspressoShot::getInputWeight)
        .containsOnly(BigDecimal.valueOf(18.4), BigDecimal.valueOf(18.7), BigDecimal.valueOf(18.4));
    verifyFindAllIsCalledOnce();
  }

  @Test
  public void whenSavingShot_ThenSuccess() {
    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    beans.setId(10L);
    var shot = new EspressoShot(beans, BigDecimal.valueOf(18.4), BigDecimal.valueOf(37.9), 32, 4, "");

    when(repository.save(any(EspressoShot.class))).thenReturn(shot);

    EspressoShot savedShot = service.save(shot);

    assertThat(savedShot).isNotNull();
    assertThat(savedShot.getInputWeight()).isEqualTo(BigDecimal.valueOf(18.4));
    assertThat(savedShot.getOutputWeight()).isEqualTo(BigDecimal.valueOf(37.9));
    assertThat(savedShot.getTimeInSeconds()).isEqualTo(32);
    assertThat(savedShot.getGrindSetting()).isEqualTo(4);
    assertThat(savedShot.getNotes()).isEqualTo("");

    verifySaveIsCalledOnce();
  }

  private void verifyFindByIdIsCalledOnce() {
    verify(repository, times(1)).findById(anyLong());
  }

  private void verifyFindAllIsCalledOnce() {
    verify(repository, times(1)).findAll();
  }

  private void verifySaveIsCalledOnce() {
    verify(repository, times(1)).save(any(EspressoShot.class));
  }
}