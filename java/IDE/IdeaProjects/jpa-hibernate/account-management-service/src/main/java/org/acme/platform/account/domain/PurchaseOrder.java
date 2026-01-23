package org.acme.platform.account.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @Column(name = "order_id")
    private String id;

    @Column(name = "order_status")
    private String status;

    @Column(name = "order_purchase_timestamp")
    private LocalDateTime purchasedAt;

    @Column(name = "order_delivered_customer_date")
    private LocalDateTime deliveredAt;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    protected PurchaseOrder() {}

    public PurchaseOrder(String id, String status, LocalDateTime purchasedAt, BigDecimal amount) {
        this.id = id;
        this.status = status;
        this.purchasedAt = purchasedAt;
        this.amount = amount;
    }

    void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}