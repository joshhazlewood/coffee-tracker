package com.hazlewood.coffeetracker.stock;

import com.hazlewood.coffeetracker.beans.Beans;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Table(name = "beans_stock")
public class BeansPurchase {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private Beans beans;

  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal initialQuantity;
  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal currentQuantity;

  public BeansPurchase() {}

  public BeansPurchase(Beans beans, BigDecimal initialQuantity, BigDecimal currentQuantity) {
    this.beans = beans;
    this.initialQuantity = initialQuantity;
    this.currentQuantity = currentQuantity;
  }

  @Override
  public String toString() {
    return "BeansPurchase{" +
        "id=" + id +
        ", beans_id=" + beans +
        ", initialQuantity=" + initialQuantity +
        ", currentQuantity=" + currentQuantity +
        '}';
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Beans getBeans() {
    return beans;
  }

  public void setBeans(Beans beans) {
    this.beans = beans;
  }

  public BigDecimal getInitialQuantity() {
    return initialQuantity;
  }

  public void setInitialQuantity(BigDecimal initialQuantity) {
    this.initialQuantity = initialQuantity;
  }

  public BigDecimal getCurrentQuantity() {
    return currentQuantity;
  }

  public void setCurrentQuantity(BigDecimal currentQuantity) {
    this.currentQuantity = currentQuantity;
  }
}
