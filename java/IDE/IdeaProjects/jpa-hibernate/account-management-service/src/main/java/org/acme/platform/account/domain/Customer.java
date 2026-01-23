package org.acme.platform.account.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_unique_id")
    private String customerUniqueId;

    @Column(name = "customer_zip_code_prefix")
    private int zipCodePrefix;

    @Column(name = "customer_city")
    private String city;

    @Column(name = "customer_state")
    private String state;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrder> orders = new ArrayList<>();

    protected Customer() {}

    public Customer(String customerId, String customerUniqueId, int zipCodePrefix, String city, String state) {
        this.customerId = customerId;
        this.customerUniqueId = customerUniqueId;
        this.zipCodePrefix = zipCodePrefix;
        this.city = city;
        this.state = state;
    }

    public void addOrder(PurchaseOrder order) {
        orders.add(order);
        order.setCustomer(this);
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerUniqueId() {
        return customerUniqueId;
    }

    public int getZipCodePrefix() {
        return zipCodePrefix;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public List<PurchaseOrder> getOrders() {
        return orders;
    }
}