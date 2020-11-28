package com.hazlewood.coffeetracker.espresso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EspressoShotServiceImpl implements EspressoShotService {

  @Autowired private EspressoShotRepository repository;

  @Override
  public Optional<EspressoShot> findById(Long id) {
    return repository.findById(id);
  }

  @Override
  public List<EspressoShot> getAll() {
    return repository.findAll();
  }

  @Override
  public EspressoShot save(EspressoShot item) {
    return repository.save(item);
  }
}
