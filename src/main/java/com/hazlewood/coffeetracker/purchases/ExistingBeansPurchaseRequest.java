package com.hazlewood.coffeetracker.purchases;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class ExistingBeansPurchaseRequest {
  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal initialQuantity;

  @JsonProperty("beansID")
  @JsonAlias("beansId")
  private Long beansID;

  public ExistingBeansPurchaseRequest(BigDecimal initialQuantity, Long beansID) {
    this.initialQuantity = initialQuantity;
    this.beansID = beansID;
  }

  public BigDecimal getInitialQuantity() {
    return initialQuantity;
  }

  public void setInitialQuantity(BigDecimal initialQuantity) {
    this.initialQuantity = initialQuantity;
  }

  public Long getBeansID() {
    return beansID;
  }

  public void setBeansID(Long beansID) {
    this.beansID = beansID;
  }
}
