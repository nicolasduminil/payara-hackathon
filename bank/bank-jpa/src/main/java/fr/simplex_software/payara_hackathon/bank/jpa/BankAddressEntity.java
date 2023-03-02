package fr.simplex_software.payara_hackathon.bank.jpa;

import jakarta.persistence.*;

@Embeddable
public class BankAddressEntity
{
  private String streetName;
  private String streetNumber;
  private String cityName;
  private String poBox;
  private String zipCode;
  private String countryName;

  public BankAddressEntity()
  {
  }

  public BankAddressEntity(String streetName, String streetNumber, String cityName, String poBox, String zipCode, String countryName)
  {
    this.streetName = streetName;
    this.streetNumber = streetNumber;
    this.cityName = cityName;
    this.poBox = poBox;
    this.zipCode = zipCode;
    this.countryName = countryName;
  }

  public String getStreetName()
  {
    return streetName;
  }

  public void setStreetName(String streetName)
  {
    this.streetName = streetName;
  }

  public String getStreetNumber()
  {
    return streetNumber;
  }

  public void setStreetNumber(String streetNumber)
  {
    this.streetNumber = streetNumber;
  }

  public String getCityName()
  {
    return cityName;
  }

  public void setCityName(String cityName)
  {
    this.cityName = cityName;
  }

  public String getPoBox()
  {
    return poBox;
  }

  public void setPoBox(String poBox)
  {
    this.poBox = poBox;
  }

  public String getZipCode()
  {
    return zipCode;
  }

  public void setZipCode(String zipCode)
  {
    this.zipCode = zipCode;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName(String countryName)
  {
    this.countryName = countryName;
  }
}
