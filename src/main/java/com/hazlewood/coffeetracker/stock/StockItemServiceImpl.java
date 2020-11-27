package com.hazlewood.coffeetracker.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StockItemServiceImpl implements StockItemService {

  Logger log = LoggerFactory.getLogger(StockItemServiceImpl.class);

  @Autowired StockItemRepository repository;

  @Override
  public Optional<StockItem> findById(Long id) {
    return repository.findById(id);
  }

  @Override
  public List<StockItem> getAll() {
    return repository.findAll();
  }

  @Override
  public StockItem save(StockItem item) {
    return repository.save(item);
  }

  @Override
  public boolean setCurrentQuantity(BigDecimal quantity, Long id) {
    if (quantity.compareTo(BigDecimal.ZERO) < 0) return false;
    int numOfUpdatedRows = repository.setCurrentQuantity(quantity, id);
    return numOfUpdatedRows > 0;
  }
}
