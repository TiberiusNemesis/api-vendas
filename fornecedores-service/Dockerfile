# Build with Maven and Java 17.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies separately from source changes.
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests -B

# Run the packaged application with the Java 17 JRE.
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
