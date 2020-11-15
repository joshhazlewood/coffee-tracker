package com.hazlewood.coffeetracker.beans;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Beans {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  private double amount;

  protected Beans() {}

  public Beans(String name, double amount) {
    this.name = name;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return String.format("Beans[id=%d, name='%s', amount='%f']", id, name, amount);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }
}
