# Account Management Service

## Overview

Simple JPA/Hibernate app that persists `Customer` and `PurchaseOrder` entities to H2.

## Tech Stack

- Java 25, Maven 3.9+
- Jakarta Persistence API 3.2, Hibernate ORM 7.2
- H2 database 2.4

## Build & Run

```bash
mvn clean package exec:java
```

## Database Configuration

Default (file, persists between runs):

```xml
<property name="jakarta.persistence.jdbc.url"
          value="jdbc:h2:file:./data/accountdb;DB_CLOSE_DELAY=-1"/>
```

Switch to in-memory (no locks, resets each run):

```xml
<property name="jakarta.persistence.jdbc.url"
          value="jdbc:h2:mem:testdb"/>
```

Both live in src/main/resources/META-INF/persistence.xml; keep only one URL at a time.

## H2 Console (optional)

Launch:

```bash
java -cp ~/.m2/repository/com/h2database/h2/2.4.240/h2-2.4.240.jar org.h2.tools.Console
```

Use the same JDBC URL as above when connecting.

## Sample Behavior

On startup, `Main` creates one customer with two purchase orders, commits, and prints counts for customers and orders.

## Data Sources

CSV samples available in `csv/`:

- `olist_customers_dataset.csv`
- `olist_orders_dataset.csv`
