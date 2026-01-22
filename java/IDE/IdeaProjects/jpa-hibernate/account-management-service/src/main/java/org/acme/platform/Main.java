package org.acme.platform;

import jakarta.persistence.EntityManager;
import org.acme.platform.account.domain.Account;
import org.acme.platform.account.domain.PurchaseOrder;
import org.acme.platform.account.infrastructure.JpaUtil;

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

            // Create orders
            PurchaseOrder order1 = new PurchaseOrder("ORD1001", "APPROVED", LocalDateTime.now());
            PurchaseOrder order2 = new PurchaseOrder("ORD1002", "PENDING", LocalDateTime.now());

            // Link orders to account
            account.addOrder(order1);
            account.addOrder(order2);

            // Persist the aggregate
            em.persist(account);

            em.getTransaction()
                    .commit();

            System.out.println("Account and orders persisted successfully!");
        } finally {
            em.close();
            JpaUtil.shutdown();
        }
    }
}
