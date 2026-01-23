package org.acme.platform;

import jakarta.persistence.EntityManager;
import org.acme.platform.account.domain.Account;
import org.acme.platform.account.domain.PurchaseOrder;
import org.acme.platform.account.infrastructure.JpaUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JpaUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            em.getTransaction().begin();

            // Create account
            Account account = new Account("CUST001", "alice@acme.com", "Sao Paulo", "SP");

            // Create orders with amounts
            PurchaseOrder order1 = new PurchaseOrder(
                    "ORD1001",
                    "APPROVED",
                    LocalDateTime.now(),
                    new BigDecimal("250.75")  // order amount
            );

            PurchaseOrder order2 = new PurchaseOrder(
                    "ORD1002",
                    "PENDING",
                    LocalDateTime.now(),
                    new BigDecimal("129.99")  // order amount
            );

            // Link orders to account (bidirectional)
            account.addOrder(order1);
            account.addOrder(order2);

            // Persist the aggregate
            em.persist(account);

            em.getTransaction().commit();

            System.out.println("Account and orders persisted successfully!");
        } finally {
            em.close();
            JpaUtil.shutdown();
        }
    }
}
