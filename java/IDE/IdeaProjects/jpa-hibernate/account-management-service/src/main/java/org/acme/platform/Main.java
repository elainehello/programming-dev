package org.acme.platform;

import jakarta.persistence.EntityManager;
import org.acme.platform.account.domain.Customer;
import org.acme.platform.account.domain.PurchaseOrder;
import org.acme.platform.account.infrastructure.JpaUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JpaUtil
                .getEntityManagerFactory()
                .createEntityManager();

        try {
            em.getTransaction().begin();

            // Create customer
            Customer customer = new Customer("CUST001", "UNIQ001", 12345, "Sao Paulo", "SP");

            // Create orders with amounts
            PurchaseOrder order1 = new PurchaseOrder(
                    "ORD1001",
                    "APPROVED",
                    LocalDateTime.now(),
                    new BigDecimal("250.75"));

            PurchaseOrder order2 = new PurchaseOrder(
                    "ORD1002",
                    "PENDING",
                    LocalDateTime.now(),
                    new BigDecimal("129.99"));

            // Link orders to customer (bidirectional)
            customer.addOrder(order1);
            customer.addOrder(order2);

            // Persist the aggregate
            em.persist(customer);

            em.getTransaction().commit();

            System.out.println("Customer and orders persisted successfully!");

            // Query all customers
            List<Customer> customers = em.createQuery("SELECT c FROM Customer c", Customer.class)
                    .getResultList();
            System.out.println("Number of customers: " + customers.size());

            // Query all orders
            List<PurchaseOrder> orders = em.createQuery("SELECT o FROM PurchaseOrder o", PurchaseOrder.class)
                    .getResultList();
            System.out.println("Number of orders: " + orders.size());

        } finally {
            em.close();
            JpaUtil.shutdown();
        }
    }
}
