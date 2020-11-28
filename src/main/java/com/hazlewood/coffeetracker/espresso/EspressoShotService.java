package com.hazlewood.coffeetracker.espresso;

import java.util.List;
import java.util.Optional;

public interface EspressoShotService {
  Optional<EspressoShot> findById(Long id);

  List<EspressoShot> getAll();

  EspressoShot save(EspressoShot item);
}
