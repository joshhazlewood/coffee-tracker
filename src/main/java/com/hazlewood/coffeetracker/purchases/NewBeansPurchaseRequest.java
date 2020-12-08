package com.hazlewood.coffeetracker.purchases;

import com.hazlewood.coffeetracker.beans.Beans;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class NewBeansPurchaseRequest {

  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal initialQuantity;

  private Beans beans;

  public NewBeansPurchaseRequest(BigDecimal initialQuantity, Beans beans) {
    this.initialQuantity = initialQuantity;
    this.beans = beans;
  }

  public BigDecimal getInitialQuantity() {
    return initialQuantity;
  }

  public void setInitialQuantity(BigDecimal initialQuantity) {
    this.initialQuantity = initialQuantity;
  }

  public Beans getBeans() {
    return beans;
  }

  public void setBeans(Beans beans) {
    this.beans = beans;
  }
}
