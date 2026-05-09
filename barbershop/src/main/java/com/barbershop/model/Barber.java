package com.barbershop.model;

public class Barber extends Person {

    private Long barberId;
    private String firstName;
    private String lastName;
    private String email;
    private String licenseNo;
    private String role;

    public Barber() {
    }

    public Long getBarberId() {
        return barberId;
    }

    public void setBarberId(Long barberId) {
        this.barberId = barberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public String getRole() {
        return role;
    }

    public void setLicenseNo(String s) {
        this.licenseNo = s;
    }

    public void setRole(String r) {
        this.role = r;
    }

}