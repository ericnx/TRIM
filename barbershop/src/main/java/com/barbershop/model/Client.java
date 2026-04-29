package com.barbershop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn(name = "client_id")
public class Client extends Person {

    public Client() {
    }

    public Client(String firstName, String lastName, String phone, String email) {
        super(firstName, lastName, phone, email);
    }
}