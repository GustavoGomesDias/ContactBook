package com.contact;

public class Contact {
    private String fullName;
    private String phoneNumber;
    private String city;
    private String country;

    public Contact(String fullName, String phoneNumber, String city, String country) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.country = country;
    }

    public String getFirstName() {
        return fullName;
    }

    public void setFirstName(String firstName) {
        this.fullName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
