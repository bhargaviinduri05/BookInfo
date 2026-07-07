# ---------- Build Stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create the directory where book cover images will be stored
RUN mkdir -p /app/uploads/book-covers

COPY --from=build /app/target/book-management-system.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
