package com.hazlewood.coffeetracker.stock;

import com.hazlewood.coffeetracker.beans.Beans;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class StockItemRepositoryIntegrationTest {

  Logger log = LoggerFactory.getLogger(StockItemRepositoryIntegrationTest.class);

  @Autowired
  private TestEntityManager entityManager;

  @Autowired private StockItemRepository repository;

  @Test
  void injectedComponentsAreNotNull() {
    assertThat(entityManager).isNotNull();
    assertThat(repository).isNotNull();
  }

  @Test
  public void whenFindingById_ThenReturnStockItem() {
    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    Beans savedBeans = entityManager.persistAndFlush(beans);

    var stock = new StockItem(savedBeans, BigDecimal.valueOf(500), BigDecimal.valueOf(500));
    StockItem savedStock = entityManager.persistAndFlush(stock);

    var found = repository.findById(savedStock.getId());
    assertThat(found).isNotEmpty();
    assertThat(found).contains(savedStock);
  }

  @Test
  public void givenNonExistingStockItem_WhenFindingById_ThenNonFound() {
    Optional<StockItem> item = repository.findById(10L);
    assertThat(item).isEmpty();
  }

  @Test
  public void whenSavingItem_ThenSuccess() {
    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    Beans savedBeans = entityManager.persistAndFlush(beans);

    var stock = new StockItem(savedBeans, BigDecimal.valueOf(500), BigDecimal.valueOf(500));
    StockItem savedStock = repository.save(stock);
    assertThat(savedStock).isNotNull();
    assertThat(savedStock.getId()).isNotNull();
  }

  @Test
  public void whenUpdatingCurrentQuantity_ThenSuccess() {
    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
    Beans savedBeans = entityManager.persistAndFlush(beans);

    var stock = new StockItem(savedBeans, BigDecimal.valueOf(500), BigDecimal.valueOf(500));
    StockItem savedStock = entityManager.persistAndFlush(stock);

    int updatedRows = repository.setCurrentQuantity(BigDecimal.valueOf(200), savedStock.getId());
    assertThat(updatedRows).isEqualTo(1);

    Optional<StockItem> updatedItem = repository.findById(savedStock.getId());
    assertThat(updatedItem).isNotEmpty();
    assertThat(updatedItem.get().getCurrentQuantity().setScale(2)).isEqualTo(BigDecimal.valueOf(200).setScale(2));
  }

//  @Test
//  public void givenInvalidAmount_WhenUpdatingCurrentQuantity_ThenError() {
//    var beans = new Beans("Ancoats house blend", "Ancoats", "profile", "India");
//    Beans savedBeans = entityManager.persistAndFlush(beans);
//
//    var stock = new StockItem(savedBeans, BigDecimal.valueOf(500), BigDecimal.valueOf(500));
//    StockItem savedStock = entityManager.persistAndFlush(stock);
//
//    int updatedRows = repository.setCurrentQuantity(BigDecimal.valueOf(-1.0), savedStock.getId());
//    repository.findAll().forEach(item -> log.info(item.toString()));
//    assertThat(updatedRows).isEqualTo(0);
//  }

}
