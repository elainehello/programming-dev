# Service

RESTful application using Spring Boot 4.x

## Documentation

- [Spring Framework API](https://docs.spring.io/spring-framework/docs/current/javadoc-api/index.html)
- [Spring Boot Initializr](https://start.spring.io/)
- [SDKMAN! Package Manager](https://sdkman.io/)

```bash
sdk list java
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 25.0.1-graalce
java -v
```

## Dependencies

- Spring Web MVC
- Spring DevTools
- Docker Compose Support
- Spring Boot Actuator
- Spring Data JDBC
- PostgreSQL Driver
- H2 Database (for testing)

## Running the Application

### Start PostgreSQL with Docker Compose

```bash
# -d flag runs the container in detached mode (background)
docker compose up -d
```

Then, in another terminal window, run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

To build the application, run the following command in the root directory:

```bash
./mvnw package
```

change directory to target:

```bash
cd target
java -jar *.jar
```

To generate a GraalVM native image executable, use the following command:

```bash
./mvnw -DskipTests -Pnative native:compile
cd target
./service
```
