package com.hazlewood.coffeetracker.beans;

import java.util.List;
import java.util.Optional;

public interface BeansService {
  Optional<Beans> getBeansById(Long id);

  List<Beans> getAllBeans();

  Optional<Beans> findByName(String name);

  boolean exists(String name);

  Beans save(Beans beans);
}
