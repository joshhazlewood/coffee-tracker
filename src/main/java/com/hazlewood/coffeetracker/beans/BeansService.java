package com.hazlewood.coffeetracker.beans;

import java.util.List;
import java.util.Optional;

public interface BeansService {
  Optional<Beans> findById(Long id);

  List<Beans> getAllBeans();

  Optional<Beans> findByName(String name);

  boolean exists(String name);

  boolean exists(Long id);

  Beans save(Beans beans);
}
