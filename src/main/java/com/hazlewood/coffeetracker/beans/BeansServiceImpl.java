package com.hazlewood.coffeetracker.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BeansServiceImpl implements BeansService {

  Logger log = LoggerFactory.getLogger(BeansServiceImpl.class);

  @Autowired private BeansRepository repository;

  @Override
  public List<Beans> getAllBeans() {
    return repository.findAll();
  }

  @Override
  public Optional<Beans> findById(Long id) {
    return repository.findById(id);
  }

  @Override
  public Optional<Beans> findByName(String name) {
    return repository.findByName(name);
  }

  @Override
  public Beans save(Beans beans) {
    log.info("Saving new beans: [{}]", beans.getName());
    return repository.save(beans);
  }

  @Override
  public boolean exists(String name) {
    return repository.findByName(name).isPresent();
  }

  @Override
  public boolean exists(Long id) {
    return repository.findById(id).isPresent();
  }
}
