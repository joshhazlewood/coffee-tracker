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
public class BeansPurchaseServiceImpl implements BeansPurchaseService {

  Logger log = LoggerFactory.getLogger(BeansPurchaseServiceImpl.class);

  @Autowired
  BeansPurchaseRepository repository;

  @Override
  public Optional<BeansPurchase> findById(Long id) {
    return repository.findById(id);
  }

  @Override
  public List<BeansPurchase> getAll() {
    return repository.findAll();
  }

  @Override
  public BeansPurchase save(BeansPurchase item) {
    return repository.save(item);
  }

  @Override
  public boolean setCurrentQuantity(BigDecimal quantity, Long id) {
    if (quantity.compareTo(BigDecimal.ZERO) < 0) return false;
    int numOfUpdatedRows = repository.setCurrentQuantity(quantity, id);
    return numOfUpdatedRows > 0;
  }
}
