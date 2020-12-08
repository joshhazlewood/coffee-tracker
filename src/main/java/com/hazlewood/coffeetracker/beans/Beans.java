package com.hazlewood.coffeetracker.beans;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "beans")
public class Beans {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Roastery is required")
  private String roastery;

  @Nullable
  private String cupProfile;

  @Nullable
  private String countryOfOrigin;

  public Beans() {}

  public Beans(String name, String roastery, String cupProfile, String countryOfOrigin) {
    this.name = name;
    this.roastery = roastery;
    this.cupProfile = cupProfile;
    this.countryOfOrigin = countryOfOrigin;
  }

  @Override
  public String toString() {
    return String.format("Beans[id=%d, name='%s', roastery='%s']", id, name, roastery);
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

  public String getRoastery() {
    return roastery;
  }

  public void setRoastery(String roastery) {
    this.roastery = roastery;
  }

  @Nullable
  public String getCupProfile() {
    return cupProfile;
  }

  public void setCupProfile(String cupProfile) {
    this.cupProfile = cupProfile;
  }

  @Nullable
  public String getCountryOfOrigin() {
    return countryOfOrigin;
  }

  public void setCountryOfOrigin(@Nullable String countryOfOrigin) {
    this.countryOfOrigin = countryOfOrigin;
  }
}
