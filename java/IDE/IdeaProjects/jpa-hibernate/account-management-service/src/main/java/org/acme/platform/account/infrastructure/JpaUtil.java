package org.acme.platform.account.infrastructure;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    // Jakarta object instantiation <EntityManagerFactory>
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("accountPU");

    // Getter
    public static EntityManagerFactory getEntityManagerFactory() {
        return  emf;
    }

    // close() jakarta
    public static void shutdown() {
        emf.close();
    }
}
