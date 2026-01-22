package org.acme.platform.account.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Account {

    // attributes
    @Id
    private String id;

    private String email;
    private String city;
    private String state;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<PurchaseOrder> orders = new ArrayList<>();

    // Default constructor required by JPA
    protected Account() {}

    // Constructor
    public Account(String id, String email, String city, String state) {
        this.id = id;
        this.email = email;
        this.city = city;
        this.state = state;
        this.createdAt = LocalDateTime.now();
    }

    // Add order helper
    public void addOrder(PurchaseOrder order) {
        orders.add(order);
        order.setAccount(this);
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<PurchaseOrder> getOrders() {
        return orders;
    }
}
