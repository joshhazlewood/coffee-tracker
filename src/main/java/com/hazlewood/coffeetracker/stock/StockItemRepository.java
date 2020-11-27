package com.hazlewood.coffeetracker.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
@Transactional
public interface StockItemRepository extends JpaRepository<StockItem, Long> {
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update StockItem i set i.currentQuantity = ?1 where i.id = ?2")
  int setCurrentQuantity(BigDecimal quantity, Long id);
}
