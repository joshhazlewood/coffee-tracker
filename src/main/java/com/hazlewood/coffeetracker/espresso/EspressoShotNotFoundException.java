package com.hazlewood.coffeetracker.espresso;

public class EspressoShotNotFoundException extends RuntimeException {
  public EspressoShotNotFoundException(Long id) {
    super("Could not find espresso shot" + id);
  }
}
