package com.hazlewood.coffeetracker.stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StockItemService {
  Optional<StockItem> findById(Long id);

  List<StockItem> getAll();

  StockItem save(StockItem item);

  boolean setCurrentQuantity(BigDecimal quantity, Long id);

}
