package org.acme.platform.account.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    private String id;

    private String status;
    private LocalDateTime purchasedAt;
    private LocalDateTime deliveredAt;

    // ORM
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    // Default constrictor required by JPA as per said
    protected PurchaseOrder() {}

    // Constructor
    public PurchaseOrder(String id, String status, LocalDateTime purchasedAt) {
        this.id = id;
        this.status = status;
        this.purchasedAt = purchasedAt;
    }

    // Package-private setter for bidirectional link
    void setAccount(Account account) {
        this.account = account;
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
