package com.hazlewood.coffeetracker.beans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface BeansRepository extends JpaRepository<Beans, Long> {
  Optional<Beans> findByName(String name);
}
