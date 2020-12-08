package com.hazlewood.coffeetracker.purchases;

import com.hazlewood.coffeetracker.beans.Beans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
class BeansPurchaseServiceImplIntegrationTest {
  Logger log = LoggerFactory.getLogger(BeansPurchaseServiceImplIntegrationTest.class);

  @TestConfiguration
  static class StockItemServiceImplTestContextConfiguration {
    @Bean
    public BeansPurchaseService stockItemService() {
      return new BeansPurchaseServiceImpl();
    }
  }

  @Autowired
  private BeansPurchaseService beansPurchaseService;

  @MockBean
  private BeansPurchaseRepository beansPurchaseRepository;

  @BeforeEach
  public void setUp() {
    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    beans.setId(10L);
    var stock1 = new BeansPurchase(beans, BigDecimal.valueOf(1000));
    stock1.setId(1L);
    var stock2 = new BeansPurchase(beans, BigDecimal.valueOf(250));
    stock2.setId(2L);
    var stock3 = new BeansPurchase(beans, BigDecimal.valueOf(500));
    stock3.setId(3L);
    var stockList = Arrays.asList(stock1, stock2, stock3);
    

    when(beansPurchaseRepository.findAll()).thenReturn(stockList);
    when(beansPurchaseRepository.findById(1L)).thenReturn(Optional.of(stock1));
  }

  @Test
  public void givenItemFound_WhenFindingById_ThenReturnBeans() {
    Optional<BeansPurchase> item = beansPurchaseService.findById(1L);

    assertThat(item).isNotEmpty();
    assertThat(item.get().getId()).isEqualTo(1L);
    assertThat(item.get().getCurrentQuantity()).isEqualTo(BigDecimal.valueOf(1000));
    assertThat(item.get().getInitialQuantity()).isEqualTo(BigDecimal.valueOf(1000));
    assertThat(item.get().getBeans()).isNotNull();
    verifyFindByIdIsCalledOnce(1L);
  }

  @Test
  public void givenNonFound_WhenFindingById_ThenReturnEmptyOptional() {
    Optional<BeansPurchase> item = beansPurchaseService.findById(11L);
    assertThat(item).isEmpty();
    verifyFindByIdIsCalledOnce(11L);
  }

  @Test
  public void whenFindingAll_ThenSuccess() {
    List<BeansPurchase> items = beansPurchaseService.getAll();
    assertThat(items)
        .hasSize(3)
        .extracting(BeansPurchase::getInitialQuantity)
        .containsOnly(BigDecimal.valueOf(1000), BigDecimal.valueOf(250), BigDecimal.valueOf(500));
    verifyFindAllIsCalledOnce();
  }

  @Test
  public void whenSavingItem_ThenSuccess() {
    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    beans.setId(1L);
    BeansPurchase item = new BeansPurchase(beans, BigDecimal.valueOf(250));
    item.setId(1L);

    when(beansPurchaseRepository.save(any(BeansPurchase.class))).thenReturn(item);
    BeansPurchase savedItem = beansPurchaseService.save(item);

    assertThat(savedItem).isNotNull();
    assertThat(savedItem.getCurrentQuantity()).isEqualTo(BigDecimal.valueOf(250));
    assertThat(savedItem.getInitialQuantity()).isEqualTo(BigDecimal.valueOf(250));
    assertThat(savedItem.getBeans()).isEqualTo(beans);

    verifySaveIsCalledOnce();
  }

  @Test
  public void whenSettingCurrentQuantity_ThenSuccess() {
    when(beansPurchaseRepository.setCurrentQuantity(any(BigDecimal.class), anyLong())).thenReturn(1);
    boolean success = beansPurchaseService.setCurrentQuantity(BigDecimal.valueOf(231.5), 2L);

    assertThat(success).isTrue();
    verifySetCurrentQuantityIsCalledOnce();
  }

  @Test
  public void givenNonExistingId_WhenSettingCurrentQuantity_ThenFailure() {
    boolean failure = beansPurchaseService.setCurrentQuantity(BigDecimal.valueOf(231.5), -1L);
    assertThat(failure).isFalse();
    verifySetCurrentQuantityIsCalledOnce();
  }

  @Test
  public void givenInvalidQuantity_WhenSettingCurrentQuantity_ThenFailure() {
    boolean failure = beansPurchaseService.setCurrentQuantity(BigDecimal.valueOf(-10.23), 1L);
    assertThat(failure).isFalse();
    verifySetCurrentQuantityIsNotCalled();
  }

  @Test
  public void givenZeroQuantity_WhenSettingCurrentQuantity_ThenSuccess() {
    when(beansPurchaseRepository.setCurrentQuantity(any(BigDecimal.class), anyLong())).thenReturn(1);
    boolean failure = beansPurchaseService.setCurrentQuantity(BigDecimal.valueOf(0.00), 1L);
    assertThat(failure).isTrue();
    verifySetCurrentQuantityIsCalledOnce();
  }

  private void verifyFindByIdIsCalledOnce(Long id) {
    Mockito.verify(beansPurchaseRepository, Mockito.times(1)).findById(id);
  }

  private void verifyFindAllIsCalledOnce() {
    Mockito.verify(beansPurchaseRepository, Mockito.times(1)).findAll();
  }

  private void verifySaveIsCalledOnce() {
    Mockito.verify(beansPurchaseRepository, times(1)).save(any(BeansPurchase.class));
  }

  private void verifySetCurrentQuantityIsCalledOnce() {
    verifySetCurrentQuantityIsCalled(1);
  }

  private void verifySetCurrentQuantityIsNotCalled() {
    verifySetCurrentQuantityIsCalled(0);
  }

  private void verifySetCurrentQuantityIsCalled(int times) {
    verify(beansPurchaseRepository, times(times)).setCurrentQuantity(any(BigDecimal.class), anyLong());
  }
}