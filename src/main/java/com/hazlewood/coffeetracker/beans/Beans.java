package com.hazlewood.coffeetracker.beans;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "beans")
public class Beans {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank(message = "Name is required")
  private String name;

  @NotNull
  @DecimalMax(value = "2000")
  @DecimalMin(value = "1")
  private BigDecimal amount;

  protected Beans() {}

  public Beans(String name, BigDecimal amount) {
    this.name = name;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return String.format("Beans[id=%d, name='%s', amount='%.2f']", id, name, amount);
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

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
