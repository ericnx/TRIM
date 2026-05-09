package com.barbershop.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "barber")
@PrimaryKeyJoinColumn(name = "barber_id")
public class Barber extends Person {

    public enum Role {
        staff, admin
    }

    @ManyToMany
    @JoinTable(name = "barber_services_mapping", joinColumns = @JoinColumn(name = "barber_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<BarberService> services;

    @Column(name = "license_no", nullable = false, unique = true)
    private String licenseNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public Barber() {
    }

    public Barber(String firstName, String lastName, String phone,
            String email, String licenseNo, Role role) {
        super(firstName, lastName, phone, email);
        this.licenseNo = licenseNo;
        this.role = role;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public Role getRole() {
        return role;
    }

    public void setLicenseNo(String s) {
        this.licenseNo = s;
    }

    public void setRole(Role r) {
        this.role = r;
    }

    public List<BarberService> getServices() {
    return services;
}
}