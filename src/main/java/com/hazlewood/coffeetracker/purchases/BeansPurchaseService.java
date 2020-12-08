package com.hazlewood.coffeetracker.stock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BeansPurchaseService {
  Optional<BeansPurchase> findById(Long id);

  List<BeansPurchase> getAll();

  BeansPurchase save(BeansPurchase item);

  boolean setCurrentQuantity(BigDecimal quantity, Long id);

}
