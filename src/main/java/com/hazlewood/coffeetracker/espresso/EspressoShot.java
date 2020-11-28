package com.hazlewood.coffeetracker.espresso;

import com.hazlewood.coffeetracker.beans.Beans;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Table(name = "shots")
public class EspressoShot {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private Beans beans;

  @DecimalMin(value = "0.0", inclusive = false)
  private BigDecimal inputWeight;

  @DecimalMin(value = "0.0", inclusive = false)
  @DecimalMax(value = "150")
  private BigDecimal outputWeight;

  @DecimalMin(value = "0.0", inclusive = false)
  @DecimalMax(value = "100", inclusive = false)
  private int timeInSeconds;

  @Nullable
  private int grindSetting;

  @Nullable
  private String notes;

  public EspressoShot() {}

  public EspressoShot(Beans beans, BigDecimal inputWeight, BigDecimal outputWeight, int timeInSeconds, int grindSetting, String notes) {
    this.beans = beans;
    this.inputWeight = inputWeight;
    this.outputWeight = outputWeight;
    this.timeInSeconds = timeInSeconds;
    this.grindSetting = grindSetting;
    this.notes = notes;
  }

  @Override
  public String toString() {
    return "EspressoShot{" +
        "id=" + id +
        ", beans=" + beans +
        ", inputWeight=" + inputWeight +
        ", outputWeight=" + outputWeight +
        ", time=" + timeInSeconds +
        ", notes='" + notes + '\'' +
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

  public BigDecimal getInputWeight() {
    return inputWeight;
  }

  public void setInputWeight(BigDecimal inputWeight) {
    this.inputWeight = inputWeight;
  }

  public BigDecimal getOutputWeight() {
    return outputWeight;
  }

  public void setOutputWeight(BigDecimal outputWeight) {
    this.outputWeight = outputWeight;
  }

  public int getTimeInSeconds() {
    return timeInSeconds;
  }

  public void setTimeInSeconds(int timeInSeconds) {
    this.timeInSeconds = timeInSeconds;
  }

  public int getGrindSetting() {
    return grindSetting;
  }

  public void setGrindSetting(int grindSetting) {
    this.grindSetting = grindSetting;
  }

  @Nullable
  public String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }
}
