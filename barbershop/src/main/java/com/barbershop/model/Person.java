package com.barbershop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    public Person() {
    }

    public Person(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public Long getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setPersonId(Long id) {
        this.personId = id;
    }

    public void setFirstName(String s) {
        this.firstName = s;
    }

    public void setLastName(String s) {
        this.lastName = s;
    }

    public void setPhone(String s) {
        this.phone = s;
    }

    public void setEmail(String s) {
        this.email = s;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}