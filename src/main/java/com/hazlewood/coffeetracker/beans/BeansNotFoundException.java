package com.hazlewood.coffeetracker.beans;

public class BeansNotFoundException extends RuntimeException {
  public BeansNotFoundException(Long id) {
    super("Could not find beans " + id);
  }
}
