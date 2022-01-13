package com.contact;

import java.io.Serializable;

public class Contact implements Serializable, Comparable<Contact> {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName) {
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

    @Override
    public int compareTo(Contact o) {
        return this.getFullName().compareToIgnoreCase(o.getFullName());
    }
}
