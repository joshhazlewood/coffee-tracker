package com.hazlewood.coffeetracker.stock;

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
class StockItemServiceImplIntegrationTest {
  Logger log = LoggerFactory.getLogger(StockItemServiceImplIntegrationTest.class);

  @TestConfiguration
  static class StockItemServiceImplTestContextConfiguration {
    @Bean
    public StockItemService stockItemService() {
      return new StockItemServiceImpl();
    }
  }

  @Autowired
  private StockItemService stockItemService;

  @MockBean
  private StockItemRepository stockItemRepository;

  @BeforeEach
  public void setUp() {
    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    beans.setId(10L);
    var stock1 = new StockItem(beans, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
    stock1.setId(1L);
    var stock2 = new StockItem(beans, BigDecimal.valueOf(250), BigDecimal.valueOf(250));
    stock2.setId(2L);
    var stock3 = new StockItem(beans, BigDecimal.valueOf(500), BigDecimal.valueOf(500));
    stock3.setId(3L);
    var stockList = Arrays.asList(stock1, stock2, stock3);
    

    when(stockItemRepository.findAll()).thenReturn(stockList);
    when(stockItemRepository.findById(1L)).thenReturn(Optional.of(stock1));
  }

  @Test
  public void givenItemFound_WhenFindingById_ThenReturnBeans() {
    Optional<StockItem> item = stockItemService.findById(1L);

    assertThat(item).isNotEmpty();
    assertThat(item.get().getId()).isEqualTo(1L);
    assertThat(item.get().getCurrentQuantity()).isEqualTo(BigDecimal.valueOf(1000));
    assertThat(item.get().getInitialQuantity()).isEqualTo(BigDecimal.valueOf(1000));
    assertThat(item.get().getBeans()).isNotNull();
    verifyFindByIdIsCalledOnce(1L);
  }

  @Test
  public void givenNonFound_WhenFindingById_ThenReturnEmptyOptional() {
    Optional<StockItem> item = stockItemService.findById(11L);
    assertThat(item).isEmpty();
    verifyFindByIdIsCalledOnce(11L);
  }

  @Test
  public void whenFindingAll_ThenSuccess() {
    List<StockItem> items = stockItemService.getAll();
    assertThat(items)
        .hasSize(3)
        .extracting(StockItem::getInitialQuantity)
        .containsOnly(BigDecimal.valueOf(1000), BigDecimal.valueOf(250), BigDecimal.valueOf(500));
    verifyFindAllIsCalledOnce();
  }

  @Test
  public void whenSavingItem_ThenSuccess() {
    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    beans.setId(1L);
    StockItem item = new StockItem(beans, BigDecimal.valueOf(250), BigDecimal.valueOf(250));
    item.setId(1L);

    when(stockItemRepository.save(any(StockItem.class))).thenReturn(item);
    StockItem savedItem = stockItemService.save(item);

    assertThat(savedItem).isNotNull();
    assertThat(savedItem.getCurrentQuantity()).isEqualTo(BigDecimal.valueOf(250));
    assertThat(savedItem.getInitialQuantity()).isEqualTo(BigDecimal.valueOf(250));
    assertThat(savedItem.getBeans()).isEqualTo(beans);

    verifySaveIsCalledOnce();
  }

  @Test
  public void whenSettingCurrentQuantity_ThenSuccess() {
    when(stockItemRepository.setCurrentQuantity(any(BigDecimal.class), anyLong())).thenReturn(1);
    boolean success = stockItemService.setCurrentQuantity(BigDecimal.valueOf(231.5), 2L);

    assertThat(success).isTrue();
    verifySetCurrentQuantityIsCalledOnce();
  }

  @Test
  public void givenNonExistingId_WhenSettingCurrentQuantity_ThenFailure() {
    boolean failure = stockItemService.setCurrentQuantity(BigDecimal.valueOf(231.5), -1L);
    assertThat(failure).isFalse();
    verifySetCurrentQuantityIsCalledOnce();
  }

  @Test
  public void givenInvalidQuantity_WhenSettingCurrentQuantity_ThenFailure() {
    boolean failure = stockItemService.setCurrentQuantity(BigDecimal.valueOf(-10.23), 1L);
    assertThat(failure).isFalse();
    verifySetCurrentQuantityIsNotCalled();
  }

  @Test
  public void givenZeroQuantity_WhenSettingCurrentQuantity_ThenSuccess() {
    when(stockItemRepository.setCurrentQuantity(any(BigDecimal.class), anyLong())).thenReturn(1);
    boolean failure = stockItemService.setCurrentQuantity(BigDecimal.valueOf(0.00), 1L);
    assertThat(failure).isTrue();
    verifySetCurrentQuantityIsCalledOnce();
  }

  private void verifyFindByIdIsCalledOnce(Long id) {
    Mockito.verify(stockItemRepository, Mockito.times(1)).findById(id);
  }

  private void verifyFindAllIsCalledOnce() {
    Mockito.verify(stockItemRepository, Mockito.times(1)).findAll();
  }

  private void verifySaveIsCalledOnce() {
    Mockito.verify(stockItemRepository, times(1)).save(any(StockItem.class));
  }

  private void verifySetCurrentQuantityIsCalledOnce() {
    verifySetCurrentQuantityIsCalled(1);
  }

  private void verifySetCurrentQuantityIsNotCalled() {
    verifySetCurrentQuantityIsCalled(0);
  }

  private void verifySetCurrentQuantityIsCalled(int times) {
    verify(stockItemRepository, times(times)).setCurrentQuantity(any(BigDecimal.class), anyLong());
  }
}